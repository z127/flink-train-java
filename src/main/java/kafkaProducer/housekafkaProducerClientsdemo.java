package kafkaProducer;


import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class housekafkaProducerClientsdemo {



    public static KafkaProducer<String,String> initializeKafka()
    {

        Properties props=new Properties();
        /**
         * 0表示不等待结果返回<br/>
         * 1表示等待至少有一个服务器返回数据接收标识<br/>
         * -1表示必须接收到所有的服务器返回标识，及同步写入<br/>
         * */
        props.put("request.required.acks", "0");
        /**
         * 内部发送数据是异步还是同步
         * sync：同步, 默认
         * async：异步
         */
        //props.put("producer.type", "async");
        props.setProperty("bootstrap.servers", "192.168.231.200:9092");
        props.setProperty("key.serializer", StringSerializer.class.getName());
        props.setProperty("value.serializer", StringSerializer.class.getName());
        /**
         * 设置分区类
         * 根据key进行数据分区
         * 默认是：kafka.producer.DefaultPartitioner ==> 安装key的hash进行分区
         * 可选:kafka.serializer.ByteArrayPartitioner ==> 转换为字节数组后进行hash分区
         */
        //props.put("partitioner.class", "JavaKafkaProducerPartitioner");
        KafkaProducer<String, String> producer= new KafkaProducer( props);
        return producer;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        String current_day=ft.format(dNow);
        //String path=String.format("D:\\pycharmprojects\\get_data_house\\get_data\\%s_data.csv",current_day);
        String path=String.format("D:\\pycharmprojects\\get_data_house\\get_data\\2020-10-01_data.csv");
        System.out.println(path);
        send_data_to_kafka(path);
    }

    private static void send_data_to_kafka(String file_path) throws IOException, InterruptedException {

        FileInputStream fileInputStream=new FileInputStream(file_path);
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(fileInputStream, Charset.forName("GBK")));
        String line=null;
        ArrayList line_list= new ArrayList();
        // 4. 发送数据到服务器，并发线程发送
        final AtomicBoolean flag = new AtomicBoolean(true);
        int numThreads = 10;


        KafkaProducer<String, String> kafka_producer= initializeKafka();

        while ( (line=bufferedReader.readLine())!=null)
        {

            String replaced_line=line.replace("\"","");
                //替换过后的line
                System.out.println("count:"+line+" ");
                //builder.append(line.replace("\"",""))
                kafka_producer.send(new ProducerRecord<String, String>("flink_data_house", replaced_line.toString()));
                Thread.sleep(1000);
        }

        fileInputStream.close();
    }
}
