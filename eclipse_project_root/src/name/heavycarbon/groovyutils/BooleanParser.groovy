package name.heavycarbon.groovyutils;

import name.heavycarbon.utils.PropertyName

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 *******************************************************************************
 * Parsing a boolean 
 *
 * 2013.01.29 - Done
 ******************************************************************************/

public class BooleanParser {

    private final static Set YES_SET = [ "yes", "true", "on", "y", "t", "1" ] as Set
    private final static Set NO_SET = [ "no", "false", "off", "n", "f", "0" ] as Set
    
    private BooleanParser() {
        // Cannot instantiate
    }
    
    /**
     * Parse the passed 'value' into a boolean - many values are acceptable. Returns null on nomatch.
     * Returns null if null is passed. 
     */

    public static Boolean parse(String value) {
        String x = PropertyName.namify(value)
        // "x" may be null
        if (YES_SET.contains(x)) {
            return Boolean.TRUE
        }
        else if (NO_SET.contains(x)) {
            return Boolean.FALSE
        }
        else {
            return null
        }
    }
}
