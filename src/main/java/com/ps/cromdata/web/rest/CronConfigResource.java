package com.ps.cromdata.web.rest;

import com.ps.cromdata.domain.CronConfig;
import com.ps.cromdata.repository.CronConfigRepository;
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
 * REST controller for managing {@link com.ps.cromdata.domain.CronConfig}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CronConfigResource {

    private final Logger log = LoggerFactory.getLogger(CronConfigResource.class);

    private static final String ENTITY_NAME = "cronConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CronConfigRepository cronConfigRepository;

    public CronConfigResource(CronConfigRepository cronConfigRepository) {
        this.cronConfigRepository = cronConfigRepository;
    }

    /**
     * {@code POST  /cron-configs} : Create a new cronConfig.
     *
     * @param cronConfig the cronConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cronConfig, or with status {@code 400 (Bad Request)} if the cronConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cron-configs")
    public ResponseEntity<CronConfig> createCronConfig(@Valid @RequestBody CronConfig cronConfig) throws URISyntaxException {
        log.debug("REST request to save CronConfig : {}", cronConfig);
        if (cronConfig.getId() != null) {
            throw new BadRequestAlertException("A new cronConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CronConfig result = cronConfigRepository.save(cronConfig);
        return ResponseEntity.created(new URI("/api/cron-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cron-configs} : Updates an existing cronConfig.
     *
     * @param cronConfig the cronConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cronConfig,
     * or with status {@code 400 (Bad Request)} if the cronConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cronConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cron-configs")
    public ResponseEntity<CronConfig> updateCronConfig(@Valid @RequestBody CronConfig cronConfig) throws URISyntaxException {
        log.debug("REST request to update CronConfig : {}", cronConfig);
        if (cronConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CronConfig result = cronConfigRepository.save(cronConfig);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cronConfig.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cron-configs} : get all the cronConfigs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cronConfigs in body.
     */
    @GetMapping("/cron-configs")
    public ResponseEntity<List<CronConfig>> getAllCronConfigs(Pageable pageable) {
        log.debug("REST request to get a page of CronConfigs");
        Page<CronConfig> page = cronConfigRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cron-configs/:id} : get the "id" cronConfig.
     *
     * @param id the id of the cronConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cronConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cron-configs/{id}")
    public ResponseEntity<CronConfig> getCronConfig(@PathVariable Long id) {
        log.debug("REST request to get CronConfig : {}", id);
        Optional<CronConfig> cronConfig = cronConfigRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cronConfig);
    }

    /**
     * {@code DELETE  /cron-configs/:id} : delete the "id" cronConfig.
     *
     * @param id the id of the cronConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cron-configs/{id}")
    public ResponseEntity<Void> deleteCronConfig(@PathVariable Long id) {
        log.debug("REST request to delete CronConfig : {}", id);
        cronConfigRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
