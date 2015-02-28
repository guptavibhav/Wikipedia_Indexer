package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.HYPHEN)
public class Hyphen implements TokenizerRule {

	@Override
	
	public void apply(TokenStream stream) throws TokenizerException {
		// TODO Auto-generated method stub

		stream.reset();
		String token ;
		String oldtoken ;
		while (stream.hasNext())
		{
		   
			token = stream.next();
			oldtoken = token;
			token = token.replaceAll("(\\b[a-zA-Z_]+\\b)-+(\\b[a-zA-Z_]+\\b)","$1 $2");
			token = token.replaceAll("-+(?!\\w)","");
			token = token.replaceAll("(?<!\\w)-+","");
			token = token.replaceAll("(?<=\\s)-+(?=\\s)",""); // can handle -[\s] or [\s]- replacement if required 
			token= token.trim();
			if(!oldtoken.equals(token))
			{
				if ("".equals(token))
				{
					stream.previous();
					stream.remove();
				}
				else
				{
				stream.previous();
				stream.set(token);
				stream.next();
				}
			}
		}
		
		stream.reset();
	}

}
