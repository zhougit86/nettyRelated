package cliutil.kafkautil;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * Created by zhou1 on 2019/7/17.
 */
public class MyKafkaCallback implements Callback {

    public void onCompletion(RecordMetadata recordMetadata, Exception e){

    }
}
