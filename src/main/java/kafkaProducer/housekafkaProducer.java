package kafkaProducer;




import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.apache.kafka.common.serialization.StringSerializer;


import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class housekafkaProducer  {
    public static final int producerNum=50;//实例池大小
    //public static BlockingQueue<KafkaProducer<String, String>> queue=new LinkedBlockingQueue<>(producerNum);

//    static {
//        for (int i = 0; i <producerNum ; i++) {
//            KafkaProducer<String,String>  housekafkaProducer=initializeKafka();
//            queue.add( housekafkaProducer);
//        }
//    }

    private Logger logger = Logger.getLogger(String.valueOf(housekafkaProducer.class));
    public static final String TOPIC_NAME = "flink_data_house";

    public static void main(String[] args) throws InterruptedException, IOException {
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        String current_day=ft.format(dNow);
        String path=String.format("D:\\pycharmprojects\\get_data_house\\get_data\\%s_data.csv",current_day);
        System.out.println(path);
        send_data_to_kafka(path);
    }

          public static Producer<String,String>  initializeKafka()
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
                props.setProperty("metadata.broker.list","192.168.231.200:9092");
                props.setProperty("key.serializer", StringSerializer.class.getName());
                props.setProperty("value.serializer", StringSerializer.class.getName());
                /**
                 * 设置分区类
                 * 根据key进行数据分区
                 * 默认是：kafka.producer.DefaultPartitioner ==> 安装key的hash进行分区
                 * 可选:kafka.serializer.ByteArrayPartitioner ==> 转换为字节数组后进行hash分区
                 */
                //props.put("partitioner.class", "JavaKafkaProducerPartitioner");
                ProducerConfig config = new ProducerConfig(props);
                Producer<String, String> producer= new Producer( config);
                return producer;
            }


    private static void send_data_to_kafka(String file_path) throws IOException, InterruptedException {

        FileInputStream fileInputStream=new FileInputStream(file_path);
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(fileInputStream, Charset.forName("GBK")));
        String line=null;
        ArrayList line_list= new ArrayList();
        // 4. 发送数据到服务器，并发线程发送
        final AtomicBoolean flag = new AtomicBoolean(true);
        int numThreads = 10;
        Producer<String, String> kafka_producer= initializeKafka();
        //用于计数
        int count=0;
        //kafka切片
        List<KeyedMessage<String, String>> messageList = null;
        while ( (line=bufferedReader.readLine())!=null)
        {
                messageList=new ArrayList<KeyedMessage<String, String>>();
//                String replaced_line=line.replace("\"","");
                String replaced_line="12356";
                System.out.println("count:"+count+"line "+line);
                messageList.add(new KeyedMessage<String, String>(TOPIC_NAME,replaced_line));
                System.out.println(replaced_line);
                if(count%500==0)
                {
                    //替换过后的line
                    //builder.append(line.replace("\"",""))
                    kafka_producer.send(messageList);
                    Thread.sleep(1000);
                    messageList=new ArrayList<KeyedMessage<String, String>>();
                    break;
                }
                count++;

        }
        //kafka_producer.send(messageList);
        fileInputStream.close();
    }
}
