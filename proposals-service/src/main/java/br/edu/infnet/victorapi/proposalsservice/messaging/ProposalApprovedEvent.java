package br.edu.infnet.victorapi.proposalsservice.messaging;

import java.io.Serializable;
import java.time.Instant;

public class ProposalApprovedEvent implements Serializable {
    private Long proposalId;
    private String title;
    private Instant approvedAt;

    public ProposalApprovedEvent() {}

    public ProposalApprovedEvent(Long proposalId, String title, Instant approvedAt) {
        this.proposalId = proposalId;
        this.title = title;
        this.approvedAt = approvedAt;
    }

    public Long getProposalId() { return proposalId; }
    public String getTitle() { return title; }
    public Instant getApprovedAt() { return approvedAt; }

    public void setProposalId(Long proposalId) { this.proposalId = proposalId; }
    public void setTitle(String title) { this.title = title; }
    public void setApprovedAt(Instant approvedAt) { this.approvedAt = approvedAt; }
}
