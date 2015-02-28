/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

/**
 * @author nikhillo
 * THis class is responsible for assigning a partition to a given term.
 * The static methods imply that all instances of this class should 
 * behave exactly the same. Given a term, irrespective of what instance
 * is called, the same partition number should be assigned to it.
 */
public class Partitioner {
	/**
	 * Method to get the total number of partitions
	 * THis is a pure design choice on how many partitions you need
	 * and also how they are assigned.
	 * @return: Total number of partitions
	 */
	public static int noOfPartitions = 27;
	public static int getNumPartitions() {
		//TODO: Implement this method
		
		// read property file and return the no of partitions
		return noOfPartitions;
	}
	
	/**
	 * Method to fetch the partition number for the given term.
	 * The partition numbers should be assigned from 0 to N-1
	 * where N is the total number of partitions.
	 * @param term: The term to be looked up
	 * @return The assigned partition number for the given term
	 */
	public static int getPartitionNumber (String term) {
		//TDOD: Implement this method
		
		if( noOfPartitions == 1 )
			return 0;
		int startChar =(int)term.toLowerCase().charAt(0); 
		
		if( startChar >=97 && startChar<=122)
		{
			// if characters between a and z then assign into first 0 to n-2 partitions 
			
			return (startChar - 97) % (noOfPartitions -1); 

			
		}
		 // other than alphabetic then assign into last partition 
		return noOfPartitions-1;
		//return -1; 
		 
		
	}
}
