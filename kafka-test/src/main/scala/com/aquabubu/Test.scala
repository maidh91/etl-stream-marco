package com.aquabubu

import java.util.Properties
import java.util.concurrent.CountDownLatch

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.{KafkaStreams, KeyValue, StreamsBuilder, StreamsConfig}
import org.slf4j.LoggerFactory

object Test extends App {
  val bootstrapServers = if (args.length > 0) args(0) else "localhost:9092"

  val LOG = LoggerFactory.getLogger(getClass.getName)
  val APP_NAME = "streams-test"
  val APP_INPUT = "streams-test-input"
  val APP_OUTPUT = "streams-test-output"
  val APP_SHUTDOWN_HOOK = "streams-test-shutdown-hook"

  implicit def Tuple2ToKeyValue[K, V](tuple: (K, V)): KeyValue[K, V] = new KeyValue(tuple._1, tuple._2)

  val props = new Properties
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, APP_NAME)
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
  props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)
  props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)

  val builder = new StreamsBuilder
  val source: KStream[String, String] = builder.stream(APP_INPUT)
  val out: KStream[String, String] = source.mapValues{ (_, value) =>
    "{\"id\":" + value + ",\"product\":\"foo\",\"quantity\":100,\"price\":50}" }
  out.to(APP_OUTPUT)

  val streams = new KafkaStreams(builder.build, props)
  val latch = new CountDownLatch(1)
  Runtime.getRuntime.addShutdownHook(new Thread(APP_SHUTDOWN_HOOK) {
    override def run(): Unit = {
      streams.close()
      latch.countDown()
    }
  })
  try {
    streams.start()
    latch.await()
  } catch {
    case _: Throwable =>
      System.exit(1)
  }
}
