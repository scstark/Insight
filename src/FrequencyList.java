import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Creates a record of the number of times each word appears in given text file.
 * @author stephanie2694
 *
 */
public class FrequencyList 
{
	/**
	 * Stores the table of word frequencies, with each 
	 * word being the key and its count being the value.
	 * 
	 * Using SortedMap interface to get sorted keys for free.
	 */
	private SortedMap<String, Integer> table;

	
	/**
	 * Contructor.
	 */
	public FrequencyList()
	{
		//call super.
		super();
		//use TreeMap implementation of SortedMap.
		table = new TreeMap<String, Integer>();

	}
	
	/**
	 * Add a word to the table.
	 * @param word
	 */
	public void add( String word )
	{
		if ( !table.containsKey( word ) )
		{
			
			//create a new key with value 1.
			table.put( word, 1 );
		}
		else
		{
			//we've seen this word before so update its current map value.
			updateValue( word );
		}
	}
	

	/**
	 * Updates the value of a key already existing in the table.
	 * @param key
	 */
	private void updateValue( String key )
	{
		//get the existing key and update its value.
		table.put( key, table.get(key) + 1 );
	}
	
	/**
	 * Returns the iterator for the keys. Keys are 
	 * automatically in alphabetical order.
	 * @return
	 */
	public Iterator<String> iterator()
	{
	
		return table.keySet().iterator();
	}

	/**
	 * Get the value associated with the key. For writing the output file.
	 * @param key
	 * @return
	 */
	public Integer getVal( String key )
	{
		return table.get( key );
	}
	
}
