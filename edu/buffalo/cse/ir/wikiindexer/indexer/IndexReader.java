/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.TreeSet;



/*import com.sun.java.util.jar.pack.Instruction.Switch;
import com.sun.xml.internal.fastinfoset.util.StringArray;*/

import edu.buffalo.cse.ir.wikiindexer.FileUtil;

/**
 * @author nikhillo This class is used to introspect a given index The
 *         expectation is the class should be able to read the index and all
 *         associated dictionaries.
 */
public class IndexReader {
	/**
	 * Constructor to create an instance
	 * 
	 * @param props
	 *            : The properties file
	 * @param field
	 *            : The index field whose index is to be read
	 * 
	 */
	public INDEXFIELD indexType;
	public SharedDictionary docDictionary;
	public TreeMap<Integer, String> InvertedDocDictionary;
	public String directoryPath;
	TreeMap<String, TreeMap<Integer, Integer>> stringIndx;
	TreeMap<Integer, TreeMap<Integer,Integer>> intIndx;
	ArrayList<TreeMap<String, TreeMap<Integer, Integer>>> termIndxlist;
	TreeMap<Integer, TreeSet<Integer>> indxPostingsCount_int; 
	TreeMap<Integer, TreeSet<String>> indxPostingsCount_str;

	
	public IndexReader(Properties props, INDEXFIELD field) {
		// TODO: Implement this method
		indexType = field;
		directoryPath = FileUtil.getRootFilesFolder(props);
		termIndxlist = new ArrayList<TreeMap<String, TreeMap<Integer, Integer>>>();
		// get the documentDictionary as a shared dictionary
		docDictionary = (SharedDictionary) getDeserialiObject(directoryPath
				+ "DocumentDictionary");

		if (docDictionary == null) {
			System.out.println("Unable to read document dictionary.");
		} else {
			// create a inverted document dictionary
			InvertedDocDictionary = new TreeMap<Integer, String>();
			for (Map.Entry<String, Integer> entry : docDictionary.mapping
					.entrySet()) {
				InvertedDocDictionary.put(entry.getValue(), entry.getKey());
			}

			File[] files = new File(directoryPath).listFiles();
			String linkIndexFile = "", authorIndexFile = "", categoryIndeFile = "";
			List<String> termIndexFiles = new ArrayList<String>();

			for (File f : files) {
				if (f.isFile()) {

					if (f.getName().toLowerCase().contains("author"))
						authorIndexFile = f.getName();
					else if (f.getName().toLowerCase().contains("term"))
						termIndexFiles.add(f.getName());
					else if (f.getName().toLowerCase().contains("category"))
						categoryIndeFile = f.getName();
					else if (f.getName().toLowerCase().contains("link"))
						linkIndexFile = f.getName();

				}
			}
			
			IndexWriter temp = null;
			switch (field) {
			case LINK:
				// its a link index i.e. DocId -> postings list
				temp = (IndexWriter) getDeserialiObject(directoryPath
						+ linkIndexFile);
				
				if (temp != null && temp.keyField == INDEXFIELD.LINK)
				{
					intIndx = temp.linkIndx;
					
					indxPostingsCount_int = new TreeMap<Integer, TreeSet<Integer>>();
					for( Map.Entry<Integer,TreeMap<Integer,Integer>>  entry : intIndx.entrySet() )
					{
						int countkey  = entry.getValue().size();  
						if(indxPostingsCount_int.containsKey(countkey))
						{
							indxPostingsCount_int.get(countkey).add(entry.getKey());
						}
						else
						{
							TreeSet<Integer> tmpSet = new TreeSet<Integer>();
							// add doc Id in the tree set
							tmpSet.add(entry.getKey());
							// add the tree set to the count index 
							indxPostingsCount_int.put(countkey, tmpSet);
							
						}
					}
					
				}

				break;
			case TERM:

				// its a term Index . term - > postings list
				
				for (String str : termIndexFiles) {
					termIndxlist.add(null);
				}
				indxPostingsCount_str = new TreeMap<Integer, TreeSet<String>>();
				
				for (String str : termIndexFiles) {
					temp = (IndexWriter) getDeserialiObject(directoryPath + str);

					if (temp != null && temp.keyField == INDEXFIELD.TERM) 
					{
						termIndxlist.set(temp.pnum,temp.termIndx);
						for( Map.Entry<String,TreeMap<Integer,Integer>>  entry : temp.termIndx.entrySet() )
						{
							int countkey  = entry.getValue().size();  
							if(indxPostingsCount_str.containsKey(countkey))
							{
								indxPostingsCount_str.get(countkey).add(entry.getKey());
							}
							else
							{
								TreeSet<String> tmpSet = new TreeSet<String>();
								// add doc Id in the tree set
								tmpSet.add(entry.getKey());
								// add the tree set to the count index 
								indxPostingsCount_str.put(countkey, tmpSet);
							}
						}
					}
				}

				break;
			case CATEGORY:

				indxPostingsCount_str = new TreeMap<Integer, TreeSet<String>>();
				
				temp = (IndexWriter) getDeserialiObject(directoryPath
						+ categoryIndeFile);
				if (temp != null && temp.keyField == INDEXFIELD.CATEGORY) {
					stringIndx = temp.categoryIndx;
					for( Map.Entry<String,TreeMap<Integer,Integer>>  entry : stringIndx.entrySet() )
					{
						int countkey  = entry.getValue().size();  
						if(indxPostingsCount_str.containsKey(countkey))
						{
							indxPostingsCount_str.get(countkey).add(entry.getKey());
						}
						else
						{
							TreeSet<String> tmpSet = new TreeSet<String>();
							// add doc Id in the tree set
							tmpSet.add(entry.getKey());
							// add the tree set to the count index 
							indxPostingsCount_str.put(countkey, tmpSet);
							
						}
					}
					
				}

				// its a category index . categoty -> Postings list

				break;
			case AUTHOR:
				indxPostingsCount_str = new TreeMap<Integer, TreeSet<String>>();
				// its a author index. Author -> postings list
				temp = (IndexWriter) getDeserialiObject(directoryPath
						+ authorIndexFile);
				if (temp != null && temp.keyField == INDEXFIELD.AUTHOR) {
					stringIndx = temp.autherIndx;
				
					for( Map.Entry<String,TreeMap<Integer,Integer>>  entry : stringIndx.entrySet() )
					{
						int countkey  = entry.getValue().size();  
						if(indxPostingsCount_str.containsKey(countkey))
						{
							indxPostingsCount_str.get(countkey).add(entry.getKey());
						}
						else
						{
							TreeSet<String> tmpSet = new TreeSet<String>();
							// add doc Id in the tree set
							tmpSet.add(entry.getKey());
							// add the tree set to the count index 
							indxPostingsCount_str.put(countkey, tmpSet);
							
						}
					}
				}
				break;
			}
		}
	}

	/**
	 * Method to get the total number of terms in the key dictionary
	 * 
	 * @return The total number of terms as above
	 */
	private Object getDeserialiObject(String fileName) {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		Object deserializedObj = null;
		try {
			fis = new FileInputStream(fileName);
			in = new ObjectInputStream(fis);
			deserializedObj = in.readObject();
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			deserializedObj = null;
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			deserializedObj = null;
		}

		return deserializedObj;
	}

	public int getTotalKeyTerms() {
		// TODO: Implement this method
		switch (indexType) {

		case AUTHOR:
			if (stringIndx != null) {
				return stringIndx.size();
			}
			break;

		case CATEGORY:
			if (stringIndx != null) {
				return stringIndx.size();
			}
			break;

		case LINK:
			if (intIndx != null) {
				return intIndx.size();
			}
			break;

		case TERM:

			if (termIndxlist != null) {
				int size = 0;
				for (TreeMap<String, TreeMap<Integer, Integer>> mp : termIndxlist)
					size = size + mp.size();
				return size;
			}

			break;
		}

		return -1;

	}

	/**
	 * Method to get the total number of terms in the value dictionary
	 * 
	 * @return The total number of terms as above
	 */
	public int getTotalValueTerms() {
		// TODO: Implement this method
		// need to check on forun
		return docDictionary.getTotalTerms();

	}

	/**
	 * Method to retrieve the postings list for a given dictionary term
	 * 
	 * @param key
	 *            : The dictionary term to be queried
	 * @return The postings list with the value term as the key and the number
	 *         of occurrences as value. An ordering is not expected on the map
	 */
	public Map<String, Integer> getPostings(String key) {

		// TODO: Implement this method
		switch (indexType) {

		case AUTHOR:
			if (stringIndx != null) 
			{
				if (stringIndx.containsKey(key)) 
				{
					// get postings of the key in the form  of Map <doc Id ,frequency> 
					TreeMap<Integer, Integer> postings_int = stringIndx.get(key);

					// Convert to  Map <docIdentifier ,frequency>
					TreeMap<String, Integer> postings_str = new TreeMap<String, Integer>();
					String docIdentifier = "OrphanDoc";
					for (Map.Entry<Integer, Integer> entry : postings_int
							.entrySet()) {
						docIdentifier = InvertedDocDictionary.get(entry
								.getKey());
						postings_str.put(docIdentifier, entry.getValue());

					}
					
					return postings_str;

				}
				else
				{
					// key not found in the postings
					return null;
				}
			}

		break;

		case CATEGORY:
			if (stringIndx != null) 
			{

				if (stringIndx.containsKey(key)) 
				{
					// get postings of the key in the form  of Map <doc Id ,frequency> 
					TreeMap<Integer, Integer> postings_int = stringIndx.get(key);

					// Convert to  Map <docIdentifier ,frequency>
					TreeMap<String, Integer> postings_str = new TreeMap<String, Integer>();
					String docIdentifier = "OrphanDoc";
					for (Map.Entry<Integer, Integer> entry : postings_int
							.entrySet()) {
						docIdentifier = InvertedDocDictionary.get(entry
								.getKey());
						postings_str.put(docIdentifier, entry.getValue());

					}
					
					return postings_str;

				}
				else
				{
					// key not found in the postings
					return null;
				}

			}
			break;

		case LINK:
			
			if (intIndx != null && docDictionary.mapping.containsKey(key))  
			{
				int DocId = docDictionary.mapping.get(key);
  
				if (intIndx.containsKey(DocId)) 
				{
					// get postings of the key in the form  of Map <doc Id ,frequency> 
					TreeMap<Integer, Integer> postings_int = intIndx.get(DocId);

					// Convert to  Map <docIdentifier ,frequency>
					TreeMap<String, Integer> postings_str = new TreeMap<String, Integer>();
					String docIdentifier = "OrphanDoc";
					for (Map.Entry<Integer, Integer> entry : postings_int
							.entrySet()) {
						docIdentifier = InvertedDocDictionary.get(entry
								.getKey());
						postings_str.put(docIdentifier, entry.getValue());

					}
					
					return postings_str;

				}
				else
				{
					// key not found in the postings
					return null;
				}

			}
			
			break;

		case TERM:

			if (termIndxlist != null) 
			{
				int pnum = Partitioner.getPartitionNumber(key);
				
				if (pnum <termIndxlist.size())
				{
					TreeMap<String, TreeMap<Integer,Integer>> tmp = termIndxlist.get(pnum);
					if( tmp.containsKey(key))
					{
						TreeMap<Integer, Integer> postings_int =tmp.get(key);

						// Convert to  Map <docIdentifier ,frequency>
						TreeMap<String, Integer> postings_str = new TreeMap<String, Integer>();
						String docIdentifier = "OrphanDoc";
						for (Map.Entry<Integer, Integer> entry : postings_int.entrySet())
						{
							docIdentifier = InvertedDocDictionary.get(entry
									.getKey());
							postings_str.put(docIdentifier, entry.getValue());

						}
						
						return postings_str;


					}
					else
					{
						// key not fount in the corresponding partitioned index 
						return null; 
					}
					
				}
				else
				{
					// probably incorrect term index collection 
					return null;
				}
				
			}

			break;
		}

		return null;

	}

	/**
	 * Method to get the top k key terms from the given index The top here
	 * refers to the largest size of postings.
	 * 
	 * @param k
	 *            : The number of postings list requested
	 * @return An ordered collection of dictionary terms that satisfy the
	 *         requirement If k is more than the total size of the index, return
	 *         the full index and don't pad the collection. Return null in case
	 *         of an error or invalid inputs
	 */
	public Collection<String> getTopK(int k) {
		
		if ( k <= 0 )
		{
			return null;
		}
		Collection<String> topElements = null; 
		try
		{
		
		int topCount = k ;	
		switch (indexType) 
		{
			case LINK:
			 topElements = new LinkedHashSet<String>();
			 
			 if( k >= intIndx.size() )
			 {
				 // to handle very large value of k 
				 topCount = intIndx.size();
			 }
			for( int i :  indxPostingsCount_int.descendingKeySet())
			{
				for( int j : indxPostingsCount_int.get(i)) 
				{
					topElements.add(InvertedDocDictionary.get(j));
					topCount--; 
					if (topCount <=0 )
						break;
				}
				if (topCount <=0 )
					break;
				
			}
			
		break;
			case TERM:
				topCount = k ;
				int termindexSize=0;
				for (Map.Entry<Integer, TreeSet<String>> entry: indxPostingsCount_str.entrySet()  )
				{
					termindexSize += entry.getValue().size();
				}
				
				if( k >= termindexSize )
				 {
					 // to handle very large value of k 
					 topCount = termindexSize;
				 }
				topElements = new LinkedHashSet<String>();
				for( int i :  indxPostingsCount_str.descendingKeySet())
				{
					for( String j : indxPostingsCount_str.get(i)) 
					{
						topElements.add(j);
						topCount--; 
						if (topCount <=0 )
							break;
					}
					if (topCount <=0 )
						break;
				}
				
				
		break;
		default:
			
			topCount = k ;
			if( k >= stringIndx.size() )
			 {
				 // to handle very large value of k 
				 topCount = stringIndx.size();
			 }
			topElements = new LinkedHashSet<String>();
			for( int i :  indxPostingsCount_str.descendingKeySet())
			{
				for( String j : indxPostingsCount_str.get(i)) 
				{
					topElements.add(j);
					topCount--; 
					if (topCount <=0 )
						break;
				}
				if (topCount <=0 )
					break;
			}
		break;
		}
		
		}
		catch(Exception e)
		{
			topElements = null;
		}
		return topElements;
	}

	/**
	 * Method to execute a boolean AND query on the index
	 * 
	 * @param terms
	 *            The terms to be queried on
	 * @return An ordered map containing the results of the query The key is the
	 *         value field of the dictionary and the value is the sum of
	 *         occurrences across the different postings. The value with the
	 *         highest cumulative count should be the first entry in the map.
	 */
	public Map<String, Integer> query(String... terms) {
		// TODO: Implement this method (FOR A BONUS)
		
		// check no of elements in term  
		
		try
		{
		if ( terms != null && terms.length >= 0)
		{
			
			//int postingsSize; 
			HashMap<String,TreeMap<Integer, Integer>> termpostingCollecction  = new HashMap<String, TreeMap<Integer,Integer>>();
			
			switch(indexType)
			{
			case TERM:
				// get term to posting counts order
				
//				TreeMap<Integer>
				// get a postings collection
				for (String term : terms )
				{
					int pnum = Partitioner.getPartitionNumber(term);
					
					if (pnum <termIndxlist.size())
					{
						TreeMap<String, TreeMap<Integer,Integer>> tmp = termIndxlist.get(pnum);
						if( tmp.containsKey(term))
						{
							termpostingCollecction.put(term, tmp.get(term));
						}
						else
						{
							// key not fount in the corresponding partitioned index
							return null; // AND result will be nnull even if one entry is not found 
							
						}		
					}
					else
					{
						// probably incorrect term index collection 
					
					}
	 
				}
			
				break;
				
				case LINK:
					for (String term : terms )
					{
							if(intIndx.containsKey(docDictionary.mapping.get(term)))
							{
								termpostingCollecction.put(term,intIndx.get(docDictionary.mapping.get(term)));
							}
							else
							{
								return null; // AND result will be nnull even if one entry is not found
							}
					}
					break;
				case CATEGORY: 
					for (String term : terms )
					{
							if(stringIndx.containsKey(term))
							{
								termpostingCollecction.put(term,stringIndx.get(term));
							}
							else
							{
								return null; // AND result will be nnull even if one entry is not found
							}
					}
					break;
				case AUTHOR:
					for (String term : terms )
					{
							if(stringIndx.containsKey(term))
							{
								termpostingCollecction.put(term,stringIndx.get(term));
							}
							else
							{
								return null; // AND result will be nnull even if one entry is not found
							}
					}
					break;
			}
				// get count of all the terms  
				
				HashSet<Integer> commonDocs = null ;  
		//		HashMap<Integer,Integer> CumulativeDocs = new  HashMap<Integer, Integer>();
				for ( Map.Entry<String,TreeMap<Integer,Integer>> entry :  termpostingCollecction.entrySet() ) 
				{
					// get common docs collection 
					if(commonDocs == null)
					{
						commonDocs =  new HashSet<Integer>();
						commonDocs.addAll(entry.getValue().keySet());
					}
					else 
					{
						commonDocs.retainAll(entry.getValue().keySet());
					}
				// update cumulative docs map
				//	CumulativeDocs.containsKey(key)
				}
				TreeMap<Integer,TreeSet<Integer>> InvertfinalRes = new TreeMap<Integer, TreeSet<Integer>>();
				
				int cumulativeCount = 0 ; 
				for( int tempDocId : commonDocs  )
				{	
					cumulativeCount = 0;
					for ( Map.Entry<String,TreeMap<Integer,Integer>> entry :  termpostingCollecction.entrySet() )
					{
						// get the cumulativecount
						
						cumulativeCount += entry.getValue().get(tempDocId);
					}
					if(InvertfinalRes.containsKey(cumulativeCount))
						{ 
						
							InvertfinalRes.get(cumulativeCount).add(tempDocId);
						}
					else 
						{
							TreeSet<Integer> tempTreeSet =  new TreeSet<Integer>();
							tempTreeSet.add(tempDocId);
							InvertfinalRes.put(cumulativeCount ,tempTreeSet);
						}
				}
				
				// get dosc in descendng order of sumulative count, lookup the doc name and return the collection 
				
				LinkedHashMap<String, Integer> res = new LinkedHashMap<String, Integer>();
				
				for (  Map.Entry<Integer, TreeSet<Integer>> entry : InvertfinalRes.descendingMap().entrySet())
				{
					for ( int i : entry.getValue())
					{
						res.put( InvertedDocDictionary.get(i) ,entry.getKey());
					}
					
				}
				
				return res;

				
				}}
		catch(Exception e)
		{
			
		}
		return null;
	}
}
