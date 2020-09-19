package kafkaProducer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;

public class housekafkaProducer  {
    public static void main(String[] args) throws InterruptedException, IOException {
        Properties properties=new Properties();
        properties.setProperty("bootstrap.servers", "192.168.231.200:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        KafkaProducer<String,String> producer= new KafkaProducer(properties);
        //
        String topic="flink_data_house";
        String path="D:\\pycharmprojects\\get_data_house\\get_data\\data.csv";
        FileInputStream fileInputStream=new FileInputStream(path);
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(fileInputStream,Charset.forName("GBK")));
        String line=null;
        while ( (line=bufferedReader.readLine())!=null)
        {


                StringBuilder builder = new StringBuilder();
                System.out.println(line);
                builder.append(line.replace("\"",""));
                producer.send(new ProducerRecord<String, String>(topic, builder.toString()));
                Thread.sleep(1000);

        }
        fileInputStream.close();
    }
}
