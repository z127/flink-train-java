# flink项目相关配置

## kafka配置

1. kafka topic名字
`$KAFKA_HOME/bin/kafka-topics.sh --create --zookeeper s200:2181 --replication-factor 1 --partitions 1 --topic flink_data_house`

2. kafka topic consumer
`$KAFKA_HOME/bin/kafka-console-consumer.sh  --bootstrap-server s200:9092 --topic flink_data_house`

3. kafka topic producer
`$KAFKA_HOME/bin/kafka-console-consumer.sh  --bootstrap-server s200:9092 --topic flink_data_house`

4. 查看kafka全部数据
$KAFKA_HOME/bin/kafka-console-consumer.sh  --bootstrap-server s200:9092 --topic flink_data_house --from-beginning