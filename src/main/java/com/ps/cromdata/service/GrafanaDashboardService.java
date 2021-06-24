package com.ps.cromdata.service;

import com.appnexus.grafana.client.GrafanaClient;
import com.ps.cromdata.config.GrafanaConnection;
import org.json.simple.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

import java.io.FileReader;

import static com.ps.cromdata.config.Constants.GRAFANA_URL;

@Service
public class GrafanaDashboardService {
    GrafanaClient grafanaConnection = GrafanaConnection.getInstance();

    public GrafanaDashboardService() {
    }

    public void saveDashboard(Object grafanaRawDashboard) {
        ResponseEntity<Object> result = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            RestTemplate restTemplate = new RestTemplate();
            String uri = GRAFANA_URL + "/api/dashboards/db ";
            HttpEntity<?> entity = new HttpEntity<>(headers);
            result = restTemplate.postForEntity(uri, grafanaRawDashboard, Object.class);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
