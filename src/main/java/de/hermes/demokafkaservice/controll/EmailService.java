package de.hermes.demokafkaservice.controll;

import de.hermes.demokafkaservice.model.MultipleShipmentRequest;
import reactor.core.publisher.Mono;

public interface EmailService {

    Mono<Void> sendEmail(MultipleShipmentRequest request);

}
