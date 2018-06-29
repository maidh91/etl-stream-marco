#!/bin/bash

bash /tmp/start_etl_stream.sh
bash /tmp/start_sqlite_sink_connector.sh

echo "Generating input events"
id=0
while true; do        
    echo $id | kafka-console-producer --broker-list localhost:9092 --topic streams-test-input
    sleep 0.02
    id=$((id+1))
done
