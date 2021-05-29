package com.ps.cromdata.web.rest;


import com.ps.cromdata.domain.UtmConfigurationParameter;
import com.ps.cromdata.service.UtmConfigurationParameterQueryService;
import com.ps.cromdata.service.UtmConfigurationParameterService;
import com.ps.cromdata.service.dto.UtmConfigurationParameterCriteria;
import com.ps.cromdata.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing UtmConfigurationParameter.
 */
@RestController
@RequestMapping("/api")
public class UtmConfigurationParameterResource {
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(UtmConfigurationParameterResource.class);

    private static final String ENTITY_NAME = "utmConfigurationParameter";
    private static final String CLASSNAME = "UtmConfigurationParameterResource";

    private final UtmConfigurationParameterService utmConfigurationParameterService;

    private final UtmConfigurationParameterQueryService utmConfigurationParameterQueryService;


    public UtmConfigurationParameterResource(UtmConfigurationParameterService utmConfigurationParameterService,
                                             UtmConfigurationParameterQueryService utmConfigurationParameterQueryService) {
        this.utmConfigurationParameterService = utmConfigurationParameterService;
        this.utmConfigurationParameterQueryService = utmConfigurationParameterQueryService;
    }

    /**
     * POST  /utm-configuration-parameters : Create a new utmConfigurationParameter.
     *
     * @param utmConfigurationParameter the utmConfigurationParameter to create
     * @return the ResponseEntity with status 201 (Created) and with body the new utmConfigurationParameter, or with status 400 (Bad Request) if the utmConfigurationParameter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/utm-configuration-parameters")
    public ResponseEntity<UtmConfigurationParameter> createUtmConfigurationParameter(@Valid @RequestBody UtmConfigurationParameter utmConfigurationParameter) throws URISyntaxException {
        final String ctx = CLASSNAME + ".createUtmConfigurationParameter";
        try {
            if (utmConfigurationParameter.getId() != null)
                throw new BadRequestAlertException("A new utmConfigurationParameter cannot already have an ID", ENTITY_NAME, "idexists");

            UtmConfigurationParameter result = utmConfigurationParameterService.save(utmConfigurationParameter);
            return ResponseEntity.created(new URI("/api/utm-configuration-parameters/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (Exception e) {
            String msg = ctx + ": " + e.getMessage();
            log.error(msg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * PUT  /utm-configuration-parameters : Updates an existing utmConfigurationParameter.
     *
     * @param parameters the utmConfigurationParameter to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated utmConfigurationParameter,
     * or with status 400 (Bad Request) if the utmConfigurationParameter is not valid,
     * or with status 500 (Internal Server Error) if the utmConfigurationParameter couldn't be updated
     */
    @PutMapping("/utm-configuration-parameters")
    public ResponseEntity<Void> updateConfigurationParameters(@Valid @RequestBody List<UtmConfigurationParameter> parameters) {
        final String ctx = CLASSNAME + ".updateUtmConfigurationParameter";
        try {
            Assert.notEmpty(parameters, "There isn't any parameter to update");
            utmConfigurationParameterService.saveAll(parameters);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            String msg = ctx + ": " + e.getMessage();
            log.error(msg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET  /utm-configuration-parameters : get all the utmConfigurationParameters.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of utmConfigurationParameters in body
     */
    @GetMapping("/utm-configuration-parameters")
    public ResponseEntity<List<UtmConfigurationParameter>> getAllUtmConfigurationParameters(UtmConfigurationParameterCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UtmConfigurationParameters by criteria: {}", criteria);
        Page<UtmConfigurationParameter> page = utmConfigurationParameterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /utm-configuration-parameters/count : count all the utmConfigurationParameters.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the count in body
     */
    @GetMapping("/utm-configuration-parameters/count")
    public ResponseEntity<Long> countUtmConfigurationParameters(UtmConfigurationParameterCriteria criteria) {
        log.debug("REST request to count UtmConfigurationParameters by criteria: {}", criteria);
        return ResponseEntity.ok().body(utmConfigurationParameterQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /utm-configuration-parameters/:id : get the "id" utmConfigurationParameter.
     *
     * @param id the id of the utmConfigurationParameter to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the utmConfigurationParameter, or with status 404 (Not Found)
     */
    @GetMapping("/utm-configuration-parameters/{id}")
    public ResponseEntity<UtmConfigurationParameter> getUtmConfigurationParameter(@PathVariable Long id) {
        log.debug("REST request to get UtmConfigurationParameter : {}", id);
        Optional<UtmConfigurationParameter> utmConfigurationParameter = utmConfigurationParameterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(utmConfigurationParameter);
    }

    /**
     * DELETE  /utm-configuration-parameters/:id : delete the "id" utmConfigurationParameter.
     *
     * @param id the id of the utmConfigurationParameter to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/utm-configuration-parameters/{id}")
    public ResponseEntity<Void> deleteUtmConfigurationParameter(@PathVariable Long id) {
        log.debug("REST request to delete UtmConfigurationParameter : {}", id);
        utmConfigurationParameterService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
