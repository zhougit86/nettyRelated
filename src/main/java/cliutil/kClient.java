package cliutil;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;


/**
 * Created by zhou1 on 2018/11/13.
 */
//  119.28.230.220:9092 -kt event_topic
public class kClient {
    private static KafkaConsumer<String, String> consumer;
    private final static String TOPIC = "event_topic";
//    private final static String TOPIC = "migration_topic";
    public kClient(){
//        String fsPath=System.getProperty("user.dir");
//        System.err.println(fsPath);
//        System.setProperty("java.security.auth.login.config", ""+fsPath+"\\kafka_client_jaas.conf");
//        System.err.println(System.getProperty("java.security.auth.login.config"));

        Properties props = new Properties();
        try{
            props.load(new FileInputStream("jaas.properties"));
        }catch (IOException e){
            e.printStackTrace();
        }
//        props.put("bootstrap.servers", "10.0.68.230:9092");
        props.put("bootstrap.servers", "119.28.230.220:9092");
        //每个消费者分配独立的组号
        props.put("group.id", "test2");
        //如果value合法，则自动提交偏移量
        props.put("enable.auto.commit", "true");
        //设置多久一次更新被消费消息的偏移量
        props.put("auto.commit.interval.ms", "1000");
        //设置会话响应的时间，超过这个时间kafka可以选择放弃消费或者消费下一条消息
        props.put("session.timeout.ms", "30000");
        //自动重置offset
        props.put("auto.offset.reset","earliest");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                StringDeserializer.class);

        props.put("sasl.jaas.config",
                "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"ckafka-g0widy27#yonghui_yunchao\" password=\"yonghuitencent123\";");

        consumer = new KafkaConsumer<String, String>(props);
    }

    public void consume(){
        FileOutputStream fos = null;
        try{
//            File file2 = new File("2.zip");
//            fos = new FileOutputStream(file2);
        }catch (Exception e){

        }



        consumer.subscribe(Arrays.asList(TOPIC));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
//            System.err.println(records.count());
            for (ConsumerRecord<String, String> record : records){
//                System.out.printf("offset = %d, key = %s, value = %s",record.offset(), record.key(), record.value());
                System.out.printf("%s\n",record.value());
//                try{
//                    if (record.key().equals("end")){
//                        System.err.println("end");
////                        fos.close();
//                        break;
//                    }
////                    fos.write(record.value());
//                    System.err.println(record.offset());
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
            }
        }
    }

    public static void main(String[] args) {
        new kClient().consume();
    }
}
