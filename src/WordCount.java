import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.nio.charset.Charset;
import java.nio.charset.UnmappableCharacterException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
//import java.nio.file.Path;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * All I/O happens in this class.
 * Contains main method.
 * @author stephanie2694
 *
 */
public class WordCount 
{
	/**
	 * Instance of the frequency list to
	 */
	private FrequencyList list;
	
	/**
	 * Constructor.
	 */
	public WordCount()
	{
		list = new FrequencyList();
		
	}
	
	/**
	 * I/O happens here.
	 */
	public void readInput()
	{
		//for each file in the directory "wc_input".
		Path dir = Paths.get( "wc_input" );
		
		//filter files in input directory by txt extension.
		try ( DirectoryStream<Path> stream = Files.newDirectoryStream( dir, "*.txt" ) ) 
		{
		    //for each file in the filtered stream
			for (Path file: stream) 
		    {
				
		      	//try to scan a file					
				try
				{
					Scanner sc = new Scanner( file );
					System.out.println( "Reading file " + file.getFileName() );
					//while there are still tokens to be read in the file
					while( sc.hasNext() )
					{
					
						String word = sc.next();
						//strip punctuation from token, aka remove all punctuation
						//characters besides hyphens and apostrophes.
						//word = word.replaceAll("[^\\p{L} ]","");
						word = word.replaceAll( "[^\\P{P}]+", "" );
						//word = word.replaceAll( "[^[:ascii:]]", "" );
						//converting to lower case for uniformity and
						//to ensure capitalization does not skew word count.
						//also ensuring empty string is not present.
						if ( word.length() > 0 )
						{
							list.add( word.toLowerCase() );
						}
						
					}
				}
				catch( FileNotFoundException e )
				{
					//e.printStackTrace();
					//inform user of exception
					System.out.println("File " + file.getFileName() +
							" not found. \nContinuing on to next file.");
					//move on to next file
					continue;
				}
					
			}
		    
		   
				
				
		}
		catch ( IOException|DirectoryIteratorException x) 
		{
		    // IOException can never be thrown by the iteration.
		    // In this snippet, it can only be thrown by newDirectoryStream.
		    System.err.println(x);
		}
				
		 //after all files are read and counted on the list:
		//save the output to file
		saveOutput();
		//File dir = new File( "wc_input" );		
		
//		if( dir.exists() && dir.isDirectory() )
//		{
//			File[] input = dir.listFiles();
//			
//			if( input.length == 0 ) //if there are no files in this directory
//			{
//				//inform user there are no files to count
//				System.out.println("no input files!");
//				
//				//exit the program
//				System.exit(0);
//			}
//		}	
//		else //in the case of a bad input, inform the user of what the problem is.
//		{
//			//if it exists then it's not a directory
//			if( dir.exists() )
//			{
//				System.out.println( "File is not a directory." );
//				System.exit(0);
//			}
//			else //otherwise is doesn't exist at all.
//			{
//				System.out.println( "File does not exist." );
//				System.exit(0);
//
//			}
//			
//		}
	
		
	}
	
	/**
	 * Start. Takes no command line arguments.
	 * @param args
	 */
	public static void main( String[] args )
	{
		//Files.deleteIfExists( Path.get)
		
		WordCount wc = new WordCount();
		
		wc.readInput();
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
			
			Files.deleteIfExists( Paths.get( "wc_output/wc_result.txt" ) );
			
			Path output = Files.createFile( Paths.get( "wc_output/wc_result.txt" ) );
			
			Charset charset = Charset.forName("UTF-8");
			
			try ( BufferedWriter writer = Files.newBufferedWriter( output, charset ) ) 
			{
				Iterator<String> it = list.iterator();
				
				while( it.hasNext() )
				//Set<String> keys = list.keys();
				//for( String key : keys )
				{
					String key = it.next();
					
					String s = key + "\t" + Integer.toString( list.getVal( key ) );
					
					writer.write( s );//, 0, s.length());
					
					writer.newLine();
				}
				
				
			
			} 
			catch (UnmappableCharacterException x)
			{
			    System.err.format("IOException: %s%n", x);
			    //System.out.println( s );
			    //if( x.getCause().equals(UnmappableCharacterException))
			    x.printStackTrace();
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		/*//make directory "wc_output"
		File dir = new File("wc_output");
		
		//if there is a file already named "wc_output"
		if( dir.exists() && !dir.isDirectory() )
		{
			//delete that file to "old_wc_output"
			dir.delete();
			System.out.println("Deleting old file named wc_output to make room for directory of the same name.");
			dir.mkdir();
		}
		else if ( dir.exists() && dir.isDirectory() )
		{
			System.out.println("deleting any child files so only desired output file is inside.");
			
			File[] children = dir.listFiles();
			System.out.println( "# of child files: " + children.length );
			for( int i = 0; i < children.length; i++ )
			{
				if( children[i].isDirectory() )
				{
					deleteChildren( children[i] );
				}
				System.out.println( "deleting file " + children[i].getName() );

				children[i].delete();
			}
		}
		else //else dir doesn't exist as a directory or a file.
		{
			System.out.println("creating new directory for output");
			dir.mkdir(); //so we make it
		}	
		
		//take the String version of the list and save it to file as "wc_result.txt" in this directory. 
		//there will be no overlapping name issues at this point since we have ensured
		//that the directory "wc_output" is empty.
		FileWriter output;
		try 
		{
			output = new FileWriter( dir.getName() + "/wc_result.txt" );
			output.write( list.toString() );

		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	/**
	 * Recursive file to get rid of children if there is a directory child.
	 * @param file
	 */
	private void deleteChildren( File file )
	{
		File[] children = file.listFiles();
		
		for( int i = 0; i < children.length; i++ )
		{
			if( children[i].isDirectory() && children[i].listFiles().length > 0 )
			{
				deleteChildren( children[i] );
			}
			System.out.println( "deleting file " + children[i].getName() );
			//delete the file once any existing children are deleted.	
			children[i].delete();
			
		}
	}
}
