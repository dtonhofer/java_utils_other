package hoover

import java.util.regex.Pattern
import static com.example.BasicChecks.*

class FileFormat {

    enum Key  {
        HAS_HEADER,
        READ_HEADER
    }

    final boolean hasHeader    // indicates that the input file has a header line; default true
    final boolean readHeader   // indicates that the header should be read instead of ignored; default false
    final Pattern seppat       // regular expression matching separators; generally based on a single character
    final List    fields = []  // a list of FieldFormat instances
    
    /**
     * Helper to check the map for the entry "key" and use that if it exists, and use the "deflt" otherwise
     */
    
    static boolean fromMap(Map map, boolean deflt, key) {
        if (map.containsKey(key)) {
            return false || map[key] // basically cast Groovy Truth to a boolean
        }
        else {
            return deflt
        }
    }

    /**
     * Construct from a separator pattern, setting all additional options to default
     */
    
    FileFormat(Pattern separatorPattern) {
        this(separatorPattern,[:])
    }

    /**
     * Construct from a separator pattern and a set of options stored in a Map, with the keys chosen from
     * the "Key" enumeration
     */
    
    FileFormat(Pattern separatorPattern, Map data) {
        checkNotNull(separatorPattern,"separatorPattern")
        hasHeader = fromMap(data, true, Key.HAS_HEADER)
        readHeader = fromMap(data, false, Key.READ_HEADER)
        seppat = separatorPattern
    }
    
    /**
     * Any of the fields marked as being the "primary key"? This will cause CsvSlurper to create
     * a Map indexed by that field
     */
    
    boolean hasPrimaryKey() {
       Collection pkeys = fields.findAll { FieldFormat fiefo -> fiefo.primaryKey }
       checkTrue(pkeys.size()<=1,"There are ${pkeys.size()} fields marked as primary keys")
       return !pkeys.isEmpty()
    }
    
    /**
     * Called by CsvSlurper to mark the last field in the "fields" array
     */
    
    void markLastField() {  
        int lastIndex = fields.size()-1      
        fields.eachWithIndex { FieldFormat fiefo, int i ->  
            fiefo.lastField = (i == lastIndex)
        }
    }
}
