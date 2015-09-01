package name.heavycarbon.utils;

import static name.heavycarbon.checks.BasicChecks.checkNotNull;
import static name.heavycarbon.checks.BasicChecks.instaFail;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 *******************************************************************************
 * Parsing a string representing a boolean in the most obvious way 
 *
 * Here is one in Groovy, btw:
 * 
 * static Boolean makeBoolean(String x, String line) {
 *    Logger logger = LOGGER_makeBoolean
 *    BasicChecks.checkNotNull(x)
 *    	x = x.toLowerCase().trim()
 *    	if (x =~ /^1|y(es)?|t(rue)?|on$/) {
 *		    return true
 *      }
 *      else if (x =~ /^0|n(o)?|f(alse)?|off$/) {
 *          return false
 *      }
 *      else {
 *         logger.warn("Could not transform '${x}' into a boolean on line '${line}'")
 *         return null
 *      }
 * }
 *  
 * 2013.01.29 - Done
 ******************************************************************************/

public class BooleanParser {

	private final static Set<String> YES_SET;
	private final static Set<String> NO_SET;

	static {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Set<String> yesSet = new HashSet();
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Set<String> noSet = new HashSet();
		String[] yesArray = new String[] { "yes", "true", "on", "y", "t", "1" };
		String[] noArray = new String[] { "no", "false", "off", "n", "f", "0" };
		for (int i = 0; i < yesArray.length; i++) {
			yesSet.add(yesArray[i]);
		}
		for (int i = 0; i < noArray.length; i++) {
			noSet.add(noArray[i]);
		}
		YES_SET = Collections.unmodifiableSet(yesSet);
		NO_SET = Collections.unmodifiableSet(noSet);
	}

	private BooleanParser() {
		// Cannot instantiate
	}

	/**
	 * Transform the passed 'value' into a boolean - many values are acceptable. 
	 * Throws if bad value (in particular, null) and if the value could not be recognized
	 */

	public static Boolean makeBoolean(String value) {
		checkNotNull(value,"value");
		String x = AbstractName.namify(value);
		// "x" may be null if not recognized
		if (YES_SET.contains(x)) {
			return Boolean.TRUE;
		} else if (NO_SET.contains(x)) {
			return Boolean.FALSE;
		} else {
			instaFail("The passed value '" + value + "' could not be recognized");
			return null; // never get here
		}
	}
    
}
