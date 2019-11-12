package de.hermes.demokafkaservice.controll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hermes.demokafkaservice.model.MultipleShipmentRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailResendServiceImpl implements EmailResendService {

    /**
     * EmailResendServiceImpl is a Kafka-Consumer, which listens to the topic "pi-email-topic" in kafka
     */

    private static final Logger log = LoggerFactory.getLogger(EmailResendServiceImpl.class.getName());

    private KafkaReceiver kafkaReceiver;

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC = "pi-email-topic";

    //Basic setup of Project Reactor Kafka Consumer
    public EmailResendServiceImpl(){
        final Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, "pi-email-consumer"); //	An id string to pass to the server when making requests. The purpose of this is to be able to track the source of requests beyond just ip/port by allowing a logical application name to be included in server-side request logging
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "pi-email-consumer-group1"); //A unique string that identifies the consumer group this consumer belongs to. This property is required if the consumer uses either the group management functionality by using subscribe(topic) or the Kafka-based offset management strategy.
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        final ReceiverOptions<Object, Object> consumerOptions = ReceiverOptions.create(consumerProps)
                .subscription(Collections.singleton(TOPIC))
                .addAssignListener(partitions -> log.debug("onPartitionsAssigned {}", partitions))
                .addRevokeListener(partitions -> log.debug("onPartitionsRevoked {}", partitions));

        kafkaReceiver = KafkaReceiver.create(consumerOptions);

        ((Flux<ReceiverRecord>) kafkaReceiver.receive())
                //Each element we receive from Kafka, we deserialize it from String back to a multipleShipmentRequest, and use it as method parameter to call resendEmail().
                .doOnNext(
                    r -> {
                        final MultipleShipmentRequest request = fromBinary((String) r.value(), MultipleShipmentRequest.class);
                        resendEmail(request);
                        r.receiverOffset().acknowledge();
                    }
                )
                .subscribe();
    }

    //Deserialization: from String to Object
    private MultipleShipmentRequest fromBinary(String object, Class<MultipleShipmentRequest> resultType) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(object, resultType);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    //public Mono<Void> resendEmail(MultipleShipmentRequest request){
    public void resendEmail(MultipleShipmentRequest request){
        //Resend Email
        log.info("resend email");
    }
}
