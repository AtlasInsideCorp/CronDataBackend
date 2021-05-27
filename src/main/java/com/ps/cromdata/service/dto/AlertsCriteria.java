package com.ps.cromdata.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.ps.cromdata.domain.Alerts} entity. This class is used
 * in {@link com.ps.cromdata.web.rest.AlertsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /alerts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AlertsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter summary;

    private StringFilter description;

    private StringFilter severity;

    private StringFilter status;

    private BooleanFilter readed;

    private InstantFilter receivedAt;

    public AlertsCriteria() {
    }

    public AlertsCriteria(AlertsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.summary = other.summary == null ? null : other.summary.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.severity = other.severity == null ? null : other.severity.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.readed = other.readed == null ? null : other.readed.copy();
        this.receivedAt = other.receivedAt == null ? null : other.receivedAt.copy();
    }

    @Override
    public AlertsCriteria copy() {
        return new AlertsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getSummary() {
        return summary;
    }

    public void setSummary(StringFilter summary) {
        this.summary = summary;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getSeverity() {
        return severity;
    }

    public void setSeverity(StringFilter severity) {
        this.severity = severity;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public BooleanFilter getReaded() {
        return readed;
    }

    public void setReaded(BooleanFilter readed) {
        this.readed = readed;
    }

    public InstantFilter getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(InstantFilter receivedAt) {
        this.receivedAt = receivedAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AlertsCriteria that = (AlertsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(summary, that.summary) &&
            Objects.equals(description, that.description) &&
            Objects.equals(severity, that.severity) &&
            Objects.equals(status, that.status) &&
            Objects.equals(readed, that.readed) &&
            Objects.equals(receivedAt, that.receivedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        summary,
        description,
        severity,
        status,
        readed,
        receivedAt
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlertsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (summary != null ? "summary=" + summary + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (severity != null ? "severity=" + severity + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (readed != null ? "readed=" + readed + ", " : "") +
                (receivedAt != null ? "receivedAt=" + receivedAt + ", " : "") +
            "}";
    }

}
