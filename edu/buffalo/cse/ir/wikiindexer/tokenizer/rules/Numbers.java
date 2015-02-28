package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;


@RuleClass(className = RULENAMES.NUMBERS)
public class Numbers implements TokenizerRule {

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		// TODO Auto-generated method stub
	
		stream.IsPretokenizationState = true;		
		
		String str = stream.TknStreamBldr.toString();
		
//(?<=(\\s|\\.))   (?=(\\s|\\Z|\\.))
		//Pattern numberPattern = Pattern.compile("(?<=(\\D))(\\d+(,\\d{3})*(\\.\\d+){0,1})(\\s){0,1}");
		Pattern numberPattern = Pattern.compile("(\\s?)(\\b\\d+(,\\d{3})*(\\.\\d+){0,1})\\b(\\s?)");
		Matcher numberMatcher = numberPattern.matcher(str); 
		
		StringBuffer tempBuff = new StringBuffer();
		
		while (numberMatcher.find())
		{
		  if ( numberMatcher.group(0).matches("\\d{8} "))
		  {
			  // dont replace it can be a date. .i.e. append as it is 
			  numberMatcher.appendReplacement(tempBuff,numberMatcher.group(0));
			 
		  }
		  else
		  {
			 /*String grp1 = numberMatcher.group(1);
			  String grp2 = numberMatcher.group(2);
			  String grp3 = numberMatcher.group(3);
			  String cha = numberMatcher.group(4);
			  String group0 = numberMatcher.group(0);*/
			  //replacementString = numberMatcher.group(2) + ((" ".equals(numberMatcher.group(5)) && " ".equals(numberMatcher.group(1)) )?" ":"");
			 
			 
			  if ( !" ".equals(numberMatcher.group(5)) && !" ".equals(numberMatcher.group(1)))
			  {
				  numberMatcher.appendReplacement(tempBuff,"");
			  }  
			  else
			  {
				  numberMatcher.appendReplacement(tempBuff," ");
			  }
			  
			  //str = str.replace(replacementString,"");
			  
		  }
		 	
		}
		numberMatcher.appendTail(tempBuff) ;
		
		stream.TknStreamBldr.setLength(0);
		stream.TknStreamBldr.append(tempBuff.toString().trim());
		tempBuff.setLength(0);
	}

}
