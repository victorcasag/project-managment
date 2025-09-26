package br.edu.infnet.victorapi.projectsservice.messaging;

import java.io.Serializable;
import java.time.Instant;

public class ProjectCreatedDTO implements Serializable {
    private Long projectId;
    private String name;
    private Instant createdAt;

    public ProjectCreatedDTO() {}

    public ProjectCreatedDTO(Long projectId, String name, Instant createdAt) {
        this.projectId = projectId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public Long getProjectId() { return projectId; }
    public String getName() { return name; }
    public Instant getCreatedAt() { return createdAt; }

    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public void setName(String name) { this.name = name; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
