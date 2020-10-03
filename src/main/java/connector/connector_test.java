package connector;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper;

import java.util.Properties;

public class connector_test {

    public static void main(String[] args) throws Exception {
        //从socket 接受数据 ，通过flink ，将数据sink到Kafka
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //，读取mysql数据，获取dataStream后可以做逻辑处理，这里没有
        DataStreamSource<Tuple2<String, String>> stream=env.addSource(new MysqlSource());
        stream.print();
        env.execute("flink mysql demo");

    }
}
