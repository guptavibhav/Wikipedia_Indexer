/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class represents a stream of tokens as the name suggests. It wraps the
 * token stream and provides utility methods to manipulate it
 * 
 * @author nikhillo
 * 
 */
public class TokenStream implements Iterator<String> {

	/**
	 * Default constructor
	 * 
	 * @param bldr
	 *            : The stringbuilder to seed the stream
	 */

	public StringBuilder TknStreamBldr;
	private ArrayList<String> TknArrayList;
	private int currentPointer;
	private Map<String, Integer> TknMap;
	private boolean IsStateValid = false;
	public boolean IsPretokenizationState = false;
	
	public TokenStream(StringBuilder bldr) {
		// TODO: Implement this method
		this.TknStreamBldr = bldr;
		if (bldr.length() > 0) {
			TknArrayList = new ArrayList<String>();
			currentPointer = 0;
			TknMap = new HashMap<String, Integer>();
			IsStateValid = false; // it will stop from Performing any actions in method
			// update map ? 
		} else {
			IsStateValid = false;
		}
	}

	/**
	 * Overloaded constructor
	 * 
	 * @param bldr
	 *            : THe stringbuilder to seed the stream
	 */
	public TokenStream(String string) {
		// TODO: Implement this method
		if (string != null && !"".equals(string)) {
			this.TknStreamBldr = new StringBuilder();
			TknStreamBldr.append(string);
			TknArrayList = new ArrayList<String>();
			currentPointer = 0;

			TknMap = new HashMap<String, Integer>();
			IsStateValid = true;
			TknArrayList.add(string);
			// currentPointer++;
			UpdateTokenMap(string, 1);
		} else {
			IsStateValid = false;
		}
	}

	/**
	 * Method to append tokens to the stream
	 * 
	 * @param tokens
	 *            : The tokens to be appended
	 */
	public void append(String... tokens) {
		// TODO: Implement this method
		// not changing the current pointer

		if (IsStateValid) {
			if (tokens != null) {
				for (String str : tokens) {
					if (str != null && !"".equals(str)) {
						TknArrayList.add(str);
						// change the map
						UpdateTokenMap(str, 1);
						TknStreamBldr.append(" "+str);
						// increment the pointer if required
					}
				}
			}
		}

	}

	/**
	 * Method to retrieve a map of token to count mapping This map should
	 * contain the unique set of tokens as keys The values should be the number
	 * of occurrences of the token in the given stream
	 * 
	 * @return The map as described above, no restrictions on ordering
	 *         applicable
	 */
	public Map<String, Integer> getTokenMap() {
		// TODO: Implement this method
		
		
		if (IsStateValid)
			return TknMap;
		else
			return null;
	}

	/**
	 * Method to get the underlying token stream as a collection of tokens
	 * 
	 * @return A collection containing the ordered tokens as wrapped by this
	 *         stream Each token must be a separate element within the
	 *         collection. Operations on the returned collection should NOT
	 *         affect the token stream
	 */
	public Collection<String> getAllTokens() {
		// TODO: Implement this method
		if (IsStateValid){
			
			if (IsPretokenizationState)
			{
				// return single string from stringBuilder
				if(TknStreamBldr.length()> 0 ) // added if else to handle empty string array return 
				return new ArrayList<String>( Arrays.asList( TknStreamBldr.toString()));
				else return null;
			}
			else
			{
				if (IsStateValid)
					return TknArrayList; // does it needs creating clone?
				else
					return null;
				}
		}
		else 
			return null;
		
	}

	/**
	 * Method to query for the given token within the stream
	 * 
	 * @param token
	 *            : The token to be queried
	 * @return: THe number of times it occurs within the stream, 0 if not found
	 */
	public int query(String token) {
		// TODO: Implement this method
		if(IsStateValid)
		{
		if (TknMap.containsKey(token)) {
			return TknMap.get(token);
		} else
			return 0;
		}else 
			return 0;
		
	}

	/**
	 * Iterator method: Method to check if the stream has any more tokens
	 * 
	 * @return true if a token exists to iterate over, false otherwise
	 */
	public boolean hasNext() {
		// TODO: Implement this method
		if (IsStateValid) {
			// if (currentPointer == -1 || currentPointer == TknArrayList.size()
			// - 1) {
			if (currentPointer == TknArrayList.size()
					|| TknArrayList.size() == 0) {
				return false;
			} else {
				return true;
			}

		} else
			return false;
	}

	/**
	 * Iterator method: Method to check if the stream has any more tokens
	 * 
	 * @return true if a token exists to iterate over, false otherwise
	 */
	public boolean hasPrevious() {

		// TODO: Implement this method
		if (IsStateValid) {
			if (currentPointer <= 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}

	}

	/**
	 * Iterator method: Method to get the next token from the stream Callers
	 * must call the set method to modify the token, changing the value of the
	 * token returned by this method must not alter the stream
	 * 
	 * @return The next token from the stream, null if at the end
	 */
	public String next() {
		// TODO: Implement this method
		if (IsStateValid) {
			if (currentPointer < TknArrayList.size()) {

				return TknArrayList.get(currentPointer++);
			} else {
				// actually should throw exception
				return null;
			}
		} else
			return null;
	}

	/**
	 * Iterator method: Method to get the previous token from the stream Callers
	 * must call the set method to modify the token, changing the value of the
	 * token returned by this method must not alter the stream
	 * 
	 * @return The next token from the stream, null if at the end
	 */
	public String previous() {
		// TODO: Implement this method
		if (IsStateValid) {
			if (currentPointer > 0) {
				return TknArrayList.get(--currentPointer);
			} else {
				// actually should throw exception
				return null;
			}
		} else
			return null;

	}

	/**
	 * Iterator method: Method to remove the current token from the stream
	 */
	public void remove() {
		// TODO: Implement this method
		if (IsStateValid) {
			if (TknArrayList.size() == 0 || currentPointer == TknArrayList.size()) {
				// do nothing
			} else {
				String str = TknArrayList.remove(currentPointer);
				// if(currentPointer>=TknArrayList.size())
				// currentPointer--;
				// update the map
				UpdateTokenMap(str, -1);

			}
		}
	}

	/**
	 * Method to merge the current token with the previous token, assumes
	 * whitespace separator between tokens when merged. The token iterator
	 * should now point to the newly merged token (i.e. the previous one)
	 * 
	 * @return true if the merge succeeded, false otherwise
	 */
	public boolean mergeWithPrevious() {
		// TODO: Implement this method
		if (IsStateValid) {
			if (currentPointer <= 0 || currentPointer >= TknArrayList.size() )
				return false;
			else {
				/*
				 * String currentToken = TknArrayList.get(currentPointer);
				 * TknArrayList.remove(currentPointer); currentPointer--;//
				 * decrease the pointer to point it at the correct // element
				 * TknArrayList.set(currentPointer,
				 * TknArrayList.get(currentPointer) + " " + currentToken);
				 * 
				 * if (TknMap.containsKey(currentToken)) { // if tokenmap shows
				 * count as 1 then just remove the element // from map. Else
				 * reduce the count by 1 if (TknMap.get(currentToken) > 1)
				 * TknMap.put(currentToken, TknMap.get(currentToken) - 1); else
				 * TknMap.remove(currentToken); } else { // some problem. Token
				 * map was not in sync. }
				 */

				
				String currentToken = TknArrayList.get(currentPointer);
				String previousToken = TknArrayList.get(currentPointer - 1);

				// remove the token
				TknArrayList.remove(currentPointer);

				// set the merged value at current position
				TknArrayList.set(currentPointer-1, previousToken + " "
						+ currentToken);

				// update the token map count for removed token
				UpdateTokenMap(previousToken, -1);

				// update the token map for the token accepting the merge
				UpdateTokenMap(currentToken, -1);

				// update the token map for combined token
				UpdateTokenMap(TknArrayList.get(currentPointer-1), 1);

				currentPointer--;
				return true;
			}
		} else
			return false;

	}

	/**
	 * Method to merge the current token with the next token, assumes whitespace
	 * separator between tokens when merged. The token iterator should now point
	 * to the newly merged token (i.e. the current one)
	 * 
	 * @return true if the merge succeeded, false otherwise
	 */
	public boolean mergeWithNext() {
		// TODO: Implement this method
		if (IsStateValid) {
			if (currentPointer >= (TknArrayList.size() - 1)
					|| TknArrayList.size() == 1)
				return false;
			else {

				/*
				 * if(currentPointer==-1 ) // To remember : put tokenstream to
				 * invalid state when CurrentPointer reaches -1
				 * currentPointer++;
				 */

				String currentToken = TknArrayList.get(currentPointer);
				String removedToken = TknArrayList.get(currentPointer + 1);

				// remove the token
				TknArrayList.remove(currentPointer + 1);

				// no need to decrement the pointer as we are removing Current +
				// 1
				// and merging that with current

				// set the merged value at current position
				TknArrayList.set(currentPointer, currentToken + " "
						+ removedToken);

				// update the token map count for removed token
				UpdateTokenMap(removedToken, -1);

				// update the token map for the token accepting the merge
				UpdateTokenMap(currentToken, -1);

				// update the token map for combined token
				UpdateTokenMap(TknArrayList.get(currentPointer), 1);

				return true;
			}
		} else
			return false;

	}

	/**
	 * Method to replace the current token with the given tokens The stream
	 * should be manipulated accordingly based upon the number of tokens set It
	 * is expected that remove will be called to delete a token instead of
	 * passing null or an empty string here. The iterator should point to the
	 * last set token, i.e, last token in the passed array.
	 * 
	 * @param newValue
	 *            : The array of new values with every new token as a separate
	 *            element within the array
	 */
	public void set(String... newValue) {
		// TODO: Implement this method

		if (IsStateValid) {
			/*
			 * if (currentPointer == -1 || currentPointer ==
			 * TknArrayList.size()) { // empty array or after end of array or
			 * before start of array. Not sure if functions should work here.
			 */
			if (TknArrayList.size() == 0 && currentPointer == 0 )
			{
				
				// term token  not working because of this hence adding below code
				/*
				if (newValue != null)
				{
					currentPointer--; // pointer goes to - 1 
					for(String tmpstr : newValue)
					{
						if (tmpstr!= null)
						{ 
						 TknArrayList.add(tmpstr);
						 currentPointer ++ ;
						}
					}
					currentPointer = (currentPointer < 0? 0 :currentPointer);// to set it to zero if no element gets added     
						
				}*/
			
				
			}
			else if( currentPointer == TknArrayList.size()) 
			{
				// empty array or after end of array or before start of array.
				// Not sure if functions should work here.
				

			} else {

				/*
				 * if (currentPointer == -1 ) currentPointer++; else if
				 * (currentPointer == TknArrayList.size()) currentPointer--;
				 */

				if (newValue != null && newValue.length > 0) {
					if (newValue[0] != null && !"".equals(newValue[0])) {
						String currentToken = TknArrayList.get(currentPointer);
						TknArrayList.set(currentPointer, newValue[0]);

						// Reduce the count by one for removed token
						UpdateTokenMap(currentToken, -1);

						// increment the count for new replacement
						UpdateTokenMap(TknArrayList.get(currentPointer), 1);
					}

					// insert values from n to 1 into the arraylist
					int pointerPosition = currentPointer;
					// currentPointer--;
					for (int i = newValue.length - 1; i >= 1; i--) {

						if (newValue[i] != null && !"".equals(newValue[i])) {
							// add new element to arraylist and increment the
							// pointer

							TknArrayList.add(pointerPosition + 1, newValue[i]);
							currentPointer++;

							// update the map for newly added element
							UpdateTokenMap(TknArrayList.get(pointerPosition + 1), 1);
						}
					}
				

				}
			}
		}

	}

	/**
	 * Iterator method: Method to reset the iterator to the start of the stream
	 * next must be called to get a token
	 */
	public void reset() {
		// TODO: Implement this method
		if (IsStateValid) {
			currentPointer = 0; // check the impact on other operations
		}
	}

	/**
	 * Iterator method: Method to set the iterator to beyond the last token in
	 * the stream previous must be called to get a token
	 */
	public void seekEnd() {

		if (IsStateValid) {
			currentPointer = TknArrayList.size();// Check the impact
		}
	}

	/**
	 * Method to merge this stream with another stream
	 * 
	 * @param other
	 *            : The stream to be merged
	 */
	public void merge(TokenStream other) {

		// TODO: Implement this method

		if (IsStateValid) {

			if (other != null && other.IsStateValid) {
				if (TknArrayList.addAll(other.getAllTokens())) // append to the
																// end of
																// Tkn stream
				{

					// Update the token map
					Map<String, Integer> m = other.getTokenMap();
					Iterator<String> itr = m.keySet().iterator();
					while (itr.hasNext()) {
						String str = (String) itr.next();
						/*
						 * if (TknMap.containsKey(str)) { // increment the count
						 * by value in the map of other TknMap.put(str,
						 * TknMap.get(str) + m.get(str)); } else { // add the
						 * key value into the map and set the count to 1
						 * TknMap.put(str, m.get(str)); }
						 */

						UpdateTokenMap(str, m.get(str));
					}

				}
			}
		}
		else 
		{
			if (other.IsStateValid)
			{
				IsStateValid = true;
				currentPointer = 0;
				TknArrayList  = new ArrayList<String>();
				TknArrayList.addAll(other.TknArrayList);
				TknMap =  new HashMap<String, Integer>();
				TknMap.putAll(other.TknMap);
				
			}
	       		
			
		}

	}

	private void UpdateTokenMap(String str, int IncrementCount) {
		if (IsStateValid) {
			if (TknMap.containsKey(str)) {
				// get the finalCount of the token that needs to updated
				int finalCount = TknMap.get(str) + IncrementCount;

				if (finalCount < 1)
					// remove the token from tokenmap
					TknMap.remove(str);
				else
					// Update the tokenmap with new values
					TknMap.put(str, finalCount);
			} else {
				// Add the token to the token map with the count
				TknMap.put(str, IncrementCount);
			}
		}
	}
	
	public  void setValidState()
	{
		this.IsStateValid  = true;
	}
	
	public void InitStream(String ...initValue)
	{
		if (initValue != null)
		{
			currentPointer = -1; // pointer goes to - 1
			TknArrayList.clear();
			TknMap.clear();
			for(String tmpstr : initValue)
			{
				if (tmpstr!= null)
				{ 
				 TknArrayList.add(tmpstr);
				 UpdateTokenMap(tmpstr, 1);
				 currentPointer ++ ;
				}
			}
			currentPointer = (currentPointer < 0? 0 :currentPointer);// to set it to zero if no element gets added     
				
		}
	}
	
	/*public void Initialize(String str)
	{
		TknArrayList.clear();
		currentPointer = 0 ;
		TknMap.clear();
		}*/
}
