package de.hermes.demokafkaservice.controll;

import de.hermes.demokafkaservice.model.MultipleShipmentRequest;
import reactor.core.publisher.Mono;

public interface EmailResendService {

    //Mono<Void> resendEmail(MultipleShipmentRequest request);
    void resendEmail(MultipleShipmentRequest request);

}
