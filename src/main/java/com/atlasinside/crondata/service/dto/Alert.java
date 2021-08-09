package com.atlasinside.crondata.service.dto;

import java.util.Date;
import java.util.Map;

public class Alert {
    private String status;
    private Map<String, String> labels;
    private Map<String, String> annotations;
    private Date startsAt;
    private Date endsAt;

    private String generatorUrl;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public Map<String, String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Map<String, String> annotations) {
        this.annotations = annotations;
    }

    public Date getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(Date startsAt) {
        this.startsAt = startsAt;
    }

    public Date getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(Date endsAt) {
        this.endsAt = endsAt;
    }

    public String getGeneratorUrl() {
        return generatorUrl;
    }

    public void setGeneratorUrl(String generatorUrl) {
        this.generatorUrl = generatorUrl;
    }
}
