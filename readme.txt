##Insight Data Engineering Coding Challenge
Stephanie Stark

Mount Holyoke College 2015

March 22, 2015

Word Count
----------
*   _Description:_ 
   *   Counts the appearances of specific words in a directory of source text
“wc\_input” and saves it to file in directory “wc\_output” as “wc\_result.txt”.

*   _Implementation Notes:_ 

   *   Implemented using a [Java TreeMap][1] with the words as keys and the count as the values. The TreeMap maintains a red-and-black tree to automatically sort the keys and by default uses the natural ordering of Strings to keep order, providing automatic alphabetical ordering of the keys.
  
   *   Creating file streams using the [Files.newDirectoryStream()][2] method scales better for larger inputs.
  
   *   Additionally, using the iterator approach and [BufferedWriter][3] class to write the output file works better for a larger number of unique keys added to the FrequencyList class (and hence scales better for larger inputs) at the cost of adding an extra try/catch block. However, as all of my test files were using UTF-8 characters and the output file is written using the UTF-8 character set, there should never be an un-mappable character exception encountered. Compared to building a single String from all of the keys and then writing that to file, this approach is much more efficient.



*   _Time Complexity:_

   *   For each file, it takes linear time to read all of the tokens and at most linear time to pre-process each token (removing punctuation and converting the tokens to lower case) before adding it to the TreeMap. The TreeMap ensures that adding each word takes logarithmic time. Checking for containment also takes logarithmic time, and so the total time complexity for reading each file is O(Quadratic) time. In total, processing the input after creating the file stream takes O(Cubic) time. 
   *   After checking for the existence of and/or creating the output file, it takes O(Quadratic) time to write the file, as it takes linear time to iterate through all of the keys as well as to write each key and value to file.

*   _Known Bugs:_

   *   A slight ordering issue arises at the end of iterating through the ordered keys. In my testing, the last two keys in the output file were out of order and already contained as keys. I was unable to determine the source of this bug within the time allotted for the challenge beyond narrowing the source down to the sorted set of keys provided by the TreeMap class.

Running Median
--------------
*   _Description:_ 
   *   Upon reading directory of input text files “wc\_input”, processes each file alphabetically and for each file, counts each word in each line. After obtaining the word count for each line, then computes the sample median for the words per line up to that point in time, and saves it to file in directory “wc\_output” as “wc\_result.txt”.

*   _Implementation Notes:_ 
   *   In order to compute the sample median, we need to obtain the value(s) that would be at the center index (or two center indices if the sample size is even) if the sample values were stored in a sorted array. Therefore, I used Java’s [implementation of a priority queue][4] (a minimum heap) to obtain the maximum and minimum values for what would be in the respective left and right ‘halves’ of such a sorted list. I exploited the properties of an ordered field to obtain a maximum heap for the ‘left’ priority queue by pushing negated integers onto the left heap and re-negating them upon retrieval. Since counting the number of words in a line of text may never produce a value smaller than zero, this approach is robust and well-defined.
  
   *   A potential drawback of this implementation for large inputs would be the repeating of many integers in the priority queue, leading to the inefficient usage of memory. An alternate approach to this would be to store the count how many times a value has been seen if the value is already contained in the heap, avoiding the problem of using this count for sparsely distributed data. However this would not have been a ‘clean’ way of implementing a solution to this problem, as a crucial part of the priority queue-based approach to this problem involves re-distributing the root values between heaps if the size difference between the two heaps exceeds 1.

   *   I assumed that the word count for each line would be an integer for this implementation. However, my approach would still work for real numbers.

   *   The directory stream implementation I used will automatically filter the files by .txt extension as well as alphabetize them. Hence the files are read in alphabetical order.

*    _Time Complexity:_ 
   *   For each file, it takes O(Quadratic) time to count each word per line and add that value one of the two heaps. It then takes at most linear time to write the running median value to file, coming to a total of O(Cubic) time. Adding each integer to a heap takes logarithmic time but is dominated by the linear time it takes to count each word in the line.

*   _Known Bugs:_ 
   *   None.

Acknowledgements
----
All .txt files except 'a.txt' and 'passage.txt' were downloaded from [Project Gutenberg][5]. All of these works are in the public domain. The file 'a.txt' was written by me and the contents of the file 'passage.txt' were provided on the coding challenge webpage.
 
[1]: http://docs.oracle.com/javase/7/docs/api/java/util/TreeMap.html
 [2]: http://docs.oracle.com/javase/7/docs/api/java/nio/file/Files.html#newDirectoryStream(java.nio.file.Path,%20java.lang.String)
 [3]: http://docs.oracle.com/javase/7/docs/api/java/io/BufferedWriter.html
 [4]: http://docs.oracle.com/javase/7/docs/api/java/util/PriorityQueue.html
[5]: http://www.gutenberg.org/