package name.heavycarbon.utils.test

import static groovy.test.GroovyAssert.shouldFail
import name.heavycarbon.utils.BooleanParser;

import org.junit.Test

class JUnit_BooleanParser {

	final static CLASS = JUnit_BooleanParser.class.name

	@Test
	void testMakeBoolean() {
		assert BooleanParser.makeBoolean('true') == true
		assert BooleanParser.makeBoolean('  true   ') == true
		assert BooleanParser.makeBoolean('T') == true
		assert BooleanParser.makeBoolean('YES') == true
		assert BooleanParser.makeBoolean('Y') == true
		assert BooleanParser.makeBoolean('on') == true
		assert BooleanParser.makeBoolean('oN') == true
		assert BooleanParser.makeBoolean('1') == true

		assert BooleanParser.makeBoolean('false') == false
		assert BooleanParser.makeBoolean('F') == false
		assert BooleanParser.makeBoolean('NO') == false
		assert BooleanParser.makeBoolean('N') == false
		assert BooleanParser.makeBoolean('off') == false
		assert BooleanParser.makeBoolean('oFF') == false
		assert BooleanParser.makeBoolean('0') == false
		
		shouldFail {
			BooleanParser.makeBoolean(null)
		}
		shouldFail {			
			assert BooleanParser.makeBoolean('X')
		}
		shouldFail {			
			assert BooleanParser.makeBoolean('')
		}		
	}
}
