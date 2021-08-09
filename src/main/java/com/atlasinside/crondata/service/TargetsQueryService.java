package com.atlasinside.crondata.service;

import java.util.List;

import com.atlasinside.crondata.domain.Targets;
import com.atlasinside.crondata.domain.Targets_;
import com.atlasinside.crondata.repository.TargetsRepository;
import com.atlasinside.crondata.service.dto.TargetsCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.atlasinside.crondata.domain.*; // for static metamodels


/**
 * Service for executing complex queries for {@link Targets} entities in the database.
 * The main input is a {@link TargetsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Targets} or a {@link Page} of {@link Targets} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TargetsQueryService extends QueryService<Targets> {

    private final Logger log = LoggerFactory.getLogger(TargetsQueryService.class);

    private final TargetsRepository targetsRepository;

    public TargetsQueryService(TargetsRepository targetsRepository) {
        this.targetsRepository = targetsRepository;
    }

    /**
     * Return a {@link List} of {@link Targets} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Targets> findByCriteria(TargetsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Targets> specification = createSpecification(criteria);
        return targetsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Targets} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Targets> findByCriteria(TargetsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Targets> specification = createSpecification(criteria);
        return targetsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TargetsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Targets> specification = createSpecification(criteria);
        return targetsRepository.count(specification);
    }

    /**
     * Function to convert {@link TargetsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Targets> createSpecification(TargetsCriteria criteria) {
        Specification<Targets> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Targets_.id));
            }
            if (criteria.getHost() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHost(), Targets_.host));
            }
            if (criteria.getPort() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPort(), Targets_.port));
            }
            if (criteria.getJob() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJob(), Targets_.job));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Targets_.description));
            }
        }
        return specification;
    }
}
