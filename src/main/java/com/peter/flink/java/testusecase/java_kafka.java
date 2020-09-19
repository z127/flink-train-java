//package com.peter.flink.java;
//import kafka.consumer.Consumer;
// import kafka.consumer.ConsumerConfig;
// import kafka.consumer.ConsumerIterator;
// import kafka.consumer.KafkaStream;
// import kafka.javaapi.consumer.ConsumerConnector;
//
//import java.util.Properties;
//
//public class java_kafka {
//    public static void main(String[] args) {
//
//        Properties prop = new Properties();
//        //指明zk的地址
//        prop.put("zookeeper.connect", "192.168.231.200:9092");
//        //指明这个consumer的消费组
//        prop.put("group.id", "group1");
//        //时间设置的过小可能会连接超时。。。
//        prop.put("zookeeper.connection.timeout.ms", "60000");
//        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(prop));
//
//                 //构造一个messageStream：输入流
//                 //String: topic的名称 List: 获取的数据
//                Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer.createMessageStreams(topicCountMap);
//    }
//
//}
