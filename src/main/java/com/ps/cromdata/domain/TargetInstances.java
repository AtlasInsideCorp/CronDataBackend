package com.ps.cromdata.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A TargetInstances.
 */
@Entity
@Table(name = "target_instances")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TargetInstances implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "target_host", nullable = false, unique = true)
    private String targetHost;

    @NotNull
    @Column(name = "job", nullable = false)
    private String job;

    @Column(name = "zone")
    private String zone;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTargetHost() {
        return targetHost;
    }

    public TargetInstances target_host(String target_host) {
        this.targetHost = target_host;
        return this;
    }

    public void setTargetHost(String target_host) {
        this.targetHost = target_host;
    }

    public String getJob() {
        return job;
    }

    public TargetInstances job(String job) {
        this.job = job;
        return this;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getZone() {
        return zone;
    }

    public TargetInstances zone(String zone) {
        this.zone = zone;
        return this;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TargetInstances)) {
            return false;
        }
        return id != null && id.equals(((TargetInstances) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TargetInstances{" +
            "id=" + getId() +
            ", target_host='" + getTargetHost() + "'" +
            ", job='" + getJob() + "'" +
            ", zone='" + getZone() + "'" +
            "}";
    }
}
