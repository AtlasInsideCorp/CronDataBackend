package com.ps.cromdata.service;

import com.ps.cromdata.domain.Alerts;
import com.ps.cromdata.repository.AlertsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Alerts}.
 */
@Service
@Transactional
public class AlertsService {

    private final Logger log = LoggerFactory.getLogger(AlertsService.class);

    private final AlertsRepository alertsRepository;

    public AlertsService(AlertsRepository alertsRepository) {
        this.alertsRepository = alertsRepository;
    }

    /**
     * Save a alerts.
     *
     * @param alerts the entity to save.
     * @return the persisted entity.
     */
    public Alerts save(Alerts alerts) {
        log.debug("Request to save Alerts : {}", alerts);
        return alertsRepository.save(alerts);
    }

    /**
     * Get all the alerts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Alerts> findAll(Pageable pageable) {
        log.debug("Request to get all Alerts");
        return alertsRepository.findAll(pageable);
    }


    /**
     * Get one alerts by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Alerts> findOne(Long id) {
        log.debug("Request to get Alerts : {}", id);
        return alertsRepository.findById(id);
    }

    /**
     * Delete the alerts by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Alerts : {}", id);
        alertsRepository.deleteById(id);
    }

}
