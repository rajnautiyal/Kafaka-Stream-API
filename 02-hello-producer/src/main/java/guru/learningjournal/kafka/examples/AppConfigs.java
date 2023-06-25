package guru.learningjournal.kafka.examples;

class AppConfigs {
    final static String applicationID = "HelloProducer";
    final static String bootstrapServers = "localhost:9092,localhost:9093";

    final static String topicName = "hello-producer";
    final static String topicName1 = "hello-producer-1";
    final static String topicName2 = "hello-producer-2";

    final static String translation_id = "Hello-Producer-Trans";

    final static int numEvents = 2;
}
