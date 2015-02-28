package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;

public class Delim implements TokenizerRule {
	
	public String delimiter; 
	public Delim()
	{
		delimiter  = ",";
		
	}
	
	public Delim(String s)
	{
		delimiter = s;//check for single char
	}

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		// TODO Auto-generated method stub
		
		// TODO Auto-generated method stub
		
		String str = stream.TknStreamBldr.toString().trim();
		//stream.set(str.split("(?!\\b[A-Z]\\w*\\b)\\s(?!\\b[A-Z]\\w*\\b)"));
		//stream.set(str.split("\\s+(?!\\b[A-Z]\\w*\\b)|(?<!\\b[A-Z]\\w*\\b)\\s+"));
		
		stream  = new TokenStream("InitToken");
		stream.setValidState();
		stream.set(str.split(""+delimiter+"+"));
		stream.IsPretokenizationState = false;
		
		stream.reset();
		while(stream.hasNext())
		{
			String token1 = stream.next();
			String token2 = null;
			if(stream.hasNext())
			token2 = stream.next();
			else 
				break;
			
			
			if(token1.matches("[A-Z0-9]*") || token2.matches("[A-Z0-9]*"));
			else
			{
			if(token1.matches("[A-Z].*") && token2.matches("[A-Z].*"))
			{
				stream.previous();
				stream.mergeWithPrevious();
				// merge both the tokens 
			}
			else
			{
				stream.previous();
				
			}
			}
			
		}
		stream.reset();
		

	}

}
