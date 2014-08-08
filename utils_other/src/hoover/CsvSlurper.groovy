package hoover

import static com.example.BasicChecks.*

import java.util.regex.Matcher
import java.util.regex.Pattern

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.example.resources.ResourceHelp
import com.example.resources.ResourceInfo
import com.mplify.logging.LogFacilities
import com.mplify.logstarter.LogbackStarter
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import groovy.lang.Closure;


class CsvSlurper {

    private final static CLASS = CsvSlurper.class.name

    static scoopOneOffOuter(String restOfLine, String line, int i, FieldFormat fiefo, Pattern seppat) {
        def (String front, String rest) = scoopOneOff(restOfLine, fiefo, seppat)
        def String fieldValue
        if (front == null) {
            // no further separator found; possible if this is the last entry!
            if (!fiefo.lastField) {
                instaFail("Expected a separator after field ${i} but found none in line ${line}")
            }
            else {
                fieldValue = rest
            }
        }
        else {
            fieldValue = front
        }
        return [ fieldValue, rest ]
    }

    static scoopOneOff(String restOfLine, FieldFormat fiefo, Pattern seppat) {
        Matcher m = seppat.matcher(restOfLine)
        boolean yes = m.find()
        if (yes) {
            // a separator found; handle specical cases
            // System.out << m.start() << "->" << m.end() << "\n"
            String newRestOfLine 
            if (m.end() < restOfLine.length()) {
                // something is left!
                newRestOfLine = restOfLine[m.end() .. restOfLine.length()-1]
            }
            else {
                // basically, the line ends with a separator
                newRestOfLine = ""
            }
            String scoopedOff
            if (m.start()>0) {
                // there is indeed something at the front                
                scoopedOff = restOfLine[0 .. m.start()-1]
            }
            else {
                scoopedOff = ""
            }
            return [scoopedOff, newRestOfLine]
        }
        else {
            // no further separator found - group everything into one last field
            return [restOfLine, null]
        }
    }
    
    static class ProcessResult {
        
        
        Map validData = [:] 
        Map invalidData
        
        
    }

    static Map processLines(List<String> lines, FileFormat ff) {
        Logger logger = LoggerFactory.getLogger("${CLASS}.processLines")
        checkNotNull(lines, "lines")
        checkNotNull(ff, "file format")
        checkNotNullAndNotEmpty(ff.fields, "list of field format")
        //
        // Prepare
        //
        ff.markLastField()
        //
        // Fill this and return it as a result
        //
        Map res = [:]
        //
        // The the key of "res" is either one of the fields or a running line number starting from 0
        //
        int counter = 0
        boolean usePrimaryKey = ff.hasPrimaryKey()
        boolean thisIsTheHeader = ff.hasHeader
        def hmFormatter = org.joda.time.format.DateTimeFormat.forPattern("H:m")
        def dMYFormatter = org.joda.time.format.DateTimeFormat.forPattern("d/M/Y")
        def headerMap = [:] // map index to header name
        //
        // Process each line
        //
        lines.each { String line ->
            //
            // Skip empty or comment lines
            //
            def trimmedLine = line.trim()
            if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                return
            }
            //
            // Skip header if so demanded
            //
            if (thisIsTheHeader) {
                if (ff.readHeader) {
                    String restOfLine = line
                    ff.fields.eachWithIndex { FieldFormat fiefo, int i ->                        
                        def (String headerString, String newRest) = scoopOneOffOuter(restOfLine, line, i, fiefo, ff.seppat)
                        headerMap[i] = headerString.trim()
                        restOfLine = newRest
                    }                    
                }
                else {
                    // skip the header
                }
                thisIsTheHeader = false
                return
            }
            //
            // Generic processing if here...
            //
            String restOfLine = line
            
            System.out << "~~~~" << line << "~~~~" << "\n"
            
            Map thisLine = [:] 
            
            ff.fields.eachWithIndex { FieldFormat fiefo, int i ->
                def (String fieldValue, String newRest) = scoopOneOffOuter(restOfLine, line, i, fiefo, ff.seppat)
                assert fieldValue != null
                
                System.out << fieldValue << " + " << newRest << "\n"
                
                restOfLine = newRest
                
                def fieldValueTrimmed = fieldValue.trim()
                
                if (fieldValueTrimmed.isEmpty() && fiefo.mayBeEmpty) {
                    // "empty value" is null, which may be what one wants or not
                    thisLine[i] = null                    
                }
                else {
                    // either filled or cannot be empty
                    try {
                    switch (fiefo.type) {
                        case FieldFormat.Type.DATE:
                            thisLine[i] = dMYFormatter.parseLocalDate(fieldValue)
                            break
                        case FieldFormat.Type.INTEGER:                          
                            thisLine[i] = new Integer(fieldValue)
                            break
                        case FieldFormat.Type.STRING:
                            thisLine[i] = fieldValue
                            break
                        case FieldFormat.Type.TIME:                            
                            thisLine[i] = hmFormatter.parseLocalTime(fieldValue)
                            break
                        default:
                            cannotHappen("Uncaught type '${fiefo.type}' -- fix the code!")
                    }    
                    }
                    catch (Exception exe) {
                        throw new IllegalArgumentException("Trying to parse ${fieldValue} into ${fiefo.type} header is ${headerMap[i]}",exe)
                    }                   
                }                
            }      
            for ( idx in 0..thisLine.size()-1 ) { 
                System.out << idx << "=" << headerMap[idx] << " => " << thisLine[idx] << "\n"
            }
        }
        return res
    }

    /**
     * Read all the data
     */

    static List slurpData() {
        def hookClass = data.Hook.class
        def resourceInfo = new ResourceInfo(hookClass, "flightdata.txt" ,"UTF-8")
        return ResourceHelp.slurpIntoListOfLines(resourceInfo)
    }


    static void main(String[] argv) {
        def logger = LoggerFactory.getLogger("${CLASS}.main")
        new LogbackStarter(CsvSlurper.class)
        //
        // read
        //
        def lines = slurpData()
        //
        // prepare file format
        //
        def seppat = ~/,/
        def hh = FileFormat.Key.HAS_HEADER
        def rh = FileFormat.Key.READ_HEADER
        def ff = new FileFormat(seppat, [(rh) : true])
        //
        // prepare field formats
        //
        def mbe = FieldFormat.Key.MAY_BE_EMPTY
        ff.fields << new FieldFormat(FieldFormat.Type.STRING)  // FLIGHT_NO
        ff.fields << new FieldFormat(FieldFormat.Type.STRING)  // INVOICED_BY
        ff.fields << new FieldFormat(FieldFormat.Type.DATE)    // LEG_DATE
        ff.fields << new FieldFormat(FieldFormat.Type.STRING)  // IMMAT
        ff.fields << new FieldFormat(FieldFormat.Type.STRING)  // DEP
        ff.fields << new FieldFormat(FieldFormat.Type.STRING)  // ARR
        ff.fields << new FieldFormat(FieldFormat.Type.TIME, [ (mbe) : true ])    // ATD
        ff.fields << new FieldFormat(FieldFormat.Type.TIME, [ (mbe) : true ])    // ATA
        ff.fields << new FieldFormat(FieldFormat.Type.INTEGER, [ (mbe) : true ]) // PAX
        ff.fields << new FieldFormat(FieldFormat.Type.INTEGER, [ (mbe) : true ]) // FLI_MINUTES
        ff.fields << new FieldFormat(FieldFormat.Type.INTEGER, [ (mbe) : true ]) // BLOCK_MINUTES
        ff.fields << new FieldFormat(FieldFormat.Type.INTEGER, [ (mbe) : true ]) // DISTANCE_NM
        ff.fields << new FieldFormat(FieldFormat.Type.INTEGER, [ (mbe) : true ]) // FUEL_LBS_CONSUMED
        //
        // process
        //
        processLines(lines, ff)
    }
}
