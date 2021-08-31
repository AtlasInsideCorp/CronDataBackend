package com.atlasinside.crondata.web.rest;

import com.atlasinside.crondata.domain.AppCategory;
import com.atlasinside.crondata.service.AppCategoryService;
import com.atlasinside.crondata.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link AppCategory}.
 */
@RestController
@RequestMapping("/api")
public class AppCategoryResource {

    private final Logger log = LoggerFactory.getLogger(AppCategoryResource.class);

    private static final String ENTITY_NAME = "appCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppCategoryService appCategoryService;

    public AppCategoryResource(AppCategoryService appCategoryService) {
        this.appCategoryService = appCategoryService;
    }

    /**
     * {@code POST  /app-categories} : Create a new appCategory.
     *
     * @param appCategory the appCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appCategory, or with status {@code 400 (Bad Request)} if the appCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/app-categories")
    public ResponseEntity<AppCategory> createAppCategory(@Valid @RequestBody AppCategory appCategory) throws URISyntaxException {
        log.debug("REST request to save AppCategory : {}", appCategory);
        if (appCategory.getId() != null) {
            throw new BadRequestAlertException("A new appCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppCategory result = appCategoryService.save(appCategory);
        return ResponseEntity.created(new URI("/api/app-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /app-categories} : Updates an existing appCategory.
     *
     * @param appCategory the appCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appCategory,
     * or with status {@code 400 (Bad Request)} if the appCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/app-categories")
    public ResponseEntity<AppCategory> updateAppCategory(@Valid @RequestBody AppCategory appCategory) throws URISyntaxException {
        log.debug("REST request to update AppCategory : {}", appCategory);
        if (appCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AppCategory result = appCategoryService.save(appCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, appCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /app-categories} : get all the appCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appCategories in body.
     */
    @GetMapping("/app-categories")
    public ResponseEntity<List<AppCategory>> getAllAppCategories(Pageable pageable) {
        log.debug("REST request to get a page of AppCategories");
        Page<AppCategory> page = appCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /app-categories/:id} : get the "id" appCategory.
     *
     * @param id the id of the appCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/app-categories/{id}")
    public ResponseEntity<AppCategory> getAppCategory(@PathVariable Long id) {
        log.debug("REST request to get AppCategory : {}", id);
        Optional<AppCategory> appCategory = appCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appCategory);
    }

    /**
     * {@code DELETE  /app-categories/:id} : delete the "id" appCategory.
     *
     * @param id the id of the appCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/app-categories/{id}")
    public ResponseEntity<Void> deleteAppCategory(@PathVariable Long id) {
        log.debug("REST request to delete AppCategory : {}", id);
        appCategoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
