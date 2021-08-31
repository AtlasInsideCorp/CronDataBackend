package com.atlasinside.crondata.service;

import com.atlasinside.crondata.domain.ConfigurationSection_;
import com.atlasinside.crondata.domain.*;
import com.atlasinside.crondata.domain.ConfigurationSection;
import com.atlasinside.crondata.repository.ConfigurationSectionRepository;
import com.atlasinside.crondata.service.dto.ConfigurationSectionCriteria;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for executing complex queries for ConfigurationSection entities in the database.
 * The main input is a {@link ConfigurationSectionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ConfigurationSection} or a {@link Page} of {@link ConfigurationSection} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConfigurationSectionQueryService extends QueryService<ConfigurationSection> {

    private final Logger log = LoggerFactory.getLogger(ConfigurationSectionQueryService.class);

    private final ConfigurationSectionRepository configurationSectionRepository;

    public ConfigurationSectionQueryService(ConfigurationSectionRepository configurationSectionRepository) {
        this.configurationSectionRepository = configurationSectionRepository;
    }

    /**
     * Return a {@link List} of {@link ConfigurationSection} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ConfigurationSection> findByCriteria(ConfigurationSectionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ConfigurationSection> specification = createSpecification(criteria);
        return configurationSectionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ConfigurationSection} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfigurationSection> findByCriteria(ConfigurationSectionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ConfigurationSection> specification = createSpecification(criteria);
        return configurationSectionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConfigurationSectionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ConfigurationSection> specification = createSpecification(criteria);
        return configurationSectionRepository.count(specification);
    }

    /**
     * Function to convert ConfigurationSectionCriteria to a {@link Specification}
     */
    private Specification<ConfigurationSection> createSpecification(ConfigurationSectionCriteria criteria) {
        Specification<ConfigurationSection> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ConfigurationSection_.id));
            }
            if (criteria.getSection() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSection(), ConfigurationSection_.section));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ConfigurationSection_.description));
            }
        }
        return specification;
    }
}
