package com.atlasinside.crondata.web.rest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link PingResource}.
 */
@RestController
@RequestMapping("/api")
public class PingResource {
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PingResource() {
    }


    /**
     * {@code GET  /ping} : Ping panel to check if is UP.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and steing UP.
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok().body("UP");
    }


}
