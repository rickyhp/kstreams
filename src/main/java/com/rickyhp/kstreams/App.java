package com.rickyhp.kstreams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.Consumed;
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
         
        StreamsBuilder builder = new StreamsBuilder();
        builder.<String, Purchase>stream("src-topic", Consumed.with(stringSerde, purchaseSerde))
        	.mapValues(value -> Purchase.builder(value).maskCreditCard().build())
        	.to("purchases", Produced.with(stringSerde, purchaseSerde));
        
        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(),streamingConfig);
        kafkaStreams.cleanUp();
        kafkaStreams.start();

    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.CLIENT_ID_CONFIG, "Example-Processor-Job");
        props.put("group.id", "kstreams1-consumer-group");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "testing-processor-api");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        return props;
    }
}
