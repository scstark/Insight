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
   [1]: http://docs.oracle.com/javase/7/docs/api/java/util/TreeMap.html
   *   Creating file streams using the [Files.newDirectoryStream()][2] method scales better for larger inputs.
   [2]: http://docs.oracle.com/javase/7/docs/api/java/nio/file/Files.html#newDirectoryStream(java.nio.file.Path,%20java.lang.String)
   *   Additionally, using the iterator approach and [BufferedWriter][3] class to write the output file works better for larger outputs at the cost of adding an extra try/catch block. However, as all of my test files were using UTF-8 and the output file is written using the UTF-8 character set, there should never be an un-mappable character exception encountered. Compared to building a single String from all of the keys and then writing that to file, this approach will actually reach all of the keys for large inputs.
   [3]: http://docs.oracle.com/javase/7/docs/api/java/io/BufferedWriter.html



*   _Time Complexity:_



   *   For each file, it takes linear time to read all of the tokens and linear time to pre-process the words for removing punctuation and converting the tokens to lower case. The TreeMap ensures that adding the word takes logarithmic time. Checking for containment also takes logarithmic time, and so the total time complexity for reading each file is O(Quadratic) time. In total, processing the input takes O(Cubic) time. 
   *   After checking for the existence of and/or creating the output file, it takes O(Quadratic) time to write the file, as it takes linear time to iterate through all of the keys as well as to write each key and value to file.

*   _Known Bugs:_

   *   A slight ordering issue arises at the end of iterating through the ordered keys. In my testing, the last two keys in the output file were out of order and already contained as keys. I was unable to determine the source of this bug within the time allotted for the challenge.

Running Median
--------------
*   _Description:_ 
   *   Upon reading directory of input text files “wc\_input”, processes each file alphabetically and for each file, counts each word in each line. After obtaining the word count for each line, then computes the sample median for the words per line up to that point in time, and saves it to file in directory “wc\_output” as “wc\_result.txt”.

*   _Implementation Notes:_ 
   *   In order to compute the sample median, we need to obtain the value(s) that would be at the center index (or two center indices if the sample size is even) if the sample values were stored in a sorted array. Therefore, I used Java’s [implementation of a priority queue][4] (a minimum heap) to obtain the maximum and minimum values for what would be in the respective left and right ‘halves’ of such a sorted list. I exploited the properties of an ordered field to obtain a maximum heap for the ‘left’ priority queue by pushing negated integers onto the left heap and re-negating them upon retrieval. Since counting the number of words in a line of text may never produce a value smaller than zero, this approach is robust and well-defined.
   [4]: http://docs.oracle.com/javase/7/docs/api/java/util/PriorityQueue.html
   *   A potential drawback of this implementation for large inputs would be the repeating of many integers in the priority queue, leading to the inefficient usage of memory. An alternate approach to this would be to store the count how many times a value has been seen if the value is already contained in the heap, avoiding the problem of using this count for sparsely distributed data. However this would not have been a ‘clean’ way of implementing a solution to this problem, as a crucial part of the priority queue-based approach to this problem involves re-distributing the root values between heaps if the size difference between the two heaps exceeds 1.

   *   I assumed that the word count for each line would be an integer for this implementation. However, my approach would still work for real numbers.

   *   The directory stream implementation I used will automatically filter the files by .txt extension as well as alphabetize them. Hence the files are read in alphabetical order.

*    _Time Complexity:_ 
   *   For each file, it takes O(Quadratic) time to count each word and add it to the heaps, and then write it to file. Adding takes logarithmic time but is dominated by the linear time it takes to count each word in the line.

*   _Known Bugs:_ 
   *   None.