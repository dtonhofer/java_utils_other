package name.heavycarbon.utils;

import static name.heavycarbon.checks.BasicChecks.*;

public class BooleanParser {

	private BooleanParser() {
		// cannot be instantiated
	}
	
    /**
     * Parse the passed 'value' into a boolean - many values are acceptable but if none of these fits, an exception is
     * thrown. (null) cannot be passed.
     */

    public static boolean parseBoolean(String value) {
    	checkNotNull(value,"value meant to denote a boolean");
        String lvalue = value.toLowerCase().trim();
        if (lvalue.equals("yes") || lvalue.equals("true") || lvalue.equals("on") || lvalue.equals("1") || lvalue.equals("y") || lvalue.equals("t")) {
            return true;
        }
        else if (lvalue.equals("no") || lvalue.equals("false") || lvalue.equals("off") || lvalue.equals("0") || lvalue.equals("n") || lvalue.equals("f")) {
            return false;
        }
        else {
        	instaFail("The passed value '" + value + "' cannot be interpreted as a boolean");
        	return false; // never get here
        }
    }
    
}
