#!/bin/sh
#run.sh
#Insight data engineering coding challenge


install openjdk-7-jdk

chmod a+x src/WordCount.java
chmod a+x src/RunningMedian.java
chmod a+x src/FrequencyList.java

javac -d bin src/*.java
java -cp bin WordCount
java -cp bin RunningMedian