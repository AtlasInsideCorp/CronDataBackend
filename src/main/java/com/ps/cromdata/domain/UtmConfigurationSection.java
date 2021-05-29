package com.ps.cromdata.domain;


import com.ps.cromdata.domain.enums.ModulesNameShort;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A UtmConfigurationSection.
 */
@Entity
@Table(name = "utm_configuration_section")
public class UtmConfigurationSection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "section")
    private String section;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public UtmConfigurationSection section(String section) {
        this.section = section;
        return this;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDescription() {
        return description;
    }

    public UtmConfigurationSection description(String description) {
        this.description = description;
        return this;
    }

}
