package com.ps.cromdata.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Alerts.
 */
@Entity
@Table(name = "alerts")
public class Alerts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "summary")
    private String summary;

    @Column(name = "description")
    private String description;

    @Column(name = "severity")
    private String severity;

    @Column(name = "status")
    private String status;

    @Column(name = "readed")
    private Boolean readed;

    @Column(name = "received_at")
    private Instant receivedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Alerts name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public Alerts summary(String summary) {
        this.summary = summary;
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public Alerts description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public Alerts severity(String severity) {
        this.severity = severity;
        return this;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public Alerts status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getReaded() {
        return readed;
    }

    public Alerts readed(Boolean readed) {
        this.readed = readed;
        return this;
    }

    public void setReaded(Boolean readed) {
        this.readed = readed;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public Alerts receivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
        return this;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alerts)) {
            return false;
        }
        return id != null && id.equals(((Alerts) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alerts{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", summary='" + getSummary() + "'" +
            ", description='" + getDescription() + "'" +
            ", severity='" + getSeverity() + "'" +
            ", status='" + getStatus() + "'" +
            ", readed='" + getReaded() + "'" +
            ", receivedAt='" + getReceivedAt() + "'" +
            "}";
    }
}
