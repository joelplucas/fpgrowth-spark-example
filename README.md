# A simple example for showing Spark MLlib in action
This demo were presented in PAPIs Connect São Paulo 2017 (http://www.papis.io/connect-sao-paulo-2017/). It takes a CSV file as imput and run the FP-Growth algorithm for finding the most frequent patterns. The content of such file comes from the digital publishing domain and it represents a set of user navigation profiles, where values in rows are eparated by semicoloms (;).

# Compiling the project
In order to compile the project, maven should be used:
$ mvn clean package

If you want to generate a jar file, for running in Spark afterwards, the "spark-jar" compilation profile should be specified:
$ mvn clean package -Pspark-jar

# Running the project
The project must be run inside a Spark cluster. The resulted jar file must be passed as parameter for the "spark-submit" process.

# Unit testing
There are two classes containing unit tests for the Spark processor.