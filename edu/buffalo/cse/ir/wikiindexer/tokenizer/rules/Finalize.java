package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.FINALIZE)
public class Finalize implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		// TODO Auto-generated method stub

		stream.reset();
		stream.setValidState();
		if(stream.IsPretokenizationState == true )
		{
			stream.reset();
			stream.set(stream.TknStreamBldr.toString());
		}
		stream.IsPretokenizationState = false;
	}

}
