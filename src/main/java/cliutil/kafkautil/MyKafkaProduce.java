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
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhou1 on 2018/11/13.
 */
public class MyKafkaProduce implements Runnable {
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

    private String hdfsSnapshotDir;
    private String hdfsUrl;
    private FileStatus fileStatus;


    @Override
    public void run(){

        Properties kafkaProps = new Properties();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            kafkaProps.load(classLoader.getResourceAsStream("jaas.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kfkAddr);
        kafkaProps.put(ProducerConfig.ACKS_CONFIG, "1");
        kafkaProps.put(ProducerConfig.RETRIES_CONFIG, 999);
        kafkaProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 163840);
        kafkaProps.put(ProducerConfig.LINGER_MS_CONFIG, 20);
        kafkaProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 120000);
        kafkaProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
//        kafkaProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
//        props.put("min.insync.replicas",3);

//        props.put("sasl.jaas.config",
//                "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"ckafka-g0widy27#yonghui_yunchao\" password=\"yonghuitencent123\";");
        kafkaProps.put("sasl.jaas.config",saslConfig);
        producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(kafkaProps);

        String sourceFileLocation = fileStatus.getPath().toString();
        String targetFileLocation =String.format("%s%s",hdfsSnapshotDir,sourceFileLocation.replace(hdfsUrl,""));
        LOG.info("handling file {}", sourceFileLocation);
        this.avroProduce(fileStatus);

        try{
            hdfsUtil.copyFileToSnapshot(sourceFileLocation, targetFileLocation);
        }catch (Exception e){
            LOG.error("{} 移动异常 {}",sourceFileLocation,e.getMessage());
        }
        LOG.info("handling file finish {}", sourceFileLocation);
    }

    public MyKafkaProduce(HdfsUtil hdfsUtilInput, String kfkAddrInput, String topicInput, Properties prop,
                          String snapShotDir, String hdfsUrl, FileStatus fileStatus) {
        this.hdfsSnapshotDir = snapShotDir;
        this.hdfsUrl = hdfsUrl;
        this.fileStatus = fileStatus;


        this.kfkAddr = kfkAddrInput;
        this.topic = topicInput;
        se = new SecUtil();
        this.aes_key = prop.getProperty(AES_KEY);
        this.saslConfig = prop.getProperty(SASL_JAAS_CONFIG);


        hdfsUtil = hdfsUtilInput;
    }

    public void avroProduce(FileStatus fileStatus) {
        String fileKey = fileStatus.getPath().getName();
        MyParquetReader myParquetReader= null;
        List<PartitionInfo> partitionInfoList = producer.partitionsFor(this.topic);
        LOG.info("partition size is {}",partitionInfoList.size());

        try {
            myParquetReader = new MyParquetReader(fileStatus.getPath().toUri());
            GenericRecord record = null;

            Future<RecordMetadata> sendingFuture = null;

            while ((record = myParquetReader.readRecord()) != null) {

                try {
                    sendingFuture = producer.send(new ProducerRecord<>(this.topic
                            , null, se.AESEncode(this.aes_key,record.toString())),
                            new MyKafkaCallback());
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("send {} record to kafka error: {}", fileStatus.getPath(), e.getMessage());
                }
            }

            //确保最后地一个消息发完
            if (sendingFuture !=null){
                try{
                    sendingFuture.get();
//                    Thread.sleep(TimeUnit.SECONDS.toMillis(2));
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
