package zhibo.flink;

import connector.MysqlWriterZhiboData;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import  org.slf4j.LoggerFactory;
import com.peter.flink.java.entity.House;
import com.peter.flink.java.entity.ZhiboLog;
import com.peter.flink.java.utils.Utils;
import connector.MysqlWriter;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class LogZhiboFlinkAnalysis {
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

            private final long maxOutOfOrderness = 3500;

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
                System.err.println(element + ",EventTime:" + timestamp + ",watermark:" + (timestamp - maxOutOfOrderness));
                return timestamp;
            }
        }).keyBy(1).//此处是按照域名进行排序
                window(TumblingEventTimeWindows.of(Time.seconds(5))).
                apply(new WindowFunction<Tuple3<Long, String, Long>, Tuple3<String, String, Long>, Tuple, TimeWindow>() {
                    @Override
                    public void apply(Tuple tuple, TimeWindow window, Iterable<Tuple3<Long, String, Long>> input, Collector<Tuple3<String, String, Long>> out) throws Exception {
                        String key=tuple.getField(0).toString();
                        long sum=0l;
                        String timestamp_date=null;
                       Iterator list_zhibo_item=input.iterator();
                       while (list_zhibo_item.hasNext())
                       {
                           Tuple3<Long, String, Long> list_item= (Tuple3<Long, String, Long>) list_zhibo_item.next();
                           //traffic求和
                           sum+=list_item.f2;
                           if(timestamp_date ==null) {
                               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                               timestamp_date = sdf.format(new Date(list_item.f0));
                               timestamp_date=timestamp_date+":00";
                               System.out.println(timestamp_date);
                           }
                       }

                        /**第一个参数：这一分钟的时间 2019-09-09 20：20
                         * 第二个参数:域名
                         * 第三个参数:traffic的和
                         */
                        out.collect(new Tuple3(timestamp_date,key,sum));
                    }
                }).addSink(new MysqlWriterZhiboData()).setParallelism(1);
        //统计level=E的数据
        env.execute("LogZhiboFlinkAnalysis");
    }
}
