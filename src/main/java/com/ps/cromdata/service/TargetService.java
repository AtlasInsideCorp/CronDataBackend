package com.ps.cromdata.service;

import com.ps.cromdata.domain.TargetInstances;
import com.ps.cromdata.repository.TargetInstancesRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

;

@Service
@Transactional
public class TargetService {

    private final TargetInstancesRepository targetInstancesRepository;

    public TargetService(TargetInstancesRepository targetInstancesRepository) {
        this.targetInstancesRepository = targetInstancesRepository;
    }

    public void generatePromConfig() {
        try {
            List<TargetInstances> targets = targetInstancesRepository.findAll();
            Map<String, List<TargetInstances>> grouped =
                targets.stream().collect(Collectors.groupingBy(TargetInstances::getJob));
            JSONArray jsonArray = new JSONArray();
            for (Map.Entry<String, List<TargetInstances>> entry : grouped.entrySet()) {
                jsonArray.add(this.buildJson(entry.getKey(), entry.getValue()));
            }
            System.out.println("************************ TARGET CREATING ******************");
            try {
                Writer output = null;
                File file = new File("/etc/prometheus/targets/cron_targets.json");
                output = new BufferedWriter(new FileWriter(file));
                output.write(jsonArray.toJSONString());
                output.close();
                System.out.println("************************ TARGET CREATED ******************");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }


    public JSONObject buildJson(String job, List<TargetInstances> targetInstances) throws IOException {
        JSONObject obj = new JSONObject();
        JSONObject jobJson = new JSONObject();
        List<String> targets = targetInstances.stream()
            .map(TargetInstances -> TargetInstances.getTargetHost() + ":" + TargetInstances.getPort().toString()).collect(Collectors.toList());
        obj.put("targets", targets);
        jobJson.put("job", job);
        obj.put("labels", jobJson);
        return obj;
    }

    public void insertDefaultValues() {
        String hostname = System.getenv("CRONDATA_SERVER_HOST");
        if (hostname == null)
            hostname = "localhost";
        List<Target> targets = new ArrayList<>();
        targets.add(new Target(hostname,
            9001, "cadvisor",
            "Container Advisor provides container users an understanding of the resource usage and" +
                " performance characteristics of their running containers."));
        targets.add(new Target(hostname,
            9100, "node-exporter",
            "Node Exporter exposes a wide variety of hardware- and kernel-related metrics"));
        targets.add(new Target(hostname,
            9093, "alert-manager",
            "The Alert Manager handles alerts sent by client applications such as the Prometheus server."));
//        this.targetInstancesRepository.saveAll(targets);
        for (Target t : targets) {
            TargetInstances target = new TargetInstances();
            target.setTargetHost(t.getTargetHost());
            target.setJob(t.getJob());
            target.setPort(t.getPort());
            target.setDescription(t.getDescription());
            targetInstancesRepository.save(target);
        }
    }

    @PostConstruct
    public void init() {
        this.insertDefaultValues();
        this.generatePromConfig();
    }
}

class Target {

    private String targetHost;
    private Integer port;
    private String job;
    private String description;

    public Target(String targetHost, Integer port, String job, String description) {

        this.targetHost = targetHost;
        this.port = port;
        this.job = job;
        this.description = description;
    }


    public String getTargetHost() {
        return targetHost;
    }

    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
