package com.ps.cromdata.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A CronConfig.
 */
@Entity
@Table(name = "cron_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CronConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "config_short", nullable = false, unique = true)
    private String config_short;

    @NotNull
    @Column(name = "config_long", nullable = false, unique = true)
    private String config_long;

    @Column(name = "config_description")
    private String config_description;

    @NotNull
    @Column(name = "restart_required", nullable = false)
    private Boolean restart_required;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfig_short() {
        return config_short;
    }

    public CronConfig config_short(String config_short) {
        this.config_short = config_short;
        return this;
    }

    public void setConfig_short(String config_short) {
        this.config_short = config_short;
    }

    public String getConfig_long() {
        return config_long;
    }

    public CronConfig config_long(String config_long) {
        this.config_long = config_long;
        return this;
    }

    public void setConfig_long(String config_long) {
        this.config_long = config_long;
    }

    public String getConfig_description() {
        return config_description;
    }

    public CronConfig config_description(String config_description) {
        this.config_description = config_description;
        return this;
    }

    public void setConfig_description(String config_description) {
        this.config_description = config_description;
    }

    public Boolean isRestart_required() {
        return restart_required;
    }

    public CronConfig restart_required(Boolean restart_required) {
        this.restart_required = restart_required;
        return this;
    }

    public void setRestart_required(Boolean restart_required) {
        this.restart_required = restart_required;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CronConfig)) {
            return false;
        }
        return id != null && id.equals(((CronConfig) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CronConfig{" +
            "id=" + getId() +
            ", config_short='" + getConfig_short() + "'" +
            ", config_long='" + getConfig_long() + "'" +
            ", config_description='" + getConfig_description() + "'" +
            ", restart_required='" + isRestart_required() + "'" +
            "}";
    }
}
