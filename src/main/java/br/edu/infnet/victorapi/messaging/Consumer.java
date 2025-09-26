package br.edu.infnet.victorapi.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @RabbitListener(queues = "victor.queue")
    public void receive(String payload) {
        // process payload
        System.out.println("Received payload: " + payload);
    }
}
