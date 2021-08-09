package com.atlasinside.crondata.service;

import com.atlasinside.crondata.domain.Targets;
import com.atlasinside.crondata.repository.TargetsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Targets}.
 */
@Service
@Transactional
public class TargetsService {

    private final Logger log = LoggerFactory.getLogger(TargetsService.class);

    private final TargetsRepository targetsRepository;
    private final TargetConfigCreator targetConfigCreator;

    public TargetsService(TargetsRepository targetsRepository, TargetConfigCreator targetConfigCreator) {
        this.targetsRepository = targetsRepository;
        this.targetConfigCreator = targetConfigCreator;
    }

    /**
     * Save a targets.
     *
     * @param targets the entity to save.
     * @return the persisted entity.
     */
    public Targets save(Targets targets) {
        log.debug("Request to save Targets : {}", targets);
        Targets result = targetsRepository.save(targets);
        this.targetConfigCreator.generatePromConfig();
        return result;
    }

    /**
     * Get all the targets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Targets> findAll(Pageable pageable) {
        log.debug("Request to get all Targets");
        return targetsRepository.findAll(pageable);
    }


    /**
     * Get one targets by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Targets> findOne(Long id) {
        log.debug("Request to get Targets : {}", id);
        return targetsRepository.findById(id);
    }

    /**
     * Delete the targets by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Targets : {}", id);
        targetsRepository.deleteById(id);
        this.targetConfigCreator.generatePromConfig();
    }


}
