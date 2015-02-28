package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.PUNCTUATION)
public class Punctuation implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		// TODO Auto-generated method stub
		stream.reset();
		String oldToken ;  
		String token ; 
		while(stream.hasNext())
		{
			
			token= stream.next();
			oldToken = token; 
			
			if( token != null )
			{
				token = token.trim().replaceAll("[!?.]+(?!\\w)", "");
				//token = token.replaceAll("(?<!\\w)[!?.]", "");
				if(!oldToken.equals(token))
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
