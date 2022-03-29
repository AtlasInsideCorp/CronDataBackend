package com.atlasinside.crondata.web.rest;

import com.atlasinside.crondata.service.ConfigurationSectionQueryService;
import com.atlasinside.crondata.web.rest.errors.BadRequestAlertException;
import com.atlasinside.crondata.domain.ConfigurationSection;
import com.atlasinside.crondata.service.ConfigurationSectionService;
import com.atlasinside.crondata.service.dto.ConfigurationSectionCriteria;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ConfigurationSection.
 */
@RestController
@RequestMapping("/api")
public class ConfigurationSectionResource {
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(ConfigurationSectionResource.class);

    private static final String ENTITY_NAME = "configurationSection";

    private final ConfigurationSectionService configurationSectionService;

    private final ConfigurationSectionQueryService configurationSectionQueryService;

    public ConfigurationSectionResource(ConfigurationSectionService configurationSectionService, ConfigurationSectionQueryService configurationSectionQueryService) {
        this.configurationSectionService = configurationSectionService;
        this.configurationSectionQueryService = configurationSectionQueryService;
    }

    /**
     * POST  /configuration-sections : Create a new configurationSection.
     *
     * @param configurationSection the configurationSection to create
     * @return the ResponseEntity with status 201 (Created) and with body the new configurationSection, or with status 400 (Bad Request) if the configurationSection has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/configuration-sections")
    public ResponseEntity<ConfigurationSection> createConfigurationSection(@RequestBody ConfigurationSection configurationSection) throws URISyntaxException {
        log.debug("REST request to save ConfigurationSection : {}", configurationSection);
        if (configurationSection.getId() != null) {
            throw new BadRequestAlertException("A new configurationSection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigurationSection result = configurationSectionService.save(configurationSection);
        return ResponseEntity.created(new URI("/api/configuration-sections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("CRONDATA", true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /configuration-sections : Updates an existing configurationSection.
     *
     * @param configurationSection the configurationSection to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated configurationSection,
     * or with status 400 (Bad Request) if the configurationSection is not valid,
     * or with status 500 (Internal Server Error) if the configurationSection couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/configuration-sections")
    public ResponseEntity<ConfigurationSection> updateConfigurationSection(@RequestBody ConfigurationSection configurationSection) throws URISyntaxException {
        log.debug("REST request to update ConfigurationSection : {}", configurationSection);
        if (configurationSection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigurationSection result = configurationSectionService.save(configurationSection);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("CRONDATA", true, ENTITY_NAME, configurationSection.getId().toString()))
            .body(result);
    }

    /**
     * GET  /configuration-sections : get all the configurationSections.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of configurationSections in body
     */
    @GetMapping("/configuration-sections")
    public ResponseEntity<List<ConfigurationSection>> getAllConfigurationSections(ConfigurationSectionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ConfigurationSections by criteria: {}", criteria);
        Page<ConfigurationSection> page = configurationSectionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /configuration-sections/count : count all the configurationSections.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the count in body
     */
    @GetMapping("/configuration-sections/count")
    public ResponseEntity<Long> countConfigurationSections(ConfigurationSectionCriteria criteria) {
        log.debug("REST request to count ConfigurationSections by criteria: {}", criteria);
        return ResponseEntity.ok().body(configurationSectionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /configuration-sections/:id : get the "id" configurationSection.
     *
     * @param id the id of the configurationSection to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the configurationSection, or with status 404 (Not Found)
     */
    @GetMapping("/configuration-sections/{id}")
    public ResponseEntity<ConfigurationSection> getConfigurationSection(@PathVariable Long id) {
        log.debug("REST request to get ConfigurationSection : {}", id);
        Optional<ConfigurationSection> configurationSection = configurationSectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configurationSection);
    }

    /**
     * DELETE  /configuration-sections/:id : delete the "id" configurationSection.
     *
     * @param id the id of the configurationSection to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/configuration-sections/{id}")
    public ResponseEntity<Void> deleteConfigurationSection(@PathVariable Long id) {
        log.debug("REST request to delete ConfigurationSection : {}", id);
        configurationSectionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/configuration-sections/checkEmailConfiguration")
    public ResponseEntity<Void> checkEmailConfiguration() {
        final String ctx = ".checkEmailConfiguration";
        try {
            configurationSectionService.checkEmailConfiguration();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            String msg = ctx + ": " + e.getMessage();
            log.error(msg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(
                HeaderUtil.createFailureAlert("CronData", false, "Section", "checkEmailConfiguration", msg)).body(null);
        }
    }
}
