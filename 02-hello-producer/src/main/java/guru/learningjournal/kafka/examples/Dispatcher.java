package guru.learningjournal.kafka.examples;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Dispatcher implements Runnable {
    private static Logger logger= LogManager.getLogger();
    private  String fileLocation;
    private  String topicName;

    private KafkaProducer<Integer,String> producer;

    public Dispatcher( KafkaProducer<Integer, String> producer,String topicName,String fileLocation) {
        this.fileLocation = fileLocation;
        this.topicName = topicName;
        this.producer = producer;
    }

    @Override
    public void run() {
        logger.info("starting sending message "+ fileLocation);
        int counter=0;
        File file=new File(fileLocation);
        try {
            Scanner scanner=new Scanner(file);
            while (scanner.hasNext()){
                String line=scanner.nextLine();
                producer.send(new ProducerRecord<>(topicName, null, line));
                System.out.println("sending data");
                counter++;
            }
            logger.info("Finising the class execution ");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
