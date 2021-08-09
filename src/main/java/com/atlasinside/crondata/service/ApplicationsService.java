package com.atlasinside.crondata.service;

import com.atlasinside.crondata.domain.Applications;
import com.atlasinside.crondata.repository.ApplicationsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Applications}.
 */
@Service
@Transactional
public class ApplicationsService {

    private final Logger log = LoggerFactory.getLogger(ApplicationsService.class);

    private final ApplicationsRepository applicationsRepository;

    public ApplicationsService(ApplicationsRepository applicationsRepository) {
        this.applicationsRepository = applicationsRepository;
    }

    /**
     * Save a applications.
     *
     * @param applications the entity to save.
     * @return the persisted entity.
     */
    public Applications save(Applications applications) {
        log.debug("Request to save Applications : {}", applications);
        return applicationsRepository.save(applications);
    }

    /**
     * Get all the applications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Applications> findAll(Pageable pageable) {
        log.debug("Request to get all Applications");
        return applicationsRepository.findAll(pageable);
    }


    /**
     * Get one applications by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Applications> findOne(Long id) {
        log.debug("Request to get Applications : {}", id);
        return applicationsRepository.findById(id);
    }

    /**
     * Delete the applications by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Applications : {}", id);
        applicationsRepository.deleteById(id);
    }
}
