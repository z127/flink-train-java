package com.peter.flink.java.project_house;


import com.peter.flink.java.entity.House;
import com.peter.flink.java.utils.Utils;
import connector.MysqlWriter;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import  org.apache.flink.api.java.tuple.Tuple2;


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class kafakaConsumerData {
    final  static  String regEx="[0-9]";
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.231.200:9092");
        properties.setProperty("group.id", "flink_data_house");
        properties.put("serializer.encoding", "gb18030");
        properties.put("auto.offset.reset", "earliest");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        FlinkKafkaConsumer consumer=new FlinkKafkaConsumer<>("flink_data_house", new SimpleStringSchema(), properties);
        consumer.setStartFromEarliest();
        DataStream<String> stream = env.addSource(consumer);
        stream.filter(new FilterFunction<String>() {
            @Override
            public boolean filter(String value) throws Exception {
               if( value.length()-value.replace("|","").length()!=5)
               {
                   return  false;
               }
               return  true;
            }

        }).map(new MapFunction<String, Tuple3<String,House, Integer>>() {
            @Override
            public Tuple3<String,House, Integer> map(String value) throws Exception {
                House house=Utils.transferToHouseObj(value);
                System.out.println(house.toString());
                return new Tuple3(house.getHid(),house,Integer.parseInt(findTotalPrice(house.getTotal_price())));
            }
        }).addSink(new MysqlWriter());
                //.print();

        env.execute("KafkaConnectorConsumerApp");
    }


    public  static  String findTotalPrice(String Total_price)
    {
        Pattern   p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(Total_price);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            sb.append(m.group());
        }
        System.out.println(sb);
        return  sb.toString();
    }
}


