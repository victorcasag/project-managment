package br.edu.infnet.victorapi.modules.area.entity;

import br.edu.infnet.victorapi.common.entity.BaseEntity;
import br.edu.infnet.victorapi.common.entity.Auditable;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "areas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "area_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("BASIC")
public class Area extends BaseEntity implements Auditable {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "description")
    private String description;

    public Area() {
        super();
    }

    public Area(String name, String code, String description) {
        super();
        this.name = name;
        this.code = code;
        this.description = description;
    }

    @Override
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() && 
               code != null && !code.trim().isEmpty();
    }

    @Override
    protected void onPrePersist() {
        if (this.code != null) {
            this.code = this.code.toUpperCase().trim();
        }
        if (this.name != null) {
            this.name = this.name.trim();
        }
    }

    @Override
    protected void onPreUpdate() {
        onPrePersist();
    }

    @Override
    public String getAuditInfo() {
        return String.format("Area[id=%d, name='%s', code='%s', active=%s, created=%s, updated=%s]",
            getId(), name, code, getIsActive(), getCreatedAt(), getUpdatedAt());
    }

    @Override
    public boolean isRecentlyModified() {
        if (getUpdatedAt() == null) return false;
        long hoursAgo = ChronoUnit.HOURS.between(getUpdatedAt(), LocalDateTime.now());
        return Math.abs(hoursAgo) <= 24;
    }

    @Override
    public String getAuditIdentifier() {
        return "AREA_" + (getId() != null ? getId() : "NEW") + "_" + 
               (code != null ? code : "NO_CODE");
    }

    @Override
    protected boolean canDeactivate() {
        return super.canDeactivate() && isValid();
    }

    @Override
    protected void onDeactivate() {
        System.out.println("Area desativada: " + getAuditIdentifier());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}