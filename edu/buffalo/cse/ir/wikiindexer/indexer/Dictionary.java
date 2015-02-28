/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.buffalo.cse.ir.wikiindexer.FileUtil;

/**
 * @author nikhillo
 * An abstract class that represents a dictionary object for a given index
 */
public abstract class Dictionary implements Writeable,Serializable  {
	
	
	public  INDEXFIELD dictionaryType;
	public  Map<String,Integer> mapping;	
	public Properties props;
	//private  FileOutputStream fileStream;//   new FileOutputStream("/tmp/employee.ser");
	//private  ObjectOutputStream objStream;// = new ObjectOutputStream(fileOut);
	 
	public Dictionary (Properties props, INDEXFIELD field) {
		//TODO Implement this method
		dictionaryType = field;
		mapping = new HashMap<String,Integer>();
		this.props = props;
		
	}
	
	/* (non-Javadoc)
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#writeToDisk()
	 */
	public void writeToDisk() throws IndexerException {
		// TODO Implement this method
		  FileOutputStream fileStream=null;
		  ObjectOutputStream objStream=null;
		  
		  try {
				String FileName="defaultfile"; 
				switch( dictionaryType) 
				{
				case LINK:
					FileName = "DocumentDictionary";
					break;
				case TERM:
					FileName = "TermDictionary";
					break;
				case CATEGORY:
					FileName = "CategoryDictionary";
					break;
				case AUTHOR:
					FileName = "AuthorDictionary";
					break;
				}
				
				// instanciate and create the file if not exists
				File dictionaryFile = new File(FileUtil.getRootFilesFolder(props)+FileName);
				
				if(!dictionaryFile.exists()) {
					dictionaryFile.createNewFile();
				}
				// instantiate the stream for the file 
				fileStream =new FileOutputStream(dictionaryFile ,false);
				objStream = new  ObjectOutputStream(fileStream);
		
				objStream.writeObject(this);
				
				fileStream.close();
				objStream.flush();
				objStream.close();
				
			} catch (FileNotFoundException e) {
				System.out.println("Dictionary file not found");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("unable to create file ");
			}
		  
		  
				

		
		

	}

	/* (non-Javadoc)
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#cleanUp()
	 */
	public void cleanUp() {
		
	}
	
	/**
	 * Method to check if the given value exists in the dictionary or not
	 * Unlike the subclassed lookup methods, it only checks if the value exists
	 * and does not change the underlying data structure
	 * @param value: The value to be looked up
	 * @return true if found, false otherwise
	 */
	public boolean exists(String value) {
		//TODO Implement this method
		if (mapping.containsKey(value))
		return true;
		else 
			return false;
	}
	
	/**
	 * MEthod to lookup a given string from the dictionary.
	 * The query string can be an exact match or have wild cards (* and ?)
	 * Must be implemented ONLY AS A BONUS
	 * @param queryStr: The query string to be searched
	 * @return A collection of ordered strings enumerating all matches if found
	 * null if no match is found
	 */
	public Collection<String> query(String queryStr) {
		//TODO: Implement this method (FOR A BONUS)
		
		Pattern  p = Pattern.compile("^([^*?]*)([*?]?)([^*?]*)$");
		Matcher m = p.matcher(queryStr);
		if(m.find())
		{
			//query contains wildcard
			String left =  m.group(1) == null ? "":m.group(1);
			String right =  m.group(3) == null ? "":m.group(3);
			String regex = null;
			if (m.group(2).equals("?"))
			{
				regex =  "\\Q" +left+ "\\E"+ "[\\s\\S]" +"\\Q"+ right + "\\E";
			}
			else if (m.group(2).equals("*"))
			{
				regex =  "\\Q" +left+ "\\E"+ "[\\s\\S]*" +"\\Q"+ right + "\\E";
			}
			else {
				regex = "\\Q" +m.group(0)+"\\E";
			}
		
			HashSet<String> res = new HashSet<String>();
			for (String s : mapping.keySet())
			{
				if (s.matches(regex))
					res.add(s);
			}
			if (res.size()> 0 )
				return res;
			else return null;
		}
		else
		{
			// no wildcard 
			return null;
		}
		

		
	}
	
	/**
	 * Method to get the total number of terms in the dictionary
	 * @return The size of the dictionary
	 */
	public int getTotalTerms() {
		//TODO: Implement this method
		return mapping.size();
	}
}
