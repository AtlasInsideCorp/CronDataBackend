package com.atlasinside.crondata.service;

import com.atlasinside.crondata.repository.AppCategoryRepository;
import com.atlasinside.crondata.domain.AppCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AppCategory}.
 */
@Service
@Transactional
public class AppCategoryService {

    private final Logger log = LoggerFactory.getLogger(AppCategoryService.class);

    private final AppCategoryRepository appCategoryRepository;

    public AppCategoryService(AppCategoryRepository appCategoryRepository) {
        this.appCategoryRepository = appCategoryRepository;
    }

    /**
     * Save a appCategory.
     *
     * @param appCategory the entity to save.
     * @return the persisted entity.
     */
    public AppCategory save(AppCategory appCategory) {
        log.debug("Request to save AppCategory : {}", appCategory);
        return appCategoryRepository.save(appCategory);
    }

    /**
     * Get all the appCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AppCategory> findAll(Pageable pageable) {
        log.debug("Request to get all AppCategories");
        return appCategoryRepository.findAll(pageable);
    }


    /**
     * Get one appCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppCategory> findOne(Long id) {
        log.debug("Request to get AppCategory : {}", id);
        return appCategoryRepository.findById(id);
    }

    /**
     * Delete the appCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AppCategory : {}", id);
        appCategoryRepository.deleteById(id);
    }
}
