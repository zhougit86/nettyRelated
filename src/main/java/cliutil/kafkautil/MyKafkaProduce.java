package cliutil.kafkautil;


import cliutil.hdfsutil.HdfsUtil;
import cliutil.hdfsutil.MyReader;
import cliutil.parquetutil.MyParquetReader;
import cliutil.serialutil.SecSerializer;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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
    private static Logger LOG = LoggerFactory.getLogger(MyKafkaProduce.class);

    private String topic ;
    private String kfkAddr;

    private Integer bufSize = 800000;

    private org.apache.kafka.clients.producer.KafkaProducer<String, String> producer;
    private HdfsUtil hdfsUtil;

    public MyKafkaProduce(HdfsUtil hdfsUtilInput,String kfkAddrInput,String topicInput){
        this.kfkAddr = kfkAddrInput;
        this.topic = topicInput;


        Properties props = new Properties();
        try{
            props.load(new FileInputStream("jaas.properties"));
        }catch (IOException e){
            e.printStackTrace();
        }
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kfkAddr);
//        props.put("acks", "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
//        props.put("min.insync.replicas",3);

        props.put("sasl.jaas.config",
                "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"ckafka-g0widy27#yonghui_yunchao\" password=\"yonghuitencent123\";");
        producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(props);
        hdfsUtil = hdfsUtilInput;
    }

    public void avroProduce(FileStatus fileStatus){
        String fileKey = fileStatus.getPath().getName();

        try{
            MyParquetReader myParquetReader = new MyParquetReader(fileStatus.getPath().toUri());
            GenericRecord record=null;

            while ((record = myParquetReader.readRecord())!=null){

                try{
//                    RecordMetadata result = producer.send(new ProducerRecord<String, String>(this.topic
//                            ,fileKey,record.toString())).get();
                    producer.send(new ProducerRecord<String, String>(this.topic
                            ,"",record.toString())).get();
                }catch (Exception e){
                    LOG.error("send {} record to kafka error",fileStatus.getPath());
                }

            }

        }catch (IOException e){
            LOG.error("read parquet file {} failed",fileStatus.getPath());
        }
    }

//    public void produce(FileStatus fileStatus){
//
//        ByteBuffer byteBuffer = ByteBuffer.allocate(bufSize);
//        byte[] bytes = new byte[bufSize];
//        MyReader reader = null;
//        try{
//            reader = new MyReader(fileStatus, hdfsUtil.fs);
//        }catch (Exception e){
//            e.printStackTrace();
//            return;
//        }
//
//        try{
//            while (true){
//                int length =reader.read(byteBuffer);
//                if (length == -1){
//                    producer.send(new ProducerRecord<String, byte[]>(TOPIC
//                            ,"end",null)).get();
//                    break;
//                }
//                byteBuffer.flip();
//
//
//                if (length == bufSize){
//                    byteBuffer.get(bytes,0,length);
//                    producer.send(new ProducerRecord<String, byte[]>(TOPIC
//                            ,fileStatus.getPath().getName(),bytes)).get();
//                }else {
//
//                    byteBuffer.get(bytes,0,length);
//                    byte[] results = Arrays.copyOfRange(bytes, 0, length);
//                    producer.send(new ProducerRecord<String, byte[]>(TOPIC
//                            ,fileStatus.getPath().getName(),results)).get();
//                }
//                byteBuffer.clear();
//            }
//
//        }catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (IOException e){
//            e.printStackTrace();
//        }finally {
//            try{
//                reader.close();
//            }catch (IOException e){
//
//            }
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


}
