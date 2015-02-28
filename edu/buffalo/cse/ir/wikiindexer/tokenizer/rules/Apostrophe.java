package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.util.HashMap;
import java.util.Map;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.APOSTROPHE)
public class Apostrophe implements TokenizerRule {

	public static Map<String, String> expansionMap = new HashMap<String, String>();
	static {
		expansionMap.put("aren't", "are not");
		expansionMap.put("can't", "cannot");
		expansionMap.put("couldn't", "could not");
		expansionMap.put("didn't", "did not");
		expansionMap.put("doesn't", "does not");
		expansionMap.put("don't", "do not");
		expansionMap.put("hadn't", "had not");
		expansionMap.put("hasn't", "has not");
		expansionMap.put("haven't", "have not");
		expansionMap.put("he'd", "he had");// , he would
		expansionMap.put("he'll", "he will");// , he shall
		expansionMap.put("he's", "he is");// , he has
		expansionMap.put("i'd", "i had");// , I would
		expansionMap.put("i'll", "i will");// , I shall
		expansionMap.put("i'm", "i am");
		expansionMap.put("i've", "i have");
		expansionMap.put("isn't", "is not");
		expansionMap.put("it's", "it is");// , it has
		expansionMap.put("let's", "let us");
		expansionMap.put("mustn't", "must not");
		expansionMap.put("shan't", "shall not");
		expansionMap.put("she'd", "she had");// , she would
		expansionMap.put("she'll", "she will");// , she shall
		expansionMap.put("she's", "she is");// , she has
		expansionMap.put("shouldn't", "should not");
		expansionMap.put("that's", "that is");// , that has
		expansionMap.put("there's", "there is");// , there has
		expansionMap.put("they'd", "they had");// , they would
		expansionMap.put("they'll", "they will");// , they shall
		expansionMap.put("they're", "they are");
		expansionMap.put("they've", "they have");
		expansionMap.put("we'd", "we had");// , we would
		expansionMap.put("we're", "we are");
		expansionMap.put("we've", "we have");
		expansionMap.put("weren't", "were not");
		expansionMap.put("what'll", "what will");// , what shall
		expansionMap.put("what're", "what are");
		expansionMap.put("what's", "what is");// , what has
		expansionMap.put("what've", "what have");
		expansionMap.put("where's", "where is");// , where has
		expansionMap.put("who'd", "who had");// , who would
		expansionMap.put("who'll", "who will");// , who shall
		expansionMap.put("who're", "who are");
		expansionMap.put("who's", "who is");// , who has
		expansionMap.put("who've", "who have");
		expansionMap.put("won't", "will not");
		expansionMap.put("wouldn't", "would not");
		expansionMap.put("you'd", "you had");// , you would
		expansionMap.put("you'll", "you will");// , you shall
		expansionMap.put("you're", "you are");
		expansionMap.put("you've", "you have");
		expansionMap.put("should've", "should have");
		expansionMap.put("they'd", "they would");
		expansionMap.put("she'll", "she will");
		expansionMap.put("'em", "them");
		
		
	}

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		// TODO Auto-generated method stub

		stream.reset();
		String token, oldToken;
		while (stream.hasNext()) {
			token = stream.next();
			oldToken = token;
			
			if (expansionMap.containsKey(token.toLowerCase())) 
			{ // expand and split on white space
				
				token = expansionMap.get(token.toLowerCase());
				if(!expansionMap.containsKey(oldToken))
				{
					token = token.substring(0,1).toUpperCase() +token.substring(1,token.length());  
				}
				stream.previous();
				stream.set(token.split(" "));
				if (stream.hasNext()) 
				{
					stream.next();
				} 
				else 
				{
					break;
				}

			} 
			else
			{
				//relpace '
				/*token = token.replaceAll("'s(?=(\\s|\\Z))", "");
				token = token.replaceAll("'(?=(\\s|\\Z))", "");
				token = token.replaceAll("(?<!\\w)'", "");
				token = token.replaceAll("'(?!\\w)", "");
*/				
				/*Pattern p = Pattern.compile("(\\b|\\A)('+s|'(\\b)?+)(?=(\\Z|\\W))");
				Matcher m = p.matcher(token);
				String s;String s1;
				if (m.find())
				{
					 s = m.group(1);
					 s1 = m.group(2);
					 token = token.replaceAll("(\\b|\\A)('+s|'(\\b)?+)(?=(\\Z|\\W))","");
				}*/
				
				token = token.replaceAll("(?<=\\A)'+(?=\\w)","");// apostrophe at the biginning of the word 
				token = token.replaceAll("(?<=\\w)'+(?=\\Z)","");// apostrophe at the end of the word
				token = token.replaceAll("'+s(?=\\b)","");// 's at the end of the word
				token = token.replaceAll("'+(?=\\b\\W)","");// ' the end of the word
				token = token.replaceAll("'+(?=\\W)","");// ' followed by non word char
				
				
			//	token = token.replaceAll("(\\b|\\A)'+s?(?=(\\b|\\Z|\\W))", "");
				

				if (!oldToken.equals(token)) {
					stream.previous();
					stream.set(token);
					stream.next();
				}
			}

		}

		stream.reset();
	}

}
