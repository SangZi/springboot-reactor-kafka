package de.hermes.demokafkaservice.controll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import de.hermes.demokafkaservice.model.MultipleShipmentRequest;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    /**
     * EmailServiceImpl is a Kafka-Producer
     */

    private KafkaSender kafkaProducer;

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    //private static final String TOPIC = "pi-email-topic";

    //Basic setup of Project Reactor Kafka Producer
    public EmailServiceImpl(){
        final Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, "pi-email-producer");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        final SenderOptions<Integer, String> producerOptions = SenderOptions.create(producerProps);

        kafkaProducer = KafkaSender.create(producerOptions);
    }

    @Override
    public Mono<Void> sendEmail(MultipleShipmentRequest request){
        String requestString = toBinary(request);
        //In ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class is used
        SenderRecord<Integer, String, Integer> message = SenderRecord.create(new ProducerRecord<>("pi-email-topic", requestString), 1);

        return kafkaProducer.send(Mono.just(message)).next();
    }

    //Serialization: from Object to String
    private String toBinary(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String requestString = ow.writeValueAsString(object);
            return requestString;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
