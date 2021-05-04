package zhibo.flink;

import connector.MysqlWriterZhiboData;
import org.apache.commons.collections.IteratorUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DateFormatInstance;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class LogZhiboFlinkWatermarkTest {
    //在生产上记录日志
    static Logger Logger_user= LoggerFactory.getLogger("LogZhiboFlinkAnalysis");
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.231.200:9092");
        properties.setProperty("group.id", "flink_data_house");
        properties.put("serializer.encoding", "gb18030");
        properties.put("auto.offset.reset", "earliest");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        FlinkKafkaConsumer consumer=new FlinkKafkaConsumer<>("zhibolog_data", new SimpleStringSchema(), properties);
        consumer.setStartFromEarliest();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //接受kafka数据
        DataStream<String> stream = env.addSource(consumer);

       DataStream<Tuple3<Long, String, Long>> computed_stream=stream.map(new MapFunction<String, Object>() {
            @Override
            public Tuple4 map(String value) throws Exception {
               String[]     items=value.split("\t");
               String level=items[2];
               String time_ds=items[3];
               Long time_long=0L;
               try {
                   SimpleDateFormat sdf=       new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   time_long=sdf.parse(time_ds).getTime();
               }catch (Exception e)
               {
                   Logger_user.error("time parse errot:$timeStr",e.getMessage());
               }
               String domain=items[5];
               String traffic=items[6];
               return new Tuple4<String,Long,String,Long>(level,time_long,domain,Long.parseLong(traffic));
            }
        }).filter(new FilterFunction<Object>() {
            @Override
            public boolean filter(Object value) throws Exception {
               Tuple4<String,Long,String,Long> result= (Tuple4<String,Long,String,Long>)value;
               return  result.f1!=0?true:false;
            }
        }).filter(new FilterFunction<Object>() {
            @Override
            public boolean filter(Object value) throws Exception {
                Tuple4<String,Long,String,Long> result= (Tuple4<String,Long,String,Long>)value;
                return  result.f0.equals("E")?true:false;
            }
        }).map(new MapFunction<Object, Tuple3<Long,String,Long>>() {
            @Override
            public Tuple3<Long,String,Long> map(Object value) throws Exception {
                Tuple4<String,Long,String,Long> result= (Tuple4<String,Long,String,Long>)value;
                return new Tuple3<Long,String,Long>(result.f1,result.f2,result.f3);
            }
        });
        //设置水印生成时间间隔100ms
        env.getConfig().setAutoWatermarkInterval(100);
        computed_stream.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<Tuple3<Long, String, Long>>() {

            private final long maxOutOfOrderness =10000;

            private long currentMaxTimestamp=0000L;

            @Nullable
            @Override
            public Watermark getCurrentWatermark() {

                return new Watermark(currentMaxTimestamp-maxOutOfOrderness);

            }

            @Override
            public long extractTimestamp(Tuple3<Long, String, Long> element, long previousElementTimestamp) {
                long timestamp =element.f0;
                currentMaxTimestamp=Math.max(previousElementTimestamp,currentMaxTimestamp);
                System.err.println(element + ",EventTime:" + timestamp + ",watermark: " + (timestamp - maxOutOfOrderness)+",event_time "+ DateFormatInstance.getInstance().parse_long(timestamp )+", watermark: "+DateFormatInstance.getInstance().parse_long(timestamp - maxOutOfOrderness ));
                return timestamp;
            }
        }).keyBy(1).//此处是按照域名进行排序
                window(TumblingEventTimeWindows.of(Time.seconds(10))).
                apply(new WindowFunction<Tuple3<Long, String, Long>, Tuple6<String, Integer,String,String,String,String>, Tuple, TimeWindow>() {


                    @Override
                    public void apply(Tuple tuple, TimeWindow window, Iterable<Tuple3<Long, String, Long>> input, Collector<Tuple6<String, Integer,String,String,String,String>> out) throws Exception {
                        String key=tuple.getField(0).toString();
                        long sum=0l;
                        String timestamp_date=null;
                       List<Tuple3<Long, String, Long>> list_zhibo_item= IteratorUtils.toList(input.iterator());
                       int item_size=list_zhibo_item.size();
                        Long max_window_time=list_zhibo_item.stream().max(new Comparator<Tuple3<Long, String, Long>>() {
                            @Override
                            public int compare(Tuple3<Long, String, Long> o1, Tuple3<Long, String, Long> o2) {
                                return (int)(o1.f0-o2.f0);
                            }
                        }).get().f0;

                        Long min_window_time=list_zhibo_item.stream().min(new Comparator<Tuple3<Long, String, Long>>() {
                            @Override
                            public int compare(Tuple3<Long, String, Long> o1, Tuple3<Long, String, Long> o2) {
                                return (int)(o1.f0-o2.f0);
                            }
                        }).get().f0;

//                        String min_time_stamp=  DateFormatInstance.getInstance().parse_long(min_window_time );
//                        String max_time_stamp=  DateFormatInstance.getInstance().parse_long(max_window_time );
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        out.collect(new Tuple6<>(key,item_size,format.format(min_window_time),format.format(max_window_time),format.format(window.getStart()),format.format(window.getEnd())));


                    }
                }).print();
        //统计level=E的数据
        env.execute("LogZhiboFlinkAnalysis");
    }
}
