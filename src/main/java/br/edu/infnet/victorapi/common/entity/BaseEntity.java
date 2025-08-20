package br.edu.infnet.victorapi.common.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "is_active")
    protected Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.isActive == null) {
            this.isActive = true;
        }
        onPrePersist();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        onPreUpdate();
    }

    protected void onPrePersist() {
    }

    protected void onPreUpdate() {
    }

    public abstract boolean isValid();

    public final void activate() {
        if (canActivate()) {
            this.isActive = true;
            onActivate();
        }
    }

    public final void deactivate() {
        if (canDeactivate()) {
            this.isActive = false;
            onDeactivate();
        }
    }

    protected boolean canActivate() {
        return !this.isActive;
    }

    protected boolean canDeactivate() {
        return this.isActive;
    }

    protected void onActivate() {
    }

    protected void onDeactivate() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseEntity that = (BaseEntity) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("%s{id=%d, isActive=%s, createdAt=%s, updatedAt=%s}", 
            getClass().getSimpleName(), id, isActive, createdAt, updatedAt);
    }
}
