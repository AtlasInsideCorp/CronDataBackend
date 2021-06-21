package com.ps.cromdata.web.rest;

import com.ps.cromdata.domain.Applications;
import com.ps.cromdata.service.ApplicationsService;
import com.ps.cromdata.web.rest.errors.BadRequestAlertException;
import com.ps.cromdata.service.dto.ApplicationsCriteria;
import com.ps.cromdata.service.ApplicationsQueryService;

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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ps.cromdata.domain.Applications}.
 */
@RestController
@RequestMapping("/api")
public class ApplicationsResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationsResource.class);

    private static final String ENTITY_NAME = "applications";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationsService applicationsService;

    private final ApplicationsQueryService applicationsQueryService;

    public ApplicationsResource(ApplicationsService applicationsService, ApplicationsQueryService applicationsQueryService) {
        this.applicationsService = applicationsService;
        this.applicationsQueryService = applicationsQueryService;
    }

    /**
     * {@code POST  /applications} : Create a new applications.
     *
     * @param applications the applications to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applications, or with status {@code 400 (Bad Request)} if the applications has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/applications")
    public ResponseEntity<Applications> createApplications(@Valid @RequestBody Applications applications) throws URISyntaxException {
        log.debug("REST request to save Applications : {}", applications);
        if (applications.getId() != null) {
            throw new BadRequestAlertException("A new applications cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Applications result = applicationsService.save(applications);
        return ResponseEntity.created(new URI("/api/applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /applications} : Updates an existing applications.
     *
     * @param applications the applications to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applications,
     * or with status {@code 400 (Bad Request)} if the applications is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applications couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/applications")
    public ResponseEntity<Applications> updateApplications(@Valid @RequestBody Applications applications) throws URISyntaxException {
        log.debug("REST request to update Applications : {}", applications);
        if (applications.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Applications result = applicationsService.save(applications);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applications.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /applications} : get all the applications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applications in body.
     */
    @GetMapping("/applications")
    public ResponseEntity<List<Applications>> getAllApplications(ApplicationsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Applications by criteria: {}", criteria);
        Page<Applications> page = applicationsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /applications/count} : count all the applications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/applications/count")
    public ResponseEntity<Long> countApplications(ApplicationsCriteria criteria) {
        log.debug("REST request to count Applications by criteria: {}", criteria);
        return ResponseEntity.ok().body(applicationsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /applications/:id} : get the "id" applications.
     *
     * @param id the id of the applications to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applications, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/applications/{id}")
    public ResponseEntity<Applications> getApplications(@PathVariable Long id) {
        log.debug("REST request to get Applications : {}", id);
        Optional<Applications> applications = applicationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(applications);
    }

    /**
     * {@code DELETE  /applications/:id} : delete the "id" applications.
     *
     * @param id the id of the applications to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/applications/{id}")
    public ResponseEntity<Void> deleteApplications(@PathVariable Long id) {
        log.debug("REST request to delete Applications : {}", id);
        applicationsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
