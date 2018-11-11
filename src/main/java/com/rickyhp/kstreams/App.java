package com.rickyhp.kstreams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.Consumed;
import java.util.Arrays;
import java.util.Properties;
import com.rickyhp.model.Purchase;
import com.rickyhp.serializer.JsonDeserializer;
import com.rickyhp.serializer.JsonSerializer;

public class App 
{
	public static void main(String[] args) throws Exception {

        StreamsConfig streamingConfig = new StreamsConfig(getProperties());
        
        JsonDeserializer<Purchase> purchaseJsonDeserializer = new JsonDeserializer<>(Purchase.class);
        JsonSerializer<Purchase> purchaseJsonSerializer = new JsonSerializer<>();
        
        Serde<String> stringSerde = Serdes.String();
        Serde<Purchase> purchaseSerde = Serdes.serdeFrom(purchaseJsonSerializer,purchaseJsonDeserializer);
        
        KStreamBuilder kStreamBuilder = new KStreamBuilder();


        KStream<String,Purchase> purchaseKStream = kStreamBuilder.stream(stringSerde,purchaseSerde,"src-topic")
                .mapValues(p -> Purchase.builder(p).maskCreditCard().build());
        
        purchaseKStream.to(stringSerde,purchaseSerde,"purchases");

        System.out.println("Starting PurchaseStreams Example");
        KafkaStreams kafkaStreams = new KafkaStreams(kStreamBuilder,streamingConfig);
        kafkaStreams.cleanUp();
        kafkaStreams.start();
        System.out.println("Now started PurchaseStreams Example");

    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.CLIENT_ID_CONFIG, "Example-Processor-Job");
        props.put("group.id", "kstreams1-consumer-group");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "testing-processor-api");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //props.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "localhost:2181");
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        //props.put(StreamsConfig.TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);
        return props;
    }
}
