package com.ps.cromdata.repository;

import com.ps.cromdata.domain.Targets;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public abstract class CustomTargetRepository {
    private final TargetsRepository targetsRepository;

    public CustomTargetRepository(TargetsRepository targetsRepository) {
        this.targetsRepository = targetsRepository;
    }

    public void generatePromConfig() {
        try {
            List<Targets> targets = targetsRepository.findAll();
            Map<String, List<Targets>> grouped =
                targets.stream().collect(Collectors.groupingBy(Targets::getJob));
            JSONArray jsonArray = new JSONArray();
            for (Map.Entry<String, List<Targets>> entry : grouped.entrySet()) {
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


    public JSONObject buildJson(String job, List<Targets> targetInstances) throws IOException {
        JSONObject obj = new JSONObject();
        JSONObject jobJson = new JSONObject();
        List<String> targets = targetInstances.stream()
            .map(TargetInstances -> TargetInstances.getHost() + ":" + TargetInstances.getPort().toString()).collect(Collectors.toList());
        obj.put("targets", targets);
        jobJson.put("job", job);
        obj.put("labels", jobJson);
        return obj;
    }

    public void insertDefaultValues() {
        if (!targetsRepository.findById(1L).isPresent()) {
            String hostname = System.getenv("CRONDATA_SERVER_HOST");
            Targets cadvisor = new Targets();
            cadvisor.setHost(hostname);
            cadvisor.setPort(9001);
            cadvisor.setJob("cadvisor");
            cadvisor.setDescription("Container Advisor provides container users an understanding of the resource usage and" +
                " performance characteristics of their running containers.");
            Targets nodeExporter = new Targets();
            nodeExporter.setHost(hostname);
            nodeExporter.setPort(9100);
            nodeExporter.setJob("node-exporter");
            nodeExporter.setDescription("Node Exporter exposes a wide variety of hardware- and kernel-related metrics.");
            Targets alertManager = new Targets();
            alertManager.setHost(hostname);
            alertManager.setPort(9093);
            alertManager.setJob("alert-manager");
            alertManager.setDescription("The Alert Manager handles alerts sent by client applications" +
                " such as the Prometheus server.");
            targetsRepository.save(cadvisor);
            targetsRepository.save(nodeExporter);
            targetsRepository.save(alertManager);
        }
    }

    @PostConstruct
    public void init() {
        this.insertDefaultValues();
        this.generatePromConfig();
    }
}
