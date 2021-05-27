package com.ps.cromdata.web.rest;

import com.ps.cromdata.domain.Alerts;
import com.ps.cromdata.service.AlertsService;
import com.ps.cromdata.service.dto.Alert;
import com.ps.cromdata.service.dto.AlertManager;
import com.ps.cromdata.web.rest.errors.BadRequestAlertException;
import com.ps.cromdata.service.dto.AlertsCriteria;
import com.ps.cromdata.service.AlertsQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import liquibase.pro.packaged.A;
import org.json.simple.JSONObject;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ps.cromdata.domain.Alerts}.
 */
@RestController
@RequestMapping("/api")
public class AlertsResource {
    public static final String FIRING_ALERT = "firing";
    public static final String ALERT_NAME = "alertname";
    public static final String ALERT_LABELS = "labels";
    public static final String ALERT_STATUS = "status";
    public static final String ALERTS = "alerts";
    public static final String STREAM_NAME = "stream_name";
    public static final String APPLICATION_NAME = "application_name";

    private final Logger log = LoggerFactory.getLogger(AlertsResource.class);

    private static final String ENTITY_NAME = "alerts";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlertsService alertsService;

    private final AlertsQueryService alertsQueryService;

    public AlertsResource(AlertsService alertsService, AlertsQueryService alertsQueryService) {
        this.alertsService = alertsService;
        this.alertsQueryService = alertsQueryService;
    }

    /**
     * {@code POST  /alerts} : Create a new alerts.
     *
     * @param payload the alerts generated by AlertManager.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alerts, or with status {@code 400 (Bad Request)} if the alerts has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/alerts")
    public ResponseEntity<String> createAlerts(@RequestBody AlertManager payload) throws URISyntaxException {
        log.debug("REST request to save Alerts");
        List<Alert> alertsIncoming = payload.getAlerts();
        for (Alert alert : alertsIncoming) {
            Alerts alerts = new Alerts();
            alerts.setName(alert.getLabels().get("alertname"));
            String description = alert.getAnnotations().get("description")
                .replace("\n", "")
                .replace("VALUE =", ", current value")
                .replace(" LABELS = map[", " in ")
                .replace("job:", "for job ")
                .replace("__name__:windows_exporter_collector_success collector:", "")
                .replace(":", " ")
                .replace("]", ".");
            alerts.setDescription(description);
            alerts.setReceivedAt(Instant.now());
            alerts.setSummary(alert.getAnnotations().get("summary"));
            alerts.setReaded(false);
            alerts.setSeverity(alert.getLabels().get("severity"));
            alerts.setStatus(alert.getStatus());
            alertsService.save(alerts);
        }
        return ResponseEntity.ok().body("OK");
    }


    /**
     * {@code PUT  /alerts} : Updates an existing alerts.
     *
     * @param alerts the alerts to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alerts,
     * or with status {@code 400 (Bad Request)} if the alerts is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alerts couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/alerts")
    public ResponseEntity<Alerts> updateAlerts(@RequestBody Alerts alerts) throws URISyntaxException {
        log.debug("REST request to update Alerts : {}", alerts);
        if (alerts.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Alerts result = alertsService.save(alerts);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, alerts.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /alerts} : get all the alerts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alerts in body.
     */
    @GetMapping("/alerts")
    public ResponseEntity<List<Alerts>> getAllAlerts(AlertsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Alerts by criteria: {}", criteria);
        Page<Alerts> page = alertsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /alerts/count} : count all the alerts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/alerts/count")
    public ResponseEntity<Long> countAlerts(AlertsCriteria criteria) {
        log.debug("REST request to count Alerts by criteria: {}", criteria);
        return ResponseEntity.ok().body(alertsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /alerts/:id} : get the "id" alerts.
     *
     * @param id the id of the alerts to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alerts, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/alerts/{id}")
    public ResponseEntity<Alerts> getAlerts(@PathVariable Long id) {
        log.debug("REST request to get Alerts : {}", id);
        Optional<Alerts> alerts = alertsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alerts);
    }

    /**
     * {@code DELETE  /alerts/:id} : delete the "id" alerts.
     *
     * @param id the id of the alerts to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/alerts/{id}")
    public ResponseEntity<Void> deleteAlerts(@PathVariable Long id) {
        log.debug("REST request to delete Alerts : {}", id);
        alertsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

}
