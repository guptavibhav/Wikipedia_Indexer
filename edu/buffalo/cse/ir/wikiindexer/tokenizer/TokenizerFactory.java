/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer;

import java.util.Properties;

import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.Accent;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.Apostrophe;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.Capitalization;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.Dates;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.EnglishStemmer;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.Finalize;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.Hyphen;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.Numbers;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.Punctuation;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.SpecialCharacters;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.Stopwords;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.Whitespace;

/**
 * Factory class to instantiate a Tokenizer instance
 * The expectation is that you need to decide which rules to apply for which field
 * Thus, given a field type, initialize the applicable rules and create the tokenizer
 * @author nikhillo
 *
 */
public class TokenizerFactory {
	//private instance, we just want one factory
	private static TokenizerFactory factory;
	
	//properties file, if you want to read soemthing for the tokenizers
	private static Properties props;
	
	/**
	 * Private constructor, singleton
	 */
	private TokenizerFactory() {
		//TODO: Implement this method
	}
	
	/**
	 * MEthod to get an instance of the factory class
	 * @return The factory instance
	 */
	public static TokenizerFactory getInstance(Properties idxProps) {
		if (factory == null) {
			factory = new TokenizerFactory();
			props = idxProps;
		}
		
		return factory;
	}
	
	/**
	 * Method to get a fully initialized tokenizer for a given field type
	 * @param field: The field for which to instantiate tokenizer
	 * @return The fully initialized tokenizer
	 */
	public Tokenizer getTokenizer(INDEXFIELD field) {
		//TODO: Implement this method
		/*
		 * For example, for field F1 I want to apply rules R1, R3 and R5
		 * For F2, the rules are R1, R2, R3, R4 and R5 both in order
		 * So the pseudo-code will be like:
		 * if (field == F1)
		 * 		return new Tokenizer(new R1(), new R3(), new R5())
		 * else if (field == F2)
		 * 		return new TOkenizer(new R1(), new R2(), new R3(), new R4(), new R5())
		 * ... etc
		 */
		try {
		if (field== INDEXFIELD.AUTHOR)
			return new Tokenizer(new Accent(),new Capitalization(),new Whitespace(),new Finalize());
		else if (field== INDEXFIELD.CATEGORY)
			return new Tokenizer(new Accent(),new Dates(), new Capitalization(),new Stopwords(),new Whitespace(),new Punctuation(),new Hyphen(),new EnglishStemmer(),new Finalize());
		else if (field== INDEXFIELD.LINK)
			return new Tokenizer(new Accent(),new Finalize()); // add delim , hyphen ?,  
		else if (field== INDEXFIELD.TERM)
			return new Tokenizer(new Accent(),new Dates(),new Numbers(), new SpecialCharacters(),new Capitalization(),new Stopwords(),new Whitespace(),new Punctuation(),new Apostrophe(),new Hyphen(),new EnglishStemmer(),new Finalize());
		}
		catch (TokenizerException e )
		{
			
		}
		return null;
		
	}
}
