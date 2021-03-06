package com.peter.flink.java.testusecase;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class StreamingWCjavaRefactApp {
    public static void main(String[] args) throws Exception {

        //获取参数
        int port=0;
        try {
            ParameterTool parameterTool = ParameterTool.fromArgs(args);
            port= parameterTool.getInt("port");
        }catch(Exception e)
        {
            System.err.println("the port is not set, use the default port:9999");
            port=9999;
        }

        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> text = env.socketTextStream("localhost", port);

        text.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] tokens=value.toLowerCase().split(",");
                for(String token:tokens)
                {
                    if(token.length()>0)
                    {
                        collector.collect(new Tuple2<String,Integer>(token,1));
                    }
                }
            }
        }).keyBy(0).timeWindow(Time.seconds(5)).sum(1).print().setParallelism(1);

        env.execute("StreamingWCjavaApp ");

    }
}

