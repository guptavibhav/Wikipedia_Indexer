/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer.test;

import java.util.Properties;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import edu.buffalo.cse.ir.wikiindexer.IndexerConstants;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;

/**
 * @author nikhillo
 *
 */
@RunWith(Parameterized.class)
public class WhitespaceRuleTest extends TokenizerRuleTest {

	public WhitespaceRuleTest(Properties props) {
		super(props, IndexerConstants.WHITESPACERULE);
	}
	
	@Test
	public void testRule() {
		if (rule == null) {
			fail("Rule not implemented");
		} else {
			try {
				assertArrayEquals(new Object[]{"this","is","a","test" }, 
						runtest("this is a test"));
				assertArrayEquals(new Object[]{"this","is","a","test" }, 
						runtest("this    is     a      test"));
				assertArrayEquals(new Object[]{"this","is","a","test" }, 
						runtest("    this is a test       "));
				assertArrayEquals(new Object[]{"this","is","a","test" }, 
						runtest("this "
								+ "is \r"
								+ "a \n test"));
				assertArrayEquals(new Object[]{"thisisatest" }, 
						runtest("thisisatest"));
				
				assertArrayEquals(new Object[]{"This Is New Test" }, 
						runtest("This Is New Test"));
				
				assertArrayEquals(new Object[]{"This Is", "A" ,"Test" }, 
						runtest("This Is A Test"));
				assertArrayEquals(new Object[]{"This", "IS" ,"A",  "Test" }, 
						runtest("This IS A Test"));
				
				
		/*		Delim dlmRl = new Delim("_");
				//TokenStream  testStream = new TokenStream("This_is_a_test");
				//TokenStream  testStream = new TokenStream("This_Is_New_Test");
				  TokenStream  testStream = new TokenStream("This_Is_A_Test");
				
				dlmRl.apply(testStream);
						
				assertArrayEquals(new Object[]{"This", "is" ,"a",  "test" }, 
						testStream.getAllTokens().toArray());

				assertArrayEquals(new Object[]{"This Is New Test" }, 
						testStream.getAllTokens().toArray());
				assertArrayEquals(new Object[]{"This Is" ,"A", "Test" }, 
						testStream.getAllTokens().toArray());
*/
				
			} catch (TokenizerException e) {
				
			}
		}
	}

}
