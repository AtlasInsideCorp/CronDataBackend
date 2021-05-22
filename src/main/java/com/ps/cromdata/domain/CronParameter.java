package com.ps.cromdata.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A CronParameter.
 */
@Entity
@Table(name = "cron_parameter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CronParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "param_short", nullable = false, unique = true)
    private String param_short;

    @NotNull
    @Column(name = "param_long", nullable = false, unique = true)
    private String param_long;

    @NotNull
    @Column(name = "config_id", nullable = false)
    private Integer config_id;

    @NotNull
    @Column(name = "param_type", nullable = false)
    private String param_type;

    @Column(name = "param_description")
    private String param_description;

    @ManyToOne
    @JsonIgnoreProperties(value = "cronParameters", allowSetters = true)
    private CronConfig cronConfig;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParam_short() {
        return param_short;
    }

    public CronParameter param_short(String param_short) {
        this.param_short = param_short;
        return this;
    }

    public void setParam_short(String param_short) {
        this.param_short = param_short;
    }

    public String getParam_long() {
        return param_long;
    }

    public CronParameter param_long(String param_long) {
        this.param_long = param_long;
        return this;
    }

    public void setParam_long(String param_long) {
        this.param_long = param_long;
    }

    public Integer getConfig_id() {
        return config_id;
    }

    public CronParameter config_id(Integer config_id) {
        this.config_id = config_id;
        return this;
    }

    public void setConfig_id(Integer config_id) {
        this.config_id = config_id;
    }

    public String getParam_type() {
        return param_type;
    }

    public CronParameter param_type(String param_type) {
        this.param_type = param_type;
        return this;
    }

    public void setParam_type(String param_type) {
        this.param_type = param_type;
    }

    public String getParam_description() {
        return param_description;
    }

    public CronParameter param_description(String param_description) {
        this.param_description = param_description;
        return this;
    }

    public void setParam_description(String param_description) {
        this.param_description = param_description;
    }

    public CronConfig getCronConfig() {
        return cronConfig;
    }

    public CronParameter cronConfig(CronConfig cronConfig) {
        this.cronConfig = cronConfig;
        return this;
    }

    public void setCronConfig(CronConfig cronConfig) {
        this.cronConfig = cronConfig;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CronParameter)) {
            return false;
        }
        return id != null && id.equals(((CronParameter) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CronParameter{" +
            "id=" + getId() +
            ", param_short='" + getParam_short() + "'" +
            ", param_long='" + getParam_long() + "'" +
            ", config_id=" + getConfig_id() +
            ", param_type='" + getParam_type() + "'" +
            ", param_description='" + getParam_description() + "'" +
            "}";
    }
}
