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

import com.ps.cromdata.domain.Alerts;
import com.ps.cromdata.domain.*; // for static metamodels
import com.ps.cromdata.repository.AlertsRepository;
import com.ps.cromdata.service.dto.AlertsCriteria;

/**
 * Service for executing complex queries for {@link Alerts} entities in the database.
 * The main input is a {@link AlertsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Alerts} or a {@link Page} of {@link Alerts} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlertsQueryService extends QueryService<Alerts> {

    private final Logger log = LoggerFactory.getLogger(AlertsQueryService.class);

    private final AlertsRepository alertsRepository;

    public AlertsQueryService(AlertsRepository alertsRepository) {
        this.alertsRepository = alertsRepository;
    }

    /**
     * Return a {@link List} of {@link Alerts} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Alerts> findByCriteria(AlertsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Alerts> specification = createSpecification(criteria);
        return alertsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Alerts} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Alerts> findByCriteria(AlertsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Alerts> specification = createSpecification(criteria);
        return alertsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlertsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Alerts> specification = createSpecification(criteria);
        return alertsRepository.count(specification);
    }

    /**
     * Function to convert {@link AlertsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Alerts> createSpecification(AlertsCriteria criteria) {
        Specification<Alerts> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Alerts_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Alerts_.name));
            }
            if (criteria.getSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSummary(), Alerts_.summary));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Alerts_.description));
            }
            if (criteria.getSeverity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSeverity(), Alerts_.severity));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Alerts_.status));
            }
            if (criteria.getReaded() != null) {
                specification = specification.and(buildSpecification(criteria.getReaded(), Alerts_.readed));
            }
            if (criteria.getReceivedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReceivedAt(), Alerts_.receivedAt));
            }
        }
        return specification;
    }
}
