package kafkaProducer;


import kafka.javaapi.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class zhibologKafkaProducer {

    //zhibolog_data

    private Logger logger = Logger.getLogger(String.valueOf(zhibologKafkaProducer.class));
    public static final String TOPIC_NAME = "zhibolog_data";

    public static void main(String[] args) throws InterruptedException, IOException {
        Properties props =new Properties();

        props.setProperty("bootstrap.servers", "192.168.231.200:9092");
        props.setProperty("key.serializer", StringSerializer.class.getName());
        props.setProperty("value.serializer", StringSerializer.class.getName());
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        KafkaProducer<String,String> producer=new KafkaProducer<String, String>(props);
        String topic="zhibolog_data";
        while (true)
        {
            StringBuilder builder=new StringBuilder();
            builder.append("nba").append("\t")
                    .append("CN").append("\t")
                    .append(getLevels()).append("\t")
                    .append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\t")
                    .append(getIpAddress()).append("\t")
                    .append(getDomain()).append("\t")
                    .append(getTraffic()).append("\t");
            System.out.println(builder.toString());
            producer.send(new ProducerRecord<String,String>(topic,builder.toString()));
            Thread.sleep(3000);
        }
    }

    private static long getTraffic() {

        return  new Random().nextInt(10000);
    }

    private static String getDomain() {
        String[] domains= new String[]{"v1.netw2d.com",
                "v2.netw2d.com",
                "v3.netw2d.com",
                "v4.netw2d.com",
                "vkm.netw2d.com",
        };
        return domains[new Random().nextInt(domains.length)];
    }






    //生产level数据
    public  static  String getLevels()
    {
        String[] levels= new String[]{"E","E"};
        return levels[new Random().nextInt(levels.length)];
    }

    //生产IP数据
    public  static  String getIpAddress()
    {
            String[] ips=new String[]{
                    "223.104.18.110",
                    "113.101.75.194",
                    "27.17.127.135",
                    "183.225.139.16",
                    "112.1.66.34",
                    "175.148.211.190",
                    "183.227.58.21",
                    "59.83.198.84",
                    "117.28.38.28",
                    "117.59.39.169"
            };
        return ips[new Random().nextInt(ips.length)];
    }
}
