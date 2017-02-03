# Demo how to stop the spark streaming gracefully
Example to show how to stop the Spark Streaming application gracefully.

Spark submit command:

`spark-submit --class com.cloudera.ps.GracefulShutdownExample --master yarn --deploy-mode cluster --num-executors 3 /tmp/streamingstopgraceful-1.0-SNAPSHOT.jar`

To shutdown the streaming app gracefully, place a file named "shutdownmarker" to HDFS /tmp folder

`hdfs dfs -put shutdownmarker /tmp/shutdownmarker`