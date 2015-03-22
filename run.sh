#!/bin/sh
#run.sh
#Insight data engineering coding challenge

apt-get install openjdk-7-jdk openjdk-7-doc openjdk-7-jre-lib

chmod a+x src/WordCount.java
chmod a+x src/RunningMedian.java
chmod a+x src/FrequencyList.java

javac -d bin src/*.java
java -cp bin WordCount
java -cp bin RunningMedian