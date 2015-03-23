import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Reads the files in the input directory, 
 * computes the running median value of words 
 * in a line after reading each line,
 * and saves the word frequencies to file.
 * All I/O happens in this class.
 * Contains main method.
 * @author Stephanie Stark (GitHub: scstark)
 *
 */
public class RunningMedian 
{
	/**
	 * Current sample size.
	 */
	int n;
	
	/**
	 * Effectively have a max heap by pushing on 
	 * negatives of the integers.
	 * Hence we can exploit the ordering properties of
	 * the integers with regard to inequalities.
	 */
	PriorityQueue<Integer> left;
	
	/**
	 * Need a min heap to take care of the 
	 * Java's Priority Queue for Integers is 
	 * by default a min heap.
	 */
	PriorityQueue<Integer> right;
	
	/**
	 * Constructor.
	 */
	public RunningMedian()
	{
		//initialize sample size
		n = 0;
		//create new priority queues to hold the integers
		left = new PriorityQueue<Integer>();
		right = new PriorityQueue<Integer>();
		//read the input files
		readInput();
	}
	
	/**
	 * Reads the input from input directory and saves the running median to file.
	 */
	public void readInput()
	{
		//for each file in the directory "wc_input".
		Path dir = Paths.get( "wc_input" );
		//create the directory and file for the output file here since
		//for this problem it is better to continuously write the file as
		//the running median is computed.
		Path output = createOutputDir();
		
		Charset charset = Charset.forName("UTF-8");
		
		try ( BufferedWriter writer = Files.newBufferedWriter( output, charset ) )
		{
			//filter files in input directory by txt extension.
			//iterator automatically gives files by their natural ordering, aka alphabetical
			try ( DirectoryStream<Path> stream = Files.newDirectoryStream( dir, "*.txt" ) ) 
			{
			    //for each file in the filtered stream
				for (Path file: stream) 
			    {
					//create a scanner for the source file
			      	Scanner sc = new Scanner( file );
					//inform user that the file is being read
			      	System.out.println( "Reading file " + file.getFileName() );
					//while there are still tokens to be read in the file
					while( sc.hasNextLine() )
					{	
						//read the line
						String line = sc.nextLine();
						n++;//increment sample size
						
						//count each word on the line.
						Scanner sc2 = new Scanner( line );
						//counting variable
						int count = 0;
						
						while( sc2.hasNext() )
						{
							sc2.next();
							count++; //count the number of words in the line.
						}
						
						//add the total number of words
						add( count );
						//write the running median to file
						writer.write( Double.toString( getRM() ) );
						//create a new line
						writer.newLine();
					
						//System.out.println( "Writing " + Double.toString( getRM() ) );
					}
					
			    }
			}
		}
		catch ( IOException|DirectoryIteratorException x) 
		{
		    // IOException can never be thrown by the iteration.
		    // In this snippet, it can only be thrown by newDirectoryStream.
		    System.err.println(x);
		    
		    x.printStackTrace();
		}
	}
	
	/**
	 * Create the output directory if it doens't exist yet 
	 * as well as the text file to store the output and 
	 * return the path to the output text file.
	 * @return
	 */
	private Path createOutputDir()
	{
		try
		{
			if( !Files.exists( Paths.get( "wc_output" ) ) )
			{
				Path dir = Files.createDirectory( Paths.get( "wc_output" ) );
			}
			
			Files.deleteIfExists( Paths.get( "wc_output/med_result.txt" ) );
			
			Path output = Files.createFile( Paths.get( "wc_output/med_result.txt" ) );
					
			return output;
			
		}
		catch (IOException e ) //this should never get thrown
		{
			e.printStackTrace();

		}
		//System.out.println( "Returning null Path." );
		return null;
	}
	
	
	/**
	 * Calculate the sample median after each line.
	 * Aka the running median.
	 * @return
	 */
	public double getRM()
	{
		double rM = 0;
		
		//if n is even
		if ( n % 2 == 0 )
		{
			//must cast the rM in order to obtain a decimal value if the sum of the numerator is odd
			rM = (double) ( -left.peek() + ( right.peek() ) ) / 2;

		}
		//if n is odd
		else
		{
			if( left.size() > right.size() )
			{
				rM = -left.peek();
			}
			else
			{
				rM = right.peek();
			}
		}
		
		return rM;
	}
	
	/**
	 * Add an integer to the sample after reading a line
	 * @param i
	 */
	private void add( int i )
	{
		if( left.isEmpty() && right.isEmpty() )
		{
			right.add( i );
		}
		else if ( left.isEmpty() )
		{
			if ( i < right.peek() )
			{
				left.add( -i );
			}
			else
			{
				int head = right.remove();
				
				right.add( i );
				
				left.add( -head );
			}
		}
		else
		{

			if( i < right.peek() )
			{
				left.add( -i );
			}
			else
			{
				right.add( i );
			}
			
		}
		
		//make sure the heaps are balanced.
		balance();
	}
	
	/**
	 * Will manage the size between the two priority queues,
	 * such that there is at most a size difference of 1 between
	 * the two.
	 */
	private void balance()
	{
		//take the size difference between the two heaps
		int diff = Math.abs( left.size() - right.size() );
		//if the difference is greater than 1
		if( diff > 1 )
		{
			if( left.size() > right.size() )
			{
				//remove the head of the left heap
				//this head is positive
				int head = left.remove();
				//add it to the right heap, negated.
				right.add( -head );
			}
			else //otherwise the right heap is the larger heap.
			{
				//remove the head of the right heap
				//this head is negative
				int head = right.remove();
				//add it to the left heap, negated.
				left.add( -head );
			}
		}
		
		//otherwise do nothing.
	}
	
	/**
	 * Start the program! Takes no command line arguments.
	 * @param args
	 */
	public static void main( String[] args )
	{
			
		new RunningMedian();
	}
	
	
}
