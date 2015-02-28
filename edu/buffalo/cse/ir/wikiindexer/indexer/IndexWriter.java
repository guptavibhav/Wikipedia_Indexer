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
import java.util.Properties;
import java.util.TreeMap;
import edu.buffalo.cse.ir.wikiindexer.FileUtil;

/**
 * @author nikhillo
 * This class is used to write an index to the disk
 * 
 */
public class IndexWriter implements Writeable,Serializable {
	
	public INDEXFIELD keyField;
	public INDEXFIELD valueField;
	public boolean isForward;
	public TreeMap<String,TreeMap<Integer,Integer>> termIndx ;
	public TreeMap<String,TreeMap<Integer,Integer>> categoryIndx ;
	public TreeMap<String,TreeMap<Integer,Integer>> autherIndx ;
	public TreeMap<Integer,TreeMap<Integer,Integer>> linkIndx ;
	public int pnum;
	Properties props;
	
	/**
	 * Constructor that assumes the underlying index is inverted
	 * Every index (inverted or forward), has a key field and the value field
	 * The key field is the field on which the postings are aggregated
	 * The value field is the field whose postings we are accumulating
	 * For term index for example:
	 * 	Key: Term (or term id) - referenced by TERM INDEXFIELD
	 * 	Value: Document (or document id) - referenced by LINK INDEXFIELD
	 * @param props: The Properties file
	 * @param keyField: The index field that is the key for this index
	 * @param valueField: The index field that is the value for this index
	 */
	public IndexWriter(Properties props, INDEXFIELD keyField, INDEXFIELD valueField) {
		this(props, keyField, valueField, false);
		
	}
	
	/**
	 * Overloaded constructor that allows specifying the index type as
	 * inverted or forward
	 * Every index (inverted or forward), has a key field and the value field
	 * The key field is the field on which the postings are aggregated
	 * The value field is the field whose postings we are accumulating
	 * For term index for example:
	 * 	Key: Term (or term id) - referenced by TERM INDEXFIELD
	 * 	Value: Document (or document id) - referenced by LINK INDEXFIELD
	 * @param props: The Properties file
	 * @param keyField: The index field that is the key for this index
	 * @param valueField: The index field that is the value for this index
	 * @param isForward: true if the index is a forward index, false if inverted
	 */
	public IndexWriter(Properties props, INDEXFIELD keyField, INDEXFIELD valueField, boolean isForward) {
		//TODO: Implement this method

		this.keyField = keyField;
		this.valueField = valueField;
		this.isForward  = isForward;
		this.props = props;
		switch (keyField) {
		case LINK:
			linkIndx = new TreeMap<Integer, TreeMap<Integer,Integer>>();
			break;

		case CATEGORY:
		    categoryIndx = new TreeMap<String, TreeMap<Integer,Integer>>();	
			break;

		case TERM:
			termIndx = new TreeMap<String, TreeMap<Integer,Integer>>();
			break;
	
		case AUTHOR:
			autherIndx =new TreeMap<String, TreeMap<Integer,Integer>>();
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * Method to make the writer self aware of the current partition it is handling
	 * Applicable only for distributed indexes.
	 * @param pnum: The partition number
	 */
	public void setPartitionNumber(int pnum) {
		//TODO: Optionally implement this method
		this.pnum= pnum;
		
	}
	
	/**
	 * Method to add a given key - value mapping to the index
	 * @param keyId: The id for the key field, pre-converted
	 * @param valueId: The id for the value field, pre-converted
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(int keyId, int valueId, int numOccurances) throws IndexerException {
		//TODO: Implement this method
		
		// only applying for link index. For other index key is string
		if(keyField ==INDEXFIELD.LINK)
		{
			if(linkIndx.containsKey(keyId))
			{
				// get the postings list (i.e.set ) and add it to the set;
				linkIndx.get(keyId).put(valueId,numOccurances);
				
			}
			else
			{
				TreeMap<Integer,Integer> tmp = new TreeMap<Integer,Integer>();
				tmp.put(valueId,numOccurances);
				linkIndx.put(keyId,tmp);
			}
			
		}
				

	}
	
	/**
	 * Method to add a given key - value mapping to the index
	 * @param keyId: The id for the key field, pre-converted
	 * @param value: The value for the value field
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(int keyId, String value, int numOccurances) throws IndexerException {
		//TODO: Implement this method
		
		
	}
	
	/**
	 * Method to add a given key - value mapping to the index
	 * @param key: The key for the key field
	 * @param valueId: The id for the value field, pre-converted
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(String key, int valueId, int numOccurances) throws IndexerException {
		//TODO: Implement this method
		 
		//this method is applicable for term,author,category
		
		if(keyField ==INDEXFIELD.TERM)
		{
			if(termIndx.containsKey(key))
			{
				// get the postings list (i.e.TreeMap<docId,numOccurance> ) and add it to the set;
				termIndx.get(key).put(valueId,numOccurances);
			}
			else
			{
				TreeMap<Integer,Integer> tmp = new TreeMap<Integer,Integer>();
				tmp.put(valueId,numOccurances);
			  	termIndx.put(key,tmp);
			}
			
		}
		
		else if(keyField == INDEXFIELD.AUTHOR)
		{
			
			
				if(autherIndx.containsKey(key))
				{
					// get the postings list (i.e.TreeMap<docId,numOccurance> ) and add it to the set;
					autherIndx.get(key).put(valueId,numOccurances);
				}
				else
				{
					TreeMap<Integer,Integer> tmp = new TreeMap<Integer,Integer>();
					tmp.put(valueId,numOccurances);
					autherIndx.put(key,tmp);
				}
				
			
			
		}
		else if(keyField == INDEXFIELD.CATEGORY)
		{
			
			
				if(categoryIndx.containsKey(key))
				{
					// get the postings list (i.e.TreeMap<docId,numOccurance> ) and add it to the set;
					categoryIndx.get(key).put(valueId,numOccurances);
				}
				else
				{
					TreeMap<Integer,Integer> tmp = new TreeMap<Integer,Integer>();
					tmp.put(valueId,numOccurances);
					categoryIndx.put(key,tmp);
				}
				
			
			
		}
		
		
	}
	
	/**
	 * Method to add a given key - value mapping to the index
	 * @param key: The key for the key field
	 * @param value: The value for the value field
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(String key, String value, int numOccurances) throws IndexerException {
		//TODO: Implement this method
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
				switch(keyField) 
				{
				case LINK:
					FileName = "LinkIndex";
					break;
				case TERM:
					FileName = "TermIndex"+ pnum;
					break;
				case CATEGORY:
					FileName = "CategoryIndex";
					break;
				case AUTHOR:
					FileName = "AuthorIndex";
					break;
				}
				
				// instanciate and create the file if not exists
				File IndexFile = new File(FileUtil.getRootFilesFolder(props)+FileName);
				
				if(!IndexFile.exists()) {
					IndexFile.createNewFile();
				}
				// instantiate the stream for the file 
				fileStream =new FileOutputStream(IndexFile ,false);
				objStream = new  ObjectOutputStream(fileStream);
		
				objStream.writeObject(this);
				
				objStream.flush();
				objStream.close();
				
			} catch (FileNotFoundException e) {
				System.out.println("Index file not found");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Unable to create index file for :" + this.keyField.toString());
			}

	}

	/* (non-Javadoc)
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#cleanUp()
	 */
	public void cleanUp() {
		// TODO Implement this method

	}

}
