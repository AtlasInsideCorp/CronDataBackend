package com.atlasinside.crondata.service;

import com.atlasinside.crondata.domain.ConfigurationParameter;
import com.atlasinside.crondata.domain.ConfigurationParameter_;
import com.atlasinside.crondata.repository.ConfigurationParameterRepository;
import com.atlasinside.crondata.service.dto.ConfigurationParameterCriteria;
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
 * Service for executing complex queries for ConfigurationParameter entities in the database.
 * The main input is a {@link ConfigurationParameterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ConfigurationParameter} or a {@link Page} of {@link ConfigurationParameter} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConfigurationParameterQueryService extends QueryService<ConfigurationParameter> {

    private final Logger log = LoggerFactory.getLogger(ConfigurationParameterQueryService.class);

    private final ConfigurationParameterRepository configurationParameterRepository;

    public ConfigurationParameterQueryService(ConfigurationParameterRepository configurationParameterRepository) {
        this.configurationParameterRepository = configurationParameterRepository;
    }

    /**
     * Return a {@link List} of {@link ConfigurationParameter} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ConfigurationParameter> findByCriteria(ConfigurationParameterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ConfigurationParameter> specification = createSpecification(criteria);
        return configurationParameterRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ConfigurationParameter} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfigurationParameter> findByCriteria(ConfigurationParameterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ConfigurationParameter> specification = createSpecification(criteria);
        return configurationParameterRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConfigurationParameterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ConfigurationParameter> specification = createSpecification(criteria);
        return configurationParameterRepository.count(specification);
    }

    /**
     * Function to convert ConfigurationParameterCriteria to a {@link Specification}
     */
    private Specification<ConfigurationParameter> createSpecification(ConfigurationParameterCriteria criteria) {
        Specification<ConfigurationParameter> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ConfigurationParameter_.id));
            }
            if (criteria.getSectionId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSectionId(), ConfigurationParameter_.sectionId));
            }
            if (criteria.getConfParamShort() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfParamShort(), ConfigurationParameter_.confParamShort));
            }
            if (criteria.getConfParamLarge() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfParamLarge(), ConfigurationParameter_.confParamLarge));
            }
            if (criteria.getConfParamDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfParamDescription(), ConfigurationParameter_.confParamDescription));
            }
            if (criteria.getConfParamValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfParamValue(), ConfigurationParameter_.confParamValue));
            }
            if (criteria.getConfParamDatatype() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfParamDatatype(), ConfigurationParameter_.confParamDatatype));
            }
            if (criteria.getConfParamRequired() != null) {
                specification = specification.and(buildSpecification(criteria.getConfParamRequired(), ConfigurationParameter_.confParamRequired));
            }
            if (criteria.getModificationTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModificationTime(), ConfigurationParameter_.modificationTime));
            }
            if (criteria.getModificationUser() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModificationUser(), ConfigurationParameter_.modificationUser));
            }
        }
        return specification;
    }
}
