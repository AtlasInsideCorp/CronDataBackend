package com.ps.cromdata.web.rest;

import com.ps.cromdata.domain.CronParameter;
import com.ps.cromdata.repository.CronParameterRepository;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ps.cromdata.domain.CronParameter}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CronParameterResource {

    private final Logger log = LoggerFactory.getLogger(CronParameterResource.class);

    private static final String ENTITY_NAME = "cronParameter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CronParameterRepository cronParameterRepository;

    public CronParameterResource(CronParameterRepository cronParameterRepository) {
        this.cronParameterRepository = cronParameterRepository;
    }

    /**
     * {@code POST  /cron-parameters} : Create a new cronParameter.
     *
     * @param cronParameter the cronParameter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cronParameter, or with status {@code 400 (Bad Request)} if the cronParameter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cron-parameters")
    public ResponseEntity<CronParameter> createCronParameter(@Valid @RequestBody CronParameter cronParameter) throws URISyntaxException {
        log.debug("REST request to save CronParameter : {}", cronParameter);
        if (cronParameter.getId() != null) {
            throw new BadRequestAlertException("A new cronParameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CronParameter result = cronParameterRepository.save(cronParameter);
        return ResponseEntity.created(new URI("/api/cron-parameters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cron-parameters} : Updates an existing cronParameter.
     *
     * @param cronParameter the cronParameter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cronParameter,
     * or with status {@code 400 (Bad Request)} if the cronParameter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cronParameter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cron-parameters")
    public ResponseEntity<CronParameter> updateCronParameter(@Valid @RequestBody CronParameter cronParameter) throws URISyntaxException {
        log.debug("REST request to update CronParameter : {}", cronParameter);
        if (cronParameter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CronParameter result = cronParameterRepository.save(cronParameter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cronParameter.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cron-parameters} : get all the cronParameters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cronParameters in body.
     */
    @GetMapping("/cron-parameters")
    public ResponseEntity<List<CronParameter>> getAllCronParameters(Pageable pageable) {
        log.debug("REST request to get a page of CronParameters");
        Page<CronParameter> page = cronParameterRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cron-parameters/:id} : get the "id" cronParameter.
     *
     * @param id the id of the cronParameter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cronParameter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cron-parameters/{id}")
    public ResponseEntity<CronParameter> getCronParameter(@PathVariable Long id) {
        log.debug("REST request to get CronParameter : {}", id);
        Optional<CronParameter> cronParameter = cronParameterRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cronParameter);
    }

    /**
     * {@code DELETE  /cron-parameters/:id} : delete the "id" cronParameter.
     *
     * @param id the id of the cronParameter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cron-parameters/{id}")
    public ResponseEntity<Void> deleteCronParameter(@PathVariable Long id) {
        log.debug("REST request to delete CronParameter : {}", id);
        cronParameterRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
