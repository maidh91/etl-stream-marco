#!/bin/bash

echo "Waiting for Kafka to be ready"
cub kafka-ready -b localhost:9092 1 20

echo "Creating Topics"
kafka-topics --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --create --topic streams-test-input
kafka-topics --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --create --topic streams-test-output

echo "Running ETL Streaming"
CLASSPATH=$CLASSPATH:/tmp/scala-library.jar:/tmp/kafka-test.jar; kafka-run-class com.aquabubu.Test &
