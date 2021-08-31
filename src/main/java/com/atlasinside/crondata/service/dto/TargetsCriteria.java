package com.atlasinside.crondata.service.dto;

import java.io.Serializable;
import java.util.Objects;

import com.atlasinside.crondata.domain.Targets;
import com.atlasinside.crondata.web.rest.TargetsResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link Targets} entity. This class is used
 * in {@link TargetsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /targets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TargetsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter host;

    private IntegerFilter port;

    private StringFilter job;

    private StringFilter description;

    public TargetsCriteria() {
    }

    public TargetsCriteria(TargetsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.host = other.host == null ? null : other.host.copy();
        this.port = other.port == null ? null : other.port.copy();
        this.job = other.job == null ? null : other.job.copy();
        this.description = other.description == null ? null : other.description.copy();
    }

    @Override
    public TargetsCriteria copy() {
        return new TargetsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getHost() {
        return host;
    }

    public void setHost(StringFilter host) {
        this.host = host;
    }

    public IntegerFilter getPort() {
        return port;
    }

    public void setPort(IntegerFilter port) {
        this.port = port;
    }

    public StringFilter getJob() {
        return job;
    }

    public void setJob(StringFilter job) {
        this.job = job;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TargetsCriteria that = (TargetsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(host, that.host) &&
            Objects.equals(port, that.port) &&
            Objects.equals(job, that.job) &&
            Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        host,
        port,
        job,
        description
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TargetsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (host != null ? "host=" + host + ", " : "") +
                (port != null ? "port=" + port + ", " : "") +
                (job != null ? "job=" + job + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
            "}";
    }

}
