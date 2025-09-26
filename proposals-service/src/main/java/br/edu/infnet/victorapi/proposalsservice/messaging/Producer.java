package br.edu.infnet.victorapi.proposalsservice.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    private final RabbitTemplate rabbitTemplate;
    public static final String QUEUE = "victor.queue";

    public Producer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendProposalApproved(ProposalApprovedEvent event) {
        rabbitTemplate.convertAndSend(QUEUE, event);
    }
}
