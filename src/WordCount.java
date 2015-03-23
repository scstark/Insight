import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.nio.charset.Charset;
import java.nio.charset.UnmappableCharacterException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Reads the files in the input directory, counts them,
 * and saves the word frequencies to file.
 * All I/O happens in this class.
 * Contains main method.
 * @author Stephanie Stark (GitHub: scstark)
 *
 */
public class WordCount 
{
	/**
	 * Instance of the frequency list to store the words with their counts.
	 */
	private FrequencyList list;
	
	/**
	 * Constructor.
	 */
	public WordCount()
	{
		list = new FrequencyList();
		
		readInput();
	}
	
	/**
	 * I/O happens here. Reads the input and adds each word to the FrequencyList.
	 */
	public void readInput()
	{
		//get the path to input directory.
		Path dir = Paths.get( "wc_input" );
		//if the file exists and is a directory.
		if( dir.toFile().exists() && dir.toFile().isDirectory() )
		{
			//filter files in input directory by txt extension.
			//get the iterator associated with this directory stream
			//if there are no .txt files in the directory the output document will be blank.
			try ( DirectoryStream<Path> stream = Files.newDirectoryStream( dir, "*.txt" ) ) 
			{
			    //for each file in the filtered stream
				for (Path file: stream) 
			    {
					// try to scan a file
					Scanner sc = new Scanner( file );
					//inform the user which files are being read.
					System.out.println( "Reading file " + file.getFileName() );
					//while there are still tokens to be read in the file
					while( sc.hasNext() )
					{
					
						String word = sc.next();
						//strip punctuation from token
						word = word.replaceAll( "[^\\P{P}]+", "" );
						
						//converting to lower case for uniformity and
						//to ensure capitalization does not skew word count.
						word = word.toLowerCase();
						//also ensuring empty string is not present.
						if ( word.length() > 0 )
						{
							//add the word to the list
							list.add( word );
						}
						
					}
				}
				//after all files are read and counted on the list:
				//save the output to file
				saveOutput();
				
			}
			catch ( DirectoryIteratorException | IOException x) 
			{
			    // IOException is from the Scanner. It will not be thrown in this implementation
				//because of the directory stream's filter.
			    // The only exception that can make it down here is the DirectoryIteratorException.
			   System.err.println(x);
			  	    
			}
		}
		else
		{
			if( !dir.toFile().exists() )
			{
				System.out.println( "Directory 'wc_input' does not exist. Exiting program." );
				System.exit(0);
			}
			else
			{
				System.out.println( "File 'wc_input' is not a directory. Exiting program." );
				System.exit(0);
			}
		}
	}		
		
	
	
	/**
	 * Start. Takes no command line arguments.
	 * @param args
	 */
	public static void main( String[] args )
	{
				
		new WordCount();
	}
	
	/**
	 * Save table to text file in directory "wc_output" as "wc_result.txt".
	 */
	private void saveOutput()
	{
		try //try to create the directory
		{
			if( !Files.exists( Paths.get( "wc_output" ) ) )
			{
				Path dir = Files.createDirectory( Paths.get( "wc_output" ) );
			}
			
			//preparing to write to file
			Path output = Paths.get( "wc_output/wc_result.txt" );
			
			//we're not trying to delete a directory so it will not throw the associated exceptions
			//deleted the file if it exists to create a new version of the file.
			Files.deleteIfExists( output );
			//create the new version of the file to write on.
			Files.createFile( output );
			//use UTF-8 charset to ensure the UnmappableCharacterException is not thrown.
			//all source files' character sets use UTF-8 encoding or a subset thereof
			Charset charset = Charset.forName("UTF-8");
			//try to create a bufferedWriter
			try ( BufferedWriter writer = Files.newBufferedWriter( output, charset ) ) 
			{
				//get the iterator from the frequency list
				Iterator<String> it = list.iterator();
				
				while( it.hasNext() )
				{
					//get the next key
					String key = it.next();
					//build the line to write with correct formatting.
					String s = key + "\t" + Integer.toString( list.getVal( key ) );
					//write the line
					writer.write( s );
					//get a new line
					writer.newLine();
				}
				//finish writing to file.
				writer.close();
			
			} 
			catch ( UnmappableCharacterException x )
			{
			    System.err.format("IOException: %s%n", x);
			    //if( x.getCause().equals(UnmappableCharacterException))
			    x.printStackTrace();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			
			System.out.println("IOException on creating output directory/file.");
		}
	}
}
