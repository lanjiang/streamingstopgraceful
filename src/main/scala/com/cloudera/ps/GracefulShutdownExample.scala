package com.cloudera.ps

/**
  * Created by ljiang on 12/13/16.
  */
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark._
import org.apache.spark.streaming._


object GracefulShutdownExample {
  val shutdownMarker = "hdfs:///tmp/shutdownmarker"
  var stopFlag:Boolean = false
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("SparkStreamingGracefulShutdown")

    val ssc = new StreamingContext(conf, Seconds(5))
    val lines = ssc.socketTextStream("ljiang-spark-1.vpc.cloudera.com", 9999)
    lines.print()
    ssc.start()
    val checkIntervalMillis = 10000
    var isStopped = false

    while (! isStopped) {
      println("calling awaitTerminationOrTimeout")
      isStopped = ssc.awaitTerminationOrTimeout(checkIntervalMillis)
      if (isStopped)
        println("confirmed! The streaming context is stopped. Exiting application...")
      else
        println("Streaming App is still running. Timeout...")
      checkShutdownMarker
      if (!isStopped && stopFlag) {
        println("stopping ssc right now")
        ssc.stop(true, true)
        println("ssc is stopped!!!!!!!")
      }
    }
  }

  def checkShutdownMarker = {
    if (!stopFlag) {
      val fs = FileSystem.get(new Configuration())
      stopFlag = fs.exists(new Path(shutdownMarker))
    }

  }
}
