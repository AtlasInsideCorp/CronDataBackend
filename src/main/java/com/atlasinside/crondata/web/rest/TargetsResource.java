package com.atlasinside.crondata.web.rest;

import com.atlasinside.crondata.domain.Targets;
import com.atlasinside.crondata.service.TargetsService;
import com.atlasinside.crondata.web.rest.errors.BadRequestAlertException;
import com.atlasinside.crondata.service.dto.TargetsCriteria;
import com.atlasinside.crondata.service.TargetsQueryService;

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
 * REST controller for managing {@link Targets}.
 */
@RestController
@RequestMapping("/api")
public class TargetsResource {

    private final Logger log = LoggerFactory.getLogger(TargetsResource.class);

    private static final String ENTITY_NAME = "targets";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TargetsService targetsService;

    private final TargetsQueryService targetsQueryService;

    public TargetsResource(TargetsService targetsService, TargetsQueryService targetsQueryService) {
        this.targetsService = targetsService;
        this.targetsQueryService = targetsQueryService;
    }

    /**
     * {@code POST  /targets} : Create a new targets.
     *
     * @param targets the targets to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new targets, or with status {@code 400 (Bad Request)} if the targets has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/targets")
    public ResponseEntity<Targets> createTargets(@Valid @RequestBody Targets targets) throws URISyntaxException {
        log.debug("REST request to save Targets : {}", targets);
        if (targets.getId() != null) {
            throw new BadRequestAlertException("A new targets cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Targets result = targetsService.save(targets);
        return ResponseEntity.created(new URI("/api/targets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /targets} : Updates an existing targets.
     *
     * @param targets the targets to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated targets,
     * or with status {@code 400 (Bad Request)} if the targets is not valid,
     * or with status {@code 500 (Internal Server Error)} if the targets couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/targets")
    public ResponseEntity<Targets> updateTargets(@Valid @RequestBody Targets targets) throws URISyntaxException {
        log.debug("REST request to update Targets : {}", targets);
        if (targets.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Targets result = targetsService.save(targets);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, targets.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /targets} : get all the targets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of targets in body.
     */
    @GetMapping("/targets")
    public ResponseEntity<List<Targets>> getAllTargets(TargetsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Targets by criteria: {}", criteria);
        Page<Targets> page = targetsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /targets/count} : count all the targets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/targets/count")
    public ResponseEntity<Long> countTargets(TargetsCriteria criteria) {
        log.debug("REST request to count Targets by criteria: {}", criteria);
        return ResponseEntity.ok().body(targetsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /targets/:id} : get the "id" targets.
     *
     * @param id the id of the targets to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the targets, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/targets/{id}")
    public ResponseEntity<Targets> getTargets(@PathVariable Long id) {
        log.debug("REST request to get Targets : {}", id);
        Optional<Targets> targets = targetsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(targets);
    }

    /**
     * {@code DELETE  /targets/:id} : delete the "id" targets.
     *
     * @param id the id of the targets to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/targets/{id}")
    public ResponseEntity<Void> deleteTargets(@PathVariable Long id) {
        log.debug("REST request to delete Targets : {}", id);
        targetsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
