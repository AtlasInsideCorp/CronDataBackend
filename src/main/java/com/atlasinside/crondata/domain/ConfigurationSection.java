package com.atlasinside.crondata.domain;


import javax.persistence.*;
import java.io.Serializable;

/**
 * A ConfigurationSection.
 */
@Entity
@Table(name = "configuration_section")
public class ConfigurationSection implements Serializable {

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

    public ConfigurationSection section(String section) {
        this.section = section;
        return this;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDescription() {
        return description;
    }

    public ConfigurationSection description(String description) {
        this.description = description;
        return this;
    }

}
