package com.peter.flink.java.kafka_use;


import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper;


import java.util.Properties;

public class kafakaConnectorProducerApp {

    public static void main(String[] args) throws Exception {
        //从socket 接受数据 ，通过flink ，将数据sink到Kafka
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        String  topic="zqjtest";
        //接受数据
        DataStream<String> stream=env.socketTextStream("localhost",9999);

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.231.200:9092");

//        FlinkKafkaProducer myProducer = new FlinkKafkaProducer(
//                "zqjtest",
//                 new SimpleStringSchema(),
//                properties,
//                FlinkKafkaProducer.Semantic.EXACTLY_ONCE);
        stream.print();
        stream.addSink(new FlinkKafkaProducer(
                "zqjtest",
                new KeyedSerializationSchemaWrapper<String>( new SimpleStringSchema()),
                properties,
                FlinkKafkaProducer.Semantic.EXACTLY_ONCE));
        env.execute("KafkaConnectorProducerApp");
    }
}
