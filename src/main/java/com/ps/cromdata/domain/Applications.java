package com.ps.cromdata.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Applications.
 */
@Entity
@Table(name = "applications")
public class Applications implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "app_name", nullable = false)
    private String appName;

    @Column(name = "app_description")
    private String appDescription;

    @NotNull
    @Column(name = "app_short", nullable = false, unique = true)
    private String appShort;

    @Column(name = "app_icon")
    private String appIcon;

    @NotNull
    @Column(name = "app_path_resource", nullable = false)
    private String appPathResource;

    @Column(name = "installation_link")
    private String installationLink;

    @Column(name = "is_installed")
    private Boolean isInstalled;

    @Column(name = "uid")
    private String uid;


    @ManyToOne
    @JsonIgnoreProperties(value = "applications", allowSetters = true)
    private AppCategory appCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public Applications appName(String appName) {
        this.appName = appName;
        return this;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public Applications appDescription(String appDescription) {
        this.appDescription = appDescription;
        return this;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public String getAppShort() {
        return appShort;
    }

    public Applications appShort(String appShort) {
        this.appShort = appShort;
        return this;
    }

    public void setAppShort(String appShort) {
        this.appShort = appShort;
    }


    public Applications isInstalled(Boolean isInstalled) {
        this.isInstalled = isInstalled;
        return this;
    }

    public void setIsInstalled(Boolean installed) {
        isInstalled = installed;
    }

    public Boolean getIsInstalled() {
        return isInstalled;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public Applications appIcon(String appIcon) {
        this.appIcon = appIcon;
        return this;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppPathResource() {
        return appPathResource;
    }

    public Applications appPathResource(String appPathResource) {
        this.appPathResource = appPathResource;
        return this;
    }

    public void setAppPathResource(String appPathResource) {
        this.appPathResource = appPathResource;
    }

    public String getInstallationLink() {
        return installationLink;
    }

    public Applications appIntall(String appIntall) {
        this.installationLink = appIntall;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public Applications uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setInstallationLink(String appIntall) {
        this.installationLink = appIntall;
    }

    public AppCategory getAppCategory() {
        return appCategory;
    }

    public Applications appCategory(AppCategory appCategory) {
        this.appCategory = appCategory;
        return this;
    }

    public void setAppCategory(AppCategory appCategory) {
        this.appCategory = appCategory;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Applications)) {
            return false;
        }
        return id != null && id.equals(((Applications) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }


    // prettier-ignore
    @Override
    public String toString() {
        return "Applications{" +
            "id=" + getId() +
            ", appName='" + getAppName() + "'" +
            ", appDescription='" + getAppDescription() + "'" +
            ", appShort='" + getAppShort() + "'" +
            ", appIcon='" + getAppIcon() + "'" +
            ", appPathResource='" + getAppPathResource() + "'" +
            ", appIntall='" + getInstallationLink() + "'" +
            "}";
    }
}
