package com.peter.flink.java.kafka_use;


import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

public class kafka_consumer {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.231.200:9092");
      properties.setProperty("group.id", "zqjtest");
        properties.put("serializer.encoding", "gb18030");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<String> stream = env.addSource(new FlinkKafkaConsumer<>("zqjtest", new SimpleStringSchema(), properties));
        stream.print();
        env.execute("KafkaConnectorConsumerApp");
    }
}
