package guru.learningjournal.kafka.examples;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class HelloProducer {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("staring the HelloProducer");
        Properties pros=new Properties();
        pros.put(ProducerConfig.CLIENT_ID_CONFIG,AppConfigs.applicationID);
        pros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,AppConfigs.bootstrapServers);
        pros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        pros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        pros.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,AppConfigs.translation_id);


        KafkaProducer<Integer,String> producer=new KafkaProducer<Integer, String>(pros);

        producer.initTransactions();
        logger.info("Starting first Translation");
        producer.beginTransaction();
        try {
            for (int i = 999998; i < 1000006; i++) {
                logger.info("Sending data");
                producer.send(new ProducerRecord<>(AppConfigs.topicName, i, "simple Message-T1-" + i));

            }
            logger.info("Committing Translation");
            producer.commitTransaction();

        }catch (Exception e){
            producer.abortTransaction();
            logger.info("Aborting Translation");
            producer.close();
            throw new RuntimeException(e);
        }



        logger.info("Starting first Translation");
        producer.beginTransaction();
        try {
            for (int i = 0; i < AppConfigs.numEvents; i++) {
                producer.send(new ProducerRecord<>(AppConfigs.topicName1, i, "simple Message-T2-" + i));
                producer.send(new ProducerRecord<>(AppConfigs.topicName2, i, "simple Message-T2-" + i));


            }
            logger.info("Aborting Translation");
            producer.abortTransaction();
        }catch (Exception e){
            producer.abortTransaction();
            logger.info("Aborting Translation");
            producer.close();
            throw new RuntimeException(e);
        }


        logger.info("Finished sending the messages closing producer");

    }
}
