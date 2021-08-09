package com.atlasinside.crondata.service.dto;

import com.atlasinside.crondata.domain.enums.ModulesNameShort;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;

/**
 * Criteria class for the ConfigurationSection entity. This class is used in ConfigurationSectionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /configuration-sections?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ConfigurationSectionCriteria implements Serializable {

    /**
     * Class for filtering ModulesNameShort
     */
    public static class ModulesNameShortFilter extends Filter<ModulesNameShort> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter section;
    private StringFilter description;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSection() {
        return section;
    }

    public void setSection(StringFilter section) {
        this.section = section;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

}
