package com.ps.cromdata.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.ps.cromdata.domain.Applications} entity. This class is used
 * in {@link com.ps.cromdata.web.rest.ApplicationsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /applications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ApplicationsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter appName;

    private StringFilter appDescription;

    private StringFilter appShort;

    private StringFilter appIcon;

    private StringFilter appPathResource;

    private StringFilter appIntall;

    private LongFilter appCategoryId;

    private BooleanFilter isInstalled;


    public ApplicationsCriteria() {
    }

    public ApplicationsCriteria(ApplicationsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.appName = other.appName == null ? null : other.appName.copy();
        this.appDescription = other.appDescription == null ? null : other.appDescription.copy();
        this.appShort = other.appShort == null ? null : other.appShort.copy();
        this.appIcon = other.appIcon == null ? null : other.appIcon.copy();
        this.appPathResource = other.appPathResource == null ? null : other.appPathResource.copy();
        this.appIntall = other.appIntall == null ? null : other.appIntall.copy();
        this.appCategoryId = other.appCategoryId == null ? null : other.appCategoryId.copy();
        this.isInstalled = other.isInstalled == null ? null : other.isInstalled.copy();
    }

    @Override
    public ApplicationsCriteria copy() {
        return new ApplicationsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAppName() {
        return appName;
    }

    public void setAppName(StringFilter appName) {
        this.appName = appName;
    }

    public StringFilter getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(StringFilter appDescription) {
        this.appDescription = appDescription;
    }

    public StringFilter getAppShort() {
        return appShort;
    }

    public void setAppShort(StringFilter appShort) {
        this.appShort = appShort;
    }

    public StringFilter getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(StringFilter appIcon) {
        this.appIcon = appIcon;
    }

    public StringFilter getAppPathResource() {
        return appPathResource;
    }

    public void setAppPathResource(StringFilter appPathResource) {
        this.appPathResource = appPathResource;
    }

    public StringFilter getAppIntall() {
        return appIntall;
    }

    public void setAppIntall(StringFilter appIntall) {
        this.appIntall = appIntall;
    }

    public LongFilter getAppCategoryId() {
        return appCategoryId;
    }

    public void setAppCategoryId(LongFilter appCategoryId) {
        this.appCategoryId = appCategoryId;
    }

    public BooleanFilter getIsInstalled() {
        return isInstalled;
    }

    public void setIsInstalled(BooleanFilter isInstalled) {
        this.isInstalled = isInstalled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ApplicationsCriteria that = (ApplicationsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(appName, that.appName) &&
            Objects.equals(appDescription, that.appDescription) &&
            Objects.equals(appShort, that.appShort) &&
            Objects.equals(appIcon, that.appIcon) &&
            Objects.equals(appPathResource, that.appPathResource) &&
            Objects.equals(appIntall, that.appIntall) &&
            Objects.equals(appCategoryId, that.appCategoryId) &&
            Objects.equals(isInstalled, that.isInstalled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        appName,
        appDescription,
        appShort,
        appIcon,
        appPathResource,
        isInstalled,
        appIntall,
            appCategoryId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (appName != null ? "appName=" + appName + ", " : "") +
                (appDescription != null ? "appDescription=" + appDescription + ", " : "") +
                (appShort != null ? "appShort=" + appShort + ", " : "") +
                (appIcon != null ? "appIcon=" + appIcon + ", " : "") +
                (appPathResource != null ? "appPathResource=" + appPathResource + ", " : "") +
                (appIntall != null ? "appIntall=" + appIntall + ", " : "") +
                (appCategoryId != null ? "app_categoryId=" + appCategoryId + ", " : "") +
            "}";
    }

}
