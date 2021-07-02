package com.ps.cromdata.web.rest;

import com.ps.cromdata.domain.GrafanaDashboardResponse;
import com.ps.cromdata.domain.Targets;
import com.ps.cromdata.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import static com.ps.cromdata.config.Constants.GRAFANA_URL;
import static com.ps.cromdata.config.Constants.PROMETHEUS_URL;

/**
 * REST controller for Prometheus orchestration.
 */
@RestController
@RequestMapping("/api")
public class PrometheusResource {

    private final Logger log = LoggerFactory.getLogger(PrometheusResource.class);

    private static final String ENTITY_NAME = "prom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PrometheusResource() {
    }

    /**
     * {@code POST  /prometheus/reload} : Reload Prometheus
     *
     * @return the {@link ResponseEntity} with status {@code 200 (Get))} and with body the new targets, or with status {@code 400 (Bad Request)} if the targets has already an ID.
     */
    @PostMapping("/prometheus/reload")
    public ResponseEntity<String> reloadConfig() {
        try {
            log.debug("REST request to reload Prometheus configuration");
            ResponseEntity<String> result;
            RestTemplate restTemplate = new RestTemplate();
            String uri = PROMETHEUS_URL + "-/reload";
            result = restTemplate.postForEntity(uri, null, String.class);
            return ResponseEntity.ok().body(result.getBody());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Bad request on reload prometheus");
        }
    }

    /**
     * {@code GET  /prometheus/healthy} : Get Prometheus targets
     *
     * @return the {@link ResponseEntity} with status {@code 200 (Get)} and with body the new targets, or with status {@code 400 (Bad Request)} if the targets has already an ID.
     */
    @GetMapping("/prometheus/healthy")
    public ResponseEntity<Object> getTHealth() {
        try {
            log.debug("REST request to get healthy Prometheus configuration");
            ResponseEntity<Object> result;
            RestTemplate restTemplate = new RestTemplate();
            String uri = PROMETHEUS_URL + "-/healthy";
            result = restTemplate.getForEntity(uri, Object.class);
            return ResponseEntity.ok().body(result.getBody());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Bad request on get targets prometheus");
        }
    }

    /**
     * {@code GET  /prometheus/rules} : Get Prometheus rules
     *
     * @return the {@link ResponseEntity} with status {@code 200 (Get)} and with body the new targets, or with status {@code 400 (Bad Request)} if the targets has already an ID.
     */
    @GetMapping("/prometheus/rules")
    public ResponseEntity<Object> getRules() {
        try {
            log.debug("REST request to reload Prometheus configuration");
            ResponseEntity<Object> result;
            RestTemplate restTemplate = new RestTemplate();
            String uri = PROMETHEUS_URL + "api/v1/rules";
            result = restTemplate.getForEntity(uri, Object.class);
            return ResponseEntity.ok().body(result.getBody());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Bad request on get rules prometheus");
        }
    }

    /**
     * {@code GET  /prometheus/targets} : Get Prometheus targets
     *
     * @return the {@link ResponseEntity} with status {@code 200 (Get)} and with body the new targets, or with status {@code 400 (Bad Request)} if the targets has already an ID.
     */
    @GetMapping("/prometheus/targets")
    public ResponseEntity<Object> getTargets() {
        try {
            log.debug("REST request to reload Prometheus configuration");
            ResponseEntity<Object> result;
            RestTemplate restTemplate = new RestTemplate();
            String uri = PROMETHEUS_URL + "api/v1/targets";
            result = restTemplate.getForEntity(uri, Object.class);
            return ResponseEntity.ok().body(result.getBody());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Bad request on get targets prometheus");
        }
    }

}
