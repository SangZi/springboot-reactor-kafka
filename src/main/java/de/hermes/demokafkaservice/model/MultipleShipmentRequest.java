package de.hermes.demokafkaservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultipleShipmentRequest {

    private String orderId;
    //private Instant orderDateTime;
    private String emailAddress;
    private String sender;
    private String recipient;
}
