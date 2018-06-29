#!/bin/bash

echo "Starting sqlite sink connector"
connect-standalone /etc/schema-registry/connect-avro-standalone.properties /etc/kafka-connect-jdbc/sink-quickstart-sqlite.properties &

echo "Consume output topic, then produce to the sink connector"
kafka-console-consumer --bootstrap-server localhost:9092 --topic streams-test-output --from-beginning | while read line; do
    echo $line
    echo $line | kafka-avro-console-producer --broker-list localhost:9092 --topic orders --property value.schema='{"type":"record","name":"myrecord","fields":[{"name":"id","type":"int"},{"name":"product","type": "string"}, {"name":"quantity", "type": "int"}, {"name":"price","type": "float"}]}'
done &
