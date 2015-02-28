/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.Tokenizer;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument.Section;

/**
 * A Callable document transformer that converts the given WikipediaDocument
 * object into an IndexableDocument object using the given Tokenizer
 * 
 * @author nikhillo
 * 
 */
public class DocumentTransformer implements Callable<IndexableDocument> {
	/**
	 * Default constructor, DO NOT change
	 * 
	 * @param tknizerMap
	 *            : A map mapping a fully initialized tokenizer to a given field
	 *            type
	 * @param doc
	 *            : The WikipediaDocument to be processed
	 */

	public Tokenizer AuthTokenizer;
	public Tokenizer CategoryTokenizer;
	public Tokenizer LinkTokenizer;
	public Tokenizer TermTokenizer;
	public WikipediaDocument document;

	public DocumentTransformer(Map<INDEXFIELD, Tokenizer> tknizerMap,
			WikipediaDocument doc) {

		// TODO: Implement this method

		AuthTokenizer = tknizerMap.get(INDEXFIELD.AUTHOR);
		CategoryTokenizer = tknizerMap.get(INDEXFIELD.CATEGORY);
		LinkTokenizer = tknizerMap.get(INDEXFIELD.LINK);
		TermTokenizer = tknizerMap.get(INDEXFIELD.TERM);
		document = doc;
	}

	/**
	 * Method to trigger the transformation
	 * 
	 * @throws TokenizerException
	 *             Inc ase any tokenization error occurs
	 */
	
	
	public IndexableDocument call() throws TokenizerException {
		// TODO Implement this method
		
		
		try {
					
			/************************************/
			
			IndexableDocument indxDoc = new IndexableDocument();
			indxDoc.setIdentifier(String.valueOf(document.getTitle()));
			StringBuilder bldr = new StringBuilder();
			TokenStream autherStream = null;
			TokenStream categoryStream = null;
			TokenStream linkStream = null;
			TokenStream termStream = null;
			
			
			// Author Tokenization**********************************************

			if (document.getAuthor() != null && !"".equals(document.getAuthor())) {
				autherStream = new TokenStream(document.getAuthor());
		
				AuthTokenizer.tokenize(autherStream);
		
		
			}

			indxDoc.addField(INDEXFIELD.AUTHOR, autherStream); // null auther stream
																// moght get added

			// Category tokenization**********************************************

			// get first non empty non null category (To avoid sending first token
			// as empty. This will fail entire append operation)
			int i = 0;
			List<String> catList = document.getCategories();
			
			for(i =0 ; i < catList.size();i++ )
			{
				if(catList.get(i) == null || "".equals(catList.get(i)))
					//do nothing
					;	
				else 
					break;
			}
			
			if (i < catList.size() ) // at least one non empty element 
			{
			// initiate the stream using one token
			categoryStream = new TokenStream(catList.get(i));

			// Append remaining Categories as next tokens
			for (i++; i < catList.size(); i++)
				categoryStream.append(catList.get(i));

			// Tokenize the stream
			
			CategoryTokenizer.tokenize(categoryStream);
			
			
			}
			// add the stream to indexable document
			indxDoc.addField(INDEXFIELD.CATEGORY, categoryStream);

			// Link tokenization*************************************************

			// get first non empty non null category (To avoid sending first token
			// as empty. This will fail entire append operation)
			i = 0;
			
			List<String> lnkList = new ArrayList<String>();  
				
			for (String str : document.getLinks() )
			{
				lnkList.add(str);
			}
			
			 for (i =0 ; i< lnkList.size();i++) 
			 {
				 if( lnkList.get(i) == null || "".equals(lnkList.get(i)))
						 //do nothing
						 ;
				 else 
					 break;
			 }
			 
			if (i<lnkList.size()) // at least one non empty token
			{
			
			// initiate the stream using one token
			 linkStream = new TokenStream(lnkList.get(i));
			 LinkTokenizer.tokenize(linkStream);
			// Append remaining links as next tokens
			for (i++; i < lnkList.size(); i++)
				{
					TokenStream tempStream = new TokenStream(lnkList.get(i));
					//tokenize individual stream 
					LinkTokenizer.tokenize(tempStream);
					// merge with final 
					linkStream.merge(tempStream);
				}
			
			}
			
			
			// add the stream to indexable document
			indxDoc.addField(INDEXFIELD.LINK, linkStream);
			
			// Term tokenization*************************************************

			// get first non empty non null category (To avoid sending first token
			// as empty. This will fail entire append operation)
			i = 0;
			Collection<Section> sectionCollection = document.getSections();
			
			// Reset the string builder 
			
				bldr.setLength(0);
			
			// for each section append  the text to the string builder  
			
				bldr.append(document.getTitle()+ " ");
				bldr.append(document.getPublishDate()+ " ");
				for(Section s : sectionCollection)
				{
				  bldr.append( (s.getTitle().equals("Default")? "" : (s.getTitle()+" ")) );
				  bldr.append( s.getText());
				}
				
			// initiate the termStream using the string builder 	
				
				if(bldr.length()> 0)
					termStream = new TokenStream(bldr);		
				
				
			// Tokenize the stream
			if(termStream != null)
			{
		
			TermTokenizer.tokenize(termStream);
		
			}

			// add the stream to indexable document
			indxDoc.addField(INDEXFIELD.TERM, termStream);

			
			// Term
		
			return indxDoc;
		}catch(Exception e )
		{
			
		}
		return null;
			/************************************/	
			}

}
