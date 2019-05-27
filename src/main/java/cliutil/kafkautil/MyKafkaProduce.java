package cliutil.kafkautil;


import cliutil.hdfsutil.HdfsUtil;
import cliutil.hdfsutil.MyReader;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.ByteBufferSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

//import static kafkaTrial.kSettings.kfkAddr;
//import static kafkaTrial.kSettings.topicString;

/**
 * Created by zhou1 on 2018/11/13.
 */
public class MyKafkaProduce {
    private final static String TOPIC = "migration_topic";
    private final static String kfkAddr = "10.0.68.230:9092";

    private final static Integer bufSize = 800000;

    private static org.apache.kafka.clients.producer.KafkaProducer<String, byte[]> producer;
    private HdfsUtil hdfsUtil;

    public MyKafkaProduce(HdfsUtil hdfsUtilInput){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kfkAddr);
//        props.put("acks", "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
//        props.put("min.insync.replicas",3);
        //设置分区类,根据key进行数据分区
        producer = new org.apache.kafka.clients.producer.KafkaProducer<String, byte[]>(props);
        hdfsUtil = hdfsUtilInput;
    }



    public void produce(FileStatus fileStatus){

        ByteBuffer byteBuffer = ByteBuffer.allocate(bufSize);
        byte[] bytes = new byte[bufSize];
        MyReader reader = null;
        try{
            reader = new MyReader(fileStatus, hdfsUtil.fs);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        try{
            while (true){
                int length =reader.read(byteBuffer);
                if (length == -1){
                    producer.send(new ProducerRecord<String, byte[]>(TOPIC
                            ,"end",null)).get();
                    break;
                }
                byteBuffer.flip();


                if (length == bufSize){
                    byteBuffer.get(bytes,0,length);
                    producer.send(new ProducerRecord<String, byte[]>(TOPIC
                            ,fileStatus.getPath().getName(),bytes)).get();
                }else {

                    byteBuffer.get(bytes,0,length);
                    byte[] results = Arrays.copyOfRange(bytes, 0, length);
                    producer.send(new ProducerRecord<String, byte[]>(TOPIC
                            ,fileStatus.getPath().getName(),results)).get();
                }
                byteBuffer.clear();
            }

        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                reader.close();
            }catch (IOException e){

            }
        }

//        try{
//            System.out.println(f.get());
//            System.out.println(key);
//        }catch (Exception e){
//
//        }

//        producer.close();
    }


}
