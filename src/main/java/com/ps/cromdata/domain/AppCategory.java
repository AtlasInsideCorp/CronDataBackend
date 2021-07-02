package com.ps.cromdata.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A AppCategory.
 */
@Entity
@Table(name = "app_category")
public class AppCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "category_name", nullable = false, unique = true)
    private String categoryName;

    @Column(name = "category_description")
    private String categoryDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public AppCategory category_name(String category_name) {
        this.categoryName = category_name;
        return this;
    }

    public void setCategoryName(String category_name) {
        this.categoryName = category_name;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public AppCategory category_description(String category_description) {
        this.categoryDescription = category_description;
        return this;
    }

    public void setCategoryDescription(String category_description) {
        this.categoryDescription = category_description;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppCategory)) {
            return false;
        }
        return id != null && id.equals(((AppCategory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppCategory{" +
            "id=" + getId() +
            ", category_name='" + getCategoryName() + "'" +
            ", category_description='" + getCategoryDescription() + "'" +
            "}";
    }
}
