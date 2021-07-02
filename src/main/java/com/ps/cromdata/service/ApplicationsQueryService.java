package com.ps.cromdata.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.ps.cromdata.domain.Applications;
import com.ps.cromdata.domain.*; // for static metamodels
import com.ps.cromdata.repository.ApplicationsRepository;
import com.ps.cromdata.service.dto.ApplicationsCriteria;

/**
 * Service for executing complex queries for {@link Applications} entities in the database.
 * The main input is a {@link ApplicationsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Applications} or a {@link Page} of {@link Applications} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ApplicationsQueryService extends QueryService<Applications> {

    private final Logger log = LoggerFactory.getLogger(ApplicationsQueryService.class);

    private final ApplicationsRepository applicationsRepository;

    public ApplicationsQueryService(ApplicationsRepository applicationsRepository) {
        this.applicationsRepository = applicationsRepository;
    }

    /**
     * Return a {@link List} of {@link Applications} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Applications> findByCriteria(ApplicationsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Applications> specification = createSpecification(criteria);
        return applicationsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Applications} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Applications> findByCriteria(ApplicationsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Applications> specification = createSpecification(criteria);
        return applicationsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ApplicationsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Applications> specification = createSpecification(criteria);
        return applicationsRepository.count(specification);
    }

    /**
     * Function to convert {@link ApplicationsCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Applications> createSpecification(ApplicationsCriteria criteria) {
        Specification<Applications> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Applications_.id));
            }
            if (criteria.getAppName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAppName(), Applications_.appName));
            }
            if (criteria.getAppDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAppDescription(), Applications_.appDescription));
            }
            if (criteria.getAppShort() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAppShort(), Applications_.appShort));
            }
            if (criteria.getAppIcon() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAppIcon(), Applications_.appIcon));
            }
            if (criteria.getAppPathResource() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAppPathResource(), Applications_.appPathResource));
            }
            if (criteria.getAppIntall() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAppIntall(), Applications_.installationLink));
            }
            if (criteria.getIsInstalled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsInstalled(), Applications_.isInstalled));
            }
            if (criteria.getAppCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getAppCategoryId(),
                    root -> root.join(Applications_.appCategory, JoinType.LEFT).get(AppCategory_.id)));
            }
        }
        return specification;
    }
}
