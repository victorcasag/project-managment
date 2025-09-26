package br.edu.infnet.victorapi.projectsservice.messaging;

import br.edu.infnet.victorapi.proposalsservice.messaging.ProposalApprovedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    @RabbitListener(queues = "victor.queue")
    public void handle(ProposalApprovedEvent event) {
        // Simple conversion: in real app, create Project entity and persist
        System.out.println("Received proposal approved event: " + event.getProposalId() + " title=" + event.getTitle());
        // ... create project stub
    }
}
