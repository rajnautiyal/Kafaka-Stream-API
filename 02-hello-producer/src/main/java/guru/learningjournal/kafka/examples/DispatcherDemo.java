package guru.learningjournal.kafka.examples;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DispatcherDemo {

    private static Logger logger= LogManager.getLogger();
    public static void main(String args[]){
        logger.info("staring the HelloProducer");
        Properties props=new Properties();
        try {
            InputStream inputStream=new FileInputStream(MultiAppConfigs.kafkaConfigFileLocation);
            props.load(inputStream);
            props.put(ProducerConfig.CLIENT_ID_CONFIG,MultiAppConfigs.applicationID);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        KafkaProducer<Integer, String> producer=new KafkaProducer<Integer,String>(props);
        Thread[]  dispatchers =new Thread[MultiAppConfigs.eventFiles.length];
        for(int i=0;i<MultiAppConfigs.eventFiles.length;i++){
            dispatchers[i]=new Thread(new Dispatcher(producer,MultiAppConfigs.topicName,MultiAppConfigs.eventFiles[i]));
            dispatchers[i].start();
        }
        try {
            for (Thread t : dispatchers) t.join();
        } catch (InterruptedException e) {
            logger.error("Main Thread Intruppted");
        }finally {
            producer.close();
            logger.info("finshed sending data");
        }
    }
}
