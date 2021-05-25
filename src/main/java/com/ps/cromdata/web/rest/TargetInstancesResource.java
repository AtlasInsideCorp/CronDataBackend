package com.ps.cromdata.web.rest;

import com.ps.cromdata.domain.TargetInstances;
import com.ps.cromdata.repository.TargetInstancesRepository;
import com.ps.cromdata.service.TargetService;
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
 * REST controller for managing {@link com.ps.cromdata.domain.TargetInstances}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TargetInstancesResource {

    private final Logger log = LoggerFactory.getLogger(TargetInstancesResource.class);

    private static final String ENTITY_NAME = "targetInstances";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TargetInstancesRepository targetInstancesRepository;
    private final TargetService targetService;

    public TargetInstancesResource(TargetInstancesRepository targetInstancesRepository,
                                   TargetService targetService) {
        this.targetInstancesRepository = targetInstancesRepository;
        this.targetService = targetService;
    }

    /**
     * {@code POST  /target-instances} : Create a new targetInstances.
     *
     * @param targetInstances the targetInstances to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new targetInstances, or with status {@code 400 (Bad Request)} if the targetInstances has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/target-instances")
    public ResponseEntity<TargetInstances> createTargetInstances(@Valid @RequestBody TargetInstances targetInstances) throws URISyntaxException {
        log.debug("REST request to save TargetInstances : {}", targetInstances);
        if (targetInstances.getId() != null) {
            throw new BadRequestAlertException("A new targetInstances cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TargetInstances result = targetInstancesRepository.save(targetInstances);
        this.targetService.generatePromConfig();
        return ResponseEntity.created(new URI("/api/target-instances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /target-instances} : Updates an existing targetInstances.
     *
     * @param targetInstances the targetInstances to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated targetInstances,
     * or with status {@code 400 (Bad Request)} if the targetInstances is not valid,
     * or with status {@code 500 (Internal Server Error)} if the targetInstances couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/target-instances")
    public ResponseEntity<TargetInstances> updateTargetInstances(@Valid @RequestBody TargetInstances targetInstances) throws URISyntaxException {
        log.debug("REST request to update TargetInstances : {}", targetInstances);
        if (targetInstances.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TargetInstances result = targetInstancesRepository.save(targetInstances);
        this.targetService.generatePromConfig();
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, targetInstances.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /target-instances} : get all the targetInstances.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of targetInstances in body.
     */
    @GetMapping("/target-instances")
    public ResponseEntity<List<TargetInstances>> getAllTargetInstances(Pageable pageable) {
        log.debug("REST request to get a page of TargetInstances");
        Page<TargetInstances> page = targetInstancesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

//    @GetMapping("/target-instances/valid")
//    public ResponseEntity<List<TargetInstances>> checkTarget(@PathVariable String target,
//                                                             @PathVariable String job,
//                                                             @PathVariable Integer port) {
//        log.debug("REST request to get a page of TargetInstances");
//
////        Page<TargetInstances> page = targetInstancesRepository.findAll(pageable);
////        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//        return ResponseEntity.ok();
//    }


    /**
     * {@code GET  /target-instances/:id} : get the "id" targetInstances.
     *
     * @param id the id of the targetInstances to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the targetInstances, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/target-instances/{id}")
    public ResponseEntity<TargetInstances> getTargetInstances(@PathVariable Long id) {
        log.debug("REST request to get TargetInstances : {}", id);
        Optional<TargetInstances> targetInstances = targetInstancesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(targetInstances);
    }

    /**
     * {@code DELETE  /target-instances/:id} : delete the "id" targetInstances.
     *
     * @param id the id of the targetInstances to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/target-instances/{id}")
    public ResponseEntity<Void> deleteTargetInstances(@PathVariable Long id) {
        log.debug("REST request to delete TargetInstances : {}", id);
        targetInstancesRepository.deleteById(id);
        this.targetService.generatePromConfig();
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
