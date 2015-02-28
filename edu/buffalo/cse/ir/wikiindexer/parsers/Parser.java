/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaParser;

/**
 * @author nikhillo
 * 
 */
public class Parser {
	/* */
	private final Properties props;
	private final Collection<WikipediaDocument> rawDocsCollection;
	/**
	 * 
	 * @param idxConfig
	 * @param parser
	 */
	public Parser(Properties idxProps) {
		props = idxProps;	
		rawDocsCollection =  new ArrayList<WikipediaDocument>(); 
	}

	
	/* TODO: Implement this method */
	/**
	 * 
	 * @param filename
	 * @param docs
	 */
	/**
	 * @param filename
	 * @param docs
	 */
	public void parse(String filename, Collection<WikipediaDocument> docs) {

		
		
			try {

				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				

				DefaultHandler handler = new DefaultHandler() {

					int Wikiid;
					String Wikititle;
					String Wikitimestamp;
					String Wikiauther;
					String Wikitext;
					private final StringBuilder textBuilder = new StringBuilder(
							64);

					boolean bpage = false;
					boolean btitle = false;
					boolean bid = false;
					boolean btimestamp = false;
					boolean btext = false;
					boolean brevision = false;
					boolean busername = false;
					boolean bip = false;

					public void startElement(String uri, String localName,
							String qName, Attributes attributes)

					throws SAXException {

						

						if (qName.equalsIgnoreCase("page")) {
						
							// Set all variable to empty value
							Wikiid = -1;
							Wikititle = null;
							Wikitimestamp = null;
							Wikiauther = null;
							Wikitext = null;

							bpage = true;

							textBuilder.setLength(0);
						}
						if (qName.equalsIgnoreCase("title")) {
							btitle = true;
							textBuilder.setLength(0);
						}

						if (qName.equalsIgnoreCase("id") && !brevision) {
							bid = true;
							textBuilder.setLength(0);
						}

						if (qName.equalsIgnoreCase("revision")) {
							brevision = true;
							textBuilder.setLength(0);
						}

						if (qName.equalsIgnoreCase("timestamp")) {
							btimestamp = true;
							textBuilder.setLength(0);
						}

						if (qName.equalsIgnoreCase("text")) {
							btext = true;
							textBuilder.setLength(0);
						}
						if (qName.equalsIgnoreCase("contributor")) {
							textBuilder.setLength(0);
						}

						if (qName.equalsIgnoreCase("username")) {
							busername = true;
							textBuilder.setLength(0);
						}
						if (qName.equalsIgnoreCase("ip")) {
							bip = true;
							textBuilder.setLength(0);
						}

					}

					public void endElement(String uri, String localName,
							String qName) throws SAXException {

						if (qName.equalsIgnoreCase("page")) {
						}

						if (qName.equalsIgnoreCase("title")) {
							Wikititle = textBuilder.toString().trim();
							btitle = false;
							textBuilder.setLength(0);
						}

						if (qName.equalsIgnoreCase("id") && !brevision) {
							Wikiid = Integer.parseInt(textBuilder.toString()
									.trim());
							bid = false;
							textBuilder.setLength(0);
						}

						if (qName.equalsIgnoreCase("revision")) {
							brevision = false;
							textBuilder.setLength(0);
						}

						if (qName.equalsIgnoreCase("timestamp")) {
							Wikitimestamp = textBuilder.toString().trim();
							btimestamp = false;
							textBuilder.setLength(0);
						}

						if (qName.equalsIgnoreCase("username")) {

							Wikiauther = textBuilder.toString().trim();
							busername = false;
							textBuilder.setLength(0);
						}
						if (qName.equalsIgnoreCase("ip")) {
							Wikiauther = textBuilder.toString().trim();
							bip = false;
							textBuilder.setLength(0);
						}

						if (qName.equalsIgnoreCase("text")) {
							// create a wiki doc
							try {

								WikipediaDocument doc = new WikipediaDocument(
										Wikiid, Wikitimestamp, Wikiauther,
										Wikititle);

								// get the page text
								Wikitext = textBuilder.toString().trim();
								textBuilder.setLength(0);
															
								doc.setText(Wikitext);
								rawDocsCollection.add(doc);								
	
	
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							btext = false;
						}

					}

					public void characters(char ch[], int start, int length)
							throws SAXException {

						if (btitle) {
							textBuilder.append(new String(ch, start, length));

						}

						if (bid && !brevision) {
							textBuilder.append(new String(ch, start, length));

						}

						if (btimestamp) {
							textBuilder.append(new String(ch, start, length));

						}

						if (btext) {
							textBuilder.append(new String(ch, start, length));

						}

						if (busername) {
							textBuilder.append(new String(ch, start, length));

						}
						if (bip) {
							textBuilder.append(new String(ch, start, length));
						}
					}
				};
				
				try 
				{	
				File file = new File(filename);
				InputStream inputStream = new FileInputStream(file);
				Reader reader = new InputStreamReader(inputStream, "UTF-8");

				InputSource is = new InputSource(reader);
				is.setEncoding("UTF-8");

				saxParser.parse(is, handler);
				
				for (WikipediaDocument temp : rawDocsCollection )
				{
				   WikipediaDocument temp1 = WikipediaParser.createWikiDoc(temp); 	
				   add(temp1,docs);
				}

			
			    }
				catch(Exception e) // should it be  specific file not found exception?
				{
				 return;	
				}
							
			} catch (Exception e) {
				e.printStackTrace();
			}
			 
	}

	/**
	 * Method to add the given document to the collection. PLEASE USE THIS
	 * METHOD TO POPULATE THE COLLECTION AS YOU PARSE DOCUMENTS For better
	 * performance, add the document to the collection only after you have
	 * completely populated it, i.e., parsing is complete for that document.
	 * 
	 * @param doc
	 *            : The WikipediaDocument to be added
	 * @param documents
	 *            : The collection of WikipediaDocuments to be added to
	 */
	private synchronized void add(WikipediaDocument doc,
			Collection<WikipediaDocument> documents) {
		documents.add(doc);
	}
}
