package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.SPECIALCHARS)

public class SpecialCharacters implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		// TODO Auto-generated method stub

		stream.IsPretokenizationState=true;
		
		String str = stream.TknStreamBldr.toString();
		//(?<=(\\w|\\s))
	/*	Pattern P = Pattern.compile("[\\W-[\\.|!|?|-]]");
		Matcher m = P.matcher(str);
		
		String replacementString;
		while(m.find())
		{
		  	replacementString = 
			
		}*/
		str = str.replaceAll("[^\\w.!?\\s\\-'-[_]]"," ");
		str = str.replaceAll("(\\s)+", "$1");	
		stream.TknStreamBldr.setLength(0);
		stream.TknStreamBldr.append(str.trim());
	}

}
