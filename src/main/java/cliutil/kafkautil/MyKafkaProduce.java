package cliutil.kafkautil;


import cliutil.CliDemo;
import cliutil.hdfsutil.HdfsUtil;
import cliutil.parquetutil.MyParquetReader;
import cliutil.secutil.SecUtil;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.FileStatus;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by zhou1 on 2018/11/13.
 */
public class MyKafkaProduce {
    private static Logger LOG = LoggerFactory.getLogger(MyKafkaProduce.class);

    private final static String AES_KEY = "aes.key";
    private final static  String SASL_JAAS_CONFIG = "sasl.jaas.config";

    private String topic;
    private String kfkAddr;

    private Integer bufSize = 800000;

    private org.apache.kafka.clients.producer.KafkaProducer<String, String> producer;
    private HdfsUtil hdfsUtil;
    private SecUtil se;
    private String aes_key;
    private String saslConfig;

    public MyKafkaProduce(HdfsUtil hdfsUtilInput, String kfkAddrInput, String topicInput, Properties prop) {
        this.kfkAddr = kfkAddrInput;
        this.topic = topicInput;
        se = new SecUtil();
        this.aes_key = prop.getProperty(AES_KEY);
        this.saslConfig = prop.getProperty(SASL_JAAS_CONFIG);

        Properties kafkaProps = new Properties();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            kafkaProps.load(classLoader.getResourceAsStream("jaas.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kfkAddr);
//        props.put("acks", "all");
        kafkaProps.put(ProducerConfig.RETRIES_CONFIG, 1);
        kafkaProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        kafkaProps.put(ProducerConfig.LINGER_MS_CONFIG, 0);
//        kafkaProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
//        props.put("min.insync.replicas",3);

//        props.put("sasl.jaas.config",
//                "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"ckafka-g0widy27#yonghui_yunchao\" password=\"yonghuitencent123\";");
        kafkaProps.put("sasl.jaas.config",saslConfig);
        producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(kafkaProps);
        hdfsUtil = hdfsUtilInput;
    }

    public void avroProduce(FileStatus fileStatus) {
        String fileKey = fileStatus.getPath().getName();
        MyParquetReader myParquetReader= null;

        try {
            myParquetReader = new MyParquetReader(fileStatus.getPath().toUri());
            GenericRecord record = null;

            Future<RecordMetadata> sendingFuture = null;

            while ((record = myParquetReader.readRecord()) != null) {

                try {
//                    RecordMetadata result = producer.send(new ProducerRecord<String, String>(this.topic
//                            ,fileKey,record.toString())).get();
                    sendingFuture = producer.send(new ProducerRecord<>(this.topic
                            , "", se.AESEncode(this.aes_key,record.toString())));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("send {} record to kafka error: {}", fileStatus.getPath(), e.getMessage());
                }
            }

            //确保最后地一个消息发完
            if (sendingFuture !=null){
                try{
                    sendingFuture.get();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }catch (ExecutionException e){
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            LOG.error("read parquet file {} failed", fileStatus.getPath());
        }finally {
            producer.close();
            if (myParquetReader!=null){
                try{
                    myParquetReader.close();
                }catch (IOException e){
                    LOG.error("close {} parquet reader failed", fileStatus.getPath());
                }
            }
        }
    }

}
