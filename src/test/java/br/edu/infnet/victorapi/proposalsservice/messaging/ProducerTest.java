package br.edu.infnet.victorapi.proposalsservice.messaging;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.Instant;

import static org.mockito.Mockito.*;

class ProducerTest {

    @Test
    void sendProposalApproved_callsRabbitTemplate() {
        RabbitTemplate rt = Mockito.mock(RabbitTemplate.class);
        Producer p = new Producer(rt);

        ProposalApprovedEvent e = new ProposalApprovedEvent(1L, "Title", Instant.now());
        p.sendProposalApproved(e);

        verify(rt, times(1)).convertAndSend(eq("victor.queue"), eq(e));
    }
}
