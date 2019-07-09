package cliutil.kafkautil;

import cliutil.hdfsutil.HdfsUtil;
import cliutil.parquetutil.MyParquetReader;
import cliutil.secutil.SecUtil;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.FileStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by zhou1 on 2019/7/9.
 */
public class MyKafkaConsumer {
    private static Logger LOG = LoggerFactory.getLogger(MyKafkaProduce.class);

    private final static String AES_KEY = "aes.key";
    private final static  String SASL_JAAS_CONFIG = "sasl.jaas.config";

    private String topic;
    private String kfkAddr;

    private Integer bufSize = 800000;

    private org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer;
    private SecUtil se;
    private String aes_key;
    private String saslConfig;

    public MyKafkaConsumer( String kfkAddrInput, String topicInput, Properties prop) {
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
        kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
//        props.put("min.insync.replicas",3);

//        props.put("sasl.jaas.config",
//                "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"ckafka-g0widy27#yonghui_yunchao\" password=\"yonghuitencent123\";");
        kafkaProps.put("sasl.jaas.config",saslConfig);
        consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<String, String>(kafkaProps);

    }

    public void consume() {
        consumer.subscribe(Arrays.asList("t_order_yh_yunchao"));

        System.err.println( consumer.partitionsFor("t_order_yh_yunchao"));
//        consumer.partitionsFor()
//        consumer.seek(new TopicPartition("t_order_yh_yunchao", 0),1051684522L);
//        consumer.seek(null,1051684522L);
        while (true) {
//            ConsumerRecords<String, String> records = consumer.poll(100);
//            for (ConsumerRecord<String, String> record : records) {
////                System.out.printf("offset = %d, key = %s, value = %s",record.offset(), record.key(), record.value());
//                System.out.printf("%s\n", record.value());
//            }

        }
    }
}
