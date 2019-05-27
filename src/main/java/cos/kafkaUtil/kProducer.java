//package cos.kafkaUtil;
//
//
//import org.apache.kafka.clients.producer.*;
//import org.apache.kafka.common.serialization.ByteArraySerializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//
//import java.io.*;
//import java.util.Arrays;
//import java.util.Properties;
//import java.util.concurrent.ExecutionException;
//
////import static kafkaTrial.kSettings.kfkAddr;
////import static kafkaTrial.kSettings.topicString;
//
///**
// * Created by zhou1 on 2018/11/13.
// */
//public class MyKafkaProduce {
//
//    private static MyKafkaProduce<String, String> producer;
//    private final static String TOPIC = "migration_topic";
//    private final static String kfkAddr = "10.0.68.230:9092";
//
//    private final static Integer bufSize = 20;
//    public MyKafkaProduce(){
//        Properties props = new Properties();
////        props.put("metadata.broker.list", "10.0.68.230:9092,10.0.68.231:9092,10.0.68.232:9092");
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kfkAddr);
////        props.put("acks", "all");
//        props.put("retries", 0);
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
////        props.put("min.insync.replicas",3);
//        //设置分区类,根据key进行数据分区
//        producer = new MyKafkaProduce<String, String>(props);
//    }
//    public void produce(String key,String value){
////        File localFile = new File("src/main/resources/test.txt");
//        char[] readBuf = new char[bufSize];
//        Reader reader = null;
//        try{
//            reader = new BufferedReader(new FileReader("src/main/resources/test.txt"));
//        }catch (Exception e){
//
//        }
//
//
//        try{
//            while (true){
//                int length =reader.read(readBuf);
//                if (length == -1){
//                    break;
//                }
//                if (length == bufSize){
//                    producer.send(new ProducerRecord<String, String>(TOPIC ,key,String.valueOf(readBuf))).get();
//                    System.err.printf("Sending %s\n",new String(readBuf));
//                }else {
//                    char[] results = Arrays.copyOfRange(readBuf, 0, length);
//                    producer.send(new ProducerRecord<String, String>(TOPIC ,key,String.valueOf(results))).get();
//                    System.err.printf("Sending %s\n",new String(results));
//                }
//
//
//            }
//
//        }catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//
////        try{
////            System.out.println(f.get());
////            System.out.println(key);
////        }catch (Exception e){
////
////        }
//
////        producer.close();
//    }
//
//    public static void main(String[] args) {
//        MyKafkaProduce k = new MyKafkaProduce();
//        k.produce("","");
//    }
//}
