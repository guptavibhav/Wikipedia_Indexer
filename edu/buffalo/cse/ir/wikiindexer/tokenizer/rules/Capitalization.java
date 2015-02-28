package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.CAPITALIZATION)
public class Capitalization implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		// TODO Auto-generated method stub
		
		stream.IsPretokenizationState = true;
		String str =stream.TknStreamBldr.toString();
		
		Pattern capsPattern = Pattern.compile("[.\\s?!]?[\\s]*\\b([A-Z](\\w*))\\b");
		Matcher m = capsPattern.matcher(str);
		
		StringBuffer tempBuffer = new  StringBuffer() ; 
		while( m.find())
			
		{
			/*String grp0 = m.group(0);
			String grp1 = m.group(1);
			String grp2 = m.group(2);
			boolean ind =  !(m.group(2).matches("[a-z]+")) ;*/
	       if( /* m.group(1).matches("[A-Z]+")  // All caps
	           ||*/ m.group(0).matches("\\s[\\w]+") // "NO PUNCTUATION -- SPACE -- WORD "
	           || !(m.group(2).matches("[a-z]+")) // Camel Case OR ALL CAPS  
	          )
	       {
	    	   // Dont replace OR equivalent is replace with itself
	    	   m.appendReplacement(tempBuffer, Matcher.quoteReplacement(m.group(0)));
	       }
	       else 
	       {
	    	
	    	   //str = str.substring(0,m.start(0)) + m.group(0).toLowerCase()+ str.substring(m.end(0),str.length());
	    	   m.appendReplacement(tempBuffer, Matcher.quoteReplacement(m.group(0).toLowerCase()));
	    	//str=str.replace(m.group(0),m.group(0).toLowerCase() );
	    	
	    	/*s*/
	    	
	    	
	       }
		}
		m.appendTail(tempBuffer);
		stream.TknStreamBldr.setLength(0); 
		stream.TknStreamBldr.append(tempBuffer.toString().trim());
		tempBuffer.setLength(0);

	}

}
