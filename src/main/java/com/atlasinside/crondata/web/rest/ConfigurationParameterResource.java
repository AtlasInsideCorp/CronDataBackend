package com.atlasinside.crondata.web.rest;


import com.atlasinside.crondata.service.ConfigurationParameterQueryService;
import com.atlasinside.crondata.service.ConfigurationParameterService;
import com.atlasinside.crondata.service.dto.ConfigurationParameterCriteria;
import com.atlasinside.crondata.web.rest.errors.BadRequestAlertException;
import com.atlasinside.crondata.domain.ConfigurationParameter;
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
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ConfigurationParameter.
 */
@RestController
@RequestMapping("/api")
public class ConfigurationParameterResource {
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(ConfigurationParameterResource.class);

    private static final String ENTITY_NAME = "ConfigurationParameter";
    private static final String CLASSNAME = "ConfigurationParameterResource";

    private final ConfigurationParameterService configurationParameterService;

    private final ConfigurationParameterQueryService configurationParameterQueryService;


    public ConfigurationParameterResource(ConfigurationParameterService configurationParameterService,
                                          ConfigurationParameterQueryService configurationParameterQueryService) {
        this.configurationParameterService = configurationParameterService;
        this.configurationParameterQueryService = configurationParameterQueryService;
    }

    /**
     * POST  /configuration-parameters : Create a new configurationParameter.
     *
     * @param configurationParameter the configurationParameter to create
     * @return the ResponseEntity with status 201 (Created) and with body the new configurationParameter, or with status 400 (Bad Request) if the configurationParameter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/configuration-parameters")
    public ResponseEntity<ConfigurationParameter> createConfigurationParameter(@Valid @RequestBody ConfigurationParameter configurationParameter) throws URISyntaxException {
        final String ctx = CLASSNAME + ".createConfigurationParameter";
        try {
            if (configurationParameter.getId() != null)
                throw new BadRequestAlertException("A new configurationParameter cannot already have an ID", ENTITY_NAME, "idexists");

            ConfigurationParameter result = configurationParameterService.save(configurationParameter);
            return ResponseEntity.created(new URI("/api/configuration-parameters/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (Exception e) {
            String msg = ctx + ": " + e.getMessage();
            log.error(msg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * PUT  /configuration-parameters : Updates an existing configurationParameter.
     *
     * @param parameters the configurationParameter to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated configurationParameter,
     * or with status 400 (Bad Request) if the configurationParameter is not valid,
     * or with status 500 (Internal Server Error) if the configurationParameter couldn't be updated
     */
    @PutMapping("/configuration-parameters")
    public ResponseEntity<Void> updateConfigurationParameters(@Valid @RequestBody List<ConfigurationParameter> parameters) {
        final String ctx = CLASSNAME + ".updateConfigurationParameter";
        try {
            Assert.notEmpty(parameters, "There isn't any parameter to update");
            configurationParameterService.saveAll(parameters);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            String msg = ctx + ": " + e.getMessage();
            log.error(msg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET  /configuration-parameters : get all the configurationParameters.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of configurationParameters in body
     */
    @GetMapping("/configuration-parameters")
    public ResponseEntity<List<ConfigurationParameter>> getAllConfigurationParameters(ConfigurationParameterCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ConfigurationParameters by criteria: {}", criteria);
        Page<ConfigurationParameter> page = configurationParameterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /configuration-parameters/count : count all the configurationParameters.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the count in body
     */
    @GetMapping("/configuration-parameters/count")
    public ResponseEntity<Long> countConfigurationParameters(ConfigurationParameterCriteria criteria) {
        log.debug("REST request to count ConfigurationParameters by criteria: {}", criteria);
        return ResponseEntity.ok().body(configurationParameterQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /configuration-parameters/:id : get the "id" configurationParameter.
     *
     * @param id the id of the configurationParameter to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the configurationParameter, or with status 404 (Not Found)
     */
    @GetMapping("/configuration-parameters/{id}")
    public ResponseEntity<ConfigurationParameter> getConfigurationParameter(@PathVariable Long id) {
        log.debug("REST request to get ConfigurationParameter : {}", id);
        Optional<ConfigurationParameter> configurationParameter = configurationParameterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configurationParameter);
    }

    /**
     * DELETE  /configuration-parameters/:id : delete the "id" configurationParameter.
     *
     * @param id the id of the configurationParameter to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/configuration-parameters/{id}")
    public ResponseEntity<Void> deleteConfigurationParameter(@PathVariable Long id) {
        log.debug("REST request to delete ConfigurationParameter : {}", id);
        configurationParameterService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
