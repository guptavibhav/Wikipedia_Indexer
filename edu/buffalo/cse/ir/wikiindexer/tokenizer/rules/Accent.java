package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

import java.text.Normalizer;

@RuleClass(className = RULENAMES.ACCENTS)
public class Accent implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		// TODO Auto-generated method stub

		stream.IsPretokenizationState = true;
		String str = null;//=  stream.TknStreamBldr.toString(); 
		//str =Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]+", "").toCharArray() ;
	
		str = Normalizer.normalize(stream.TknStreamBldr.toString(), Normalizer.Form.NFD);
	/*	char[] arr = str.toCharArray();
		int aAsci = 'a';
		 int[] integers = new int[1000];  
		 int i = 0;
		 for ( char a : arr)
		 {
			 
			 integers[i]  = ((int)a) ;
			 i++;
		 }*/
		
		 
		//str = str.replaceAll("[^\\p{ASCII}]+", "") ;
		str = str.replaceAll("[\\p{InCombiningDiacriticalMarks}]+", "") ;
		
		
		stream.TknStreamBldr.setLength(0);
		stream.TknStreamBldr.append(str);
		
		
	}

}
