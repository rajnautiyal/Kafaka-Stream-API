package guru.learningjournal.kafka.examples;

import guru.learningjournal.kafka.examples.serde.AppSerdes;
import guru.learningjournal.kafka.examples.types.PosInvoice;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class PosFanoutApp {

    private static Logger logger= LogManager.getLogger();

    private static void  main(String args[]){

        Properties props=new Properties();
        props.put(StreamsConfig.CLIENT_ID_CONFIG,AppConfigs.applicationID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,AppConfigs.bootstrapServers);

        StreamsBuilder builder=new StreamsBuilder();
        KStream<String, PosInvoice> KSO=builder.stream(AppConfigs.posTopicName, Consumed.with(AppSerdes.String(),AppSerdes.PosInvoice()));

        KSO.filter((k, v) -> v.getDeliveryType().equalsIgnoreCase(AppConfigs.DELIVERY_TYPE_HOME_DELIVERY)).to(AppConfigs.shipmentTopicName, Produced.with(AppSerdes.String(), AppSerdes.PosInvoice()));
        ;
        KSO.filter((k,v)->v.getDeliveryType().equalsIgnoreCase((AppConfigs.CUSTOMER_TYPE_PRIME))).mapValues(invoice->RecordBuilder.getNotification(invoice)).to(AppConfigs.notificationTopic,
                Produced.with(AppSerdes.String(),AppSerdes.Notification()));




        KSO.mapValues(invoice->RecordBuilder.getMaskedInvoice(invoice))
                .flatMapValues(invoice->RecordBuilder.getHadoopRecords(invoice)).to(AppConfigs.hadoopTopic,
                Produced.with(AppSerdes.String(),AppSerdes.HadoopRecord()));

        KafkaStreams streams=new KafkaStreams(builder.build(),props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            logger.info("Stopping stream");
            streams.close();
        }));
    }
}
