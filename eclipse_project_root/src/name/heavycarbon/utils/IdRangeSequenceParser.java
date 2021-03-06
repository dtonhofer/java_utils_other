package name.heavycarbon.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static name.heavycarbon.checks.BasicChecks.*;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 * *****************************************************************************
 * A class that has a function able to parse a "range sequence" string
 * and give back a RangeSequence.
 * 
 * TODO: Allow multiline range sequence, trailing separator, sequence of
 *       separators, missing separator if there is a newline
 * 
 * 2009.12.18 - Moved out of RangeSequence to its own toplevel class.
 * 2010.09.27 - Moved from the specialized project "70_msgserver_cli" 
 *              to project "04_core_low" and package "com.mplify.id_ranges".
 ******************************************************************************/

@SuppressWarnings({ "unchecked", "rawtypes" })
public class IdRangeSequenceParser<T extends AbstractId> {

    /**
     * Pattern used in parsing a stringified range sequence in addFromString()
     */

    private final static Pattern patternId = Pattern.compile("^\\s*(\\d+)(.*)$");
    private final static Pattern patternIdBracketed = Pattern.compile("^\\s*\\[\\s*(\\d+)\\s*\\](.*)$");
    private final static Pattern patternIdRange = Pattern.compile("^\\s*(\\d+)\\-(\\d+)(.*)$");
    private final static Pattern patternIdBracketedRange = Pattern.compile("^\\s*\\[\\s*(\\d+)\\-(\\d+)\\s*\\](.*)$");
    private final static Pattern patternSeparator = Pattern.compile("^\\s*(,|;)(.*)$");
    private final static Pattern emptyLine = Pattern.compile("^\\s*$");

    /**
     * Parses a string like "6767, 7888-9999, 9990, [4556], 11255, [3344-3322]" and adds the resulting ranges. The
     * function is NOT static because it depends on the generic type of the enclosing class.
     */

//    @SuppressWarnings("static-method")
    public IdRangeSequence<T> parse(String rawIn, IdFactory<T> make,boolean checkSequence) {
        checkNotNull(rawIn,"String");
        checkNotNull(make,"IdFactory");
        IdRangeSequence<T> res = new IdRangeSequence();
        String raw = rawIn;
        boolean separatorMustExist = false;
        while (!emptyLine.matcher(raw).matches()) {
            // A separator MAY or MUST occur
            {
                Matcher matcherSeparator = patternSeparator.matcher(raw);
                if (matcherSeparator.matches()) {
                    raw = matcherSeparator.group(2);
                    separatorMustExist = false; // more separators may follow but they are optional
                    continue; // LOOP AROUND
                } else {
                    if (separatorMustExist) {
                        throw new IllegalArgumentException("Expected separator at '" + raw + "'");
                    }
                }
            }
            // Check for ranges first otherwise they won't be recognized
            {
                Matcher matcherIdRange = patternIdRange.matcher(raw);
                if (matcherIdRange.matches()) {
                    Integer left = new Integer(matcherIdRange.group(1));
                    Integer right = new Integer(matcherIdRange.group(2));
                    raw = matcherIdRange.group(3);
                    res.addRange(new IdRange(make.make(left), make.make(right)),checkSequence);
                    separatorMustExist = true;
                    continue; // LOOP AROUND
                }
            }
            {
                Matcher matcherIdBracketedRange = patternIdBracketedRange.matcher(raw);
                if (matcherIdBracketedRange.matches()) {
                    Integer left = new Integer(matcherIdBracketedRange.group(1));
                    Integer right = new Integer(matcherIdBracketedRange.group(2));
                    raw = matcherIdBracketedRange.group(3);
                    res.addRange(new IdRange(make.make(left), make.make(right)),checkSequence);
                    separatorMustExist = true;
                    continue; // LOOP AROUND                                    
                }
            }
            {
                Matcher matcherId = patternId.matcher(raw);
                if (matcherId.matches()) {
                    Integer id = new Integer(matcherId.group(1)); // might fail if number too long                
                    raw = matcherId.group(2);
                    res.addRange(new IdRange(make.make(id)),checkSequence);
                    separatorMustExist = true;
                    continue; // LOOP AROUND                                    
                }
            }
            {
                Matcher matcherIdBracketed = patternIdBracketed.matcher(raw);
                if (matcherIdBracketed.matches()) {
                    Integer id = new Integer(matcherIdBracketed.group(1)); // might fail if number too long                
                    raw = matcherIdBracketed.group(2);
                    res.addRange(new IdRange(make.make(id)),checkSequence);
                    separatorMustExist = true;
                    continue; // LOOP AROUND                                    
                }
            }
            // if we are here, failure
            throw new IllegalArgumentException("Expected id or sequence at '" + raw + "'");
        }
        return res;
    }
}
