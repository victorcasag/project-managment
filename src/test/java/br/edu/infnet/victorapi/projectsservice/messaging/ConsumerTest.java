package br.edu.infnet.victorapi.projectsservice.messaging;

import br.edu.infnet.victorapi.proposalsservice.messaging.ProposalApprovedEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class ConsumerTest {

    @Test
    void handle_printsWithoutException() {
        Consumer c = new Consumer();
        ProposalApprovedEvent e = new ProposalApprovedEvent(10L, "T", Instant.now());
        c.handle(e);
        // no assertion, just ensure no exception
    }
}
