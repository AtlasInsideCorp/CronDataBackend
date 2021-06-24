package com.ps.cromdata.service;

import com.ps.cromdata.domain.GrafanaDashboardResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static com.ps.cromdata.config.Constants.GRAFANA_URL;
import static com.ps.cromdata.config.Constants.PROMETHEUS_PATH;

@Service
public class ApplicationImportService {

    public ApplicationImportService() {
    }

    public String saveDashboard(Object grafanaRawDashboard) {
        ResponseEntity<GrafanaDashboardResponse> result = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            RestTemplate restTemplate = new RestTemplate();
            String uri = GRAFANA_URL + "/api/dashboards/db ";
            HttpEntity<?> entity = new HttpEntity<>(headers);
            result = restTemplate.postForEntity(uri, grafanaRawDashboard, GrafanaDashboardResponse.class);
            return Objects.requireNonNull(result.getBody()).uid;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void deleteDashboard(String uid) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String uri = GRAFANA_URL + "/api/dashboards/uid/" + uid;
            restTemplate.delete(uri, Object.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void deleteRules(String sourceDirectoryLocation)
        throws IOException {
        String destinationDirectoryLocation = PROMETHEUS_PATH + "alerts/";
        String finalSourceDirectoryLocation = processPath(sourceDirectoryLocation);
        Files.walk(Paths.get(sourceDirectoryLocation))
            .forEach(source -> {
                Path destination = Paths.get(destinationDirectoryLocation, source.toString()
                    .substring(finalSourceDirectoryLocation.length()));
                try {
                    Files.deleteIfExists(destination);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

    private String processPath(String location) {
        if (location.startsWith("/")) {
            location = location.substring(1, location.length());
        }
        return location;
    }


    public void copyDirectory(String sourceDirectoryLocation)
        throws IOException {
        String destinationDirectoryLocation = PROMETHEUS_PATH + "alerts/";
        String finalSourceDirectoryLocation = processPath(sourceDirectoryLocation);
        Files.walk(Paths.get(sourceDirectoryLocation))
            .forEach(source -> {
                Path destination = Paths.get(destinationDirectoryLocation, source.toString()
                    .substring(finalSourceDirectoryLocation.length()));
                try {
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

//    public void copyFolder(Path src, Path dest) throws IOException {
//        try (Stream<Path> stream = Files.walk(src)) {
//            stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
//        }
//    }

    public void copy(String path) {
        Path alert = Paths.get(path);
        File dest = new File(PROMETHEUS_PATH + "alerts/" + alert.getFileName());
        try {
            Files.copy(alert, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
