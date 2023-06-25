cd /Users/raj/Documents/Workspace/Kafka/kafka_2.12-3.4.0/bin/
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --whitelist "hello-producer-1|hello-producer-2"