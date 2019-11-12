package de.hermes.demokafkaservice.boundary;

import de.hermes.demokafkaservice.model.MultipleShipmentRequest;
import de.hermes.demokafkaservice.controll.EmailServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class EmailController {

    private EmailServiceImpl emailServiceImpl;

    public EmailController(EmailServiceImpl emailServiceImpl){
        this.emailServiceImpl = emailServiceImpl;
    }

    @PostMapping(value = "/email")
    public Mono<Void> sendEmail(@RequestBody MultipleShipmentRequest request){
        return emailServiceImpl.sendEmail(request);
    }

}
