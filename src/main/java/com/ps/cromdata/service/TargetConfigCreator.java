package com.ps.cromdata.service;

import com.ps.cromdata.domain.Targets;
import com.ps.cromdata.repository.TargetsRepository;
import com.ps.cromdata.util.FilePermissionUtil;
import org.apache.commons.lang3.SystemUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import sun.awt.OSInfo;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ps.cromdata.config.Constants.PROMETHEUS_PATH;

@Component
public class TargetConfigCreator {

    private final TargetsRepository targetsRepository;

    public TargetConfigCreator(TargetsRepository targetsRepository) {
        this.targetsRepository = targetsRepository;
    }

    @PostConstruct
    public void init() {
        this.insertDefaultValues();
        this.generatePromConfig();
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
                Writer output;
                String path = PROMETHEUS_PATH + "targets/cron_targets.json";
                File file = new File(path);
                file.getParentFile().mkdirs();
                file.setReadable(true, false);
                file.setWritable(true, false);
                file.setExecutable(true, false);
                if(SystemUtils.IS_OS_LINUX) {
                    FilePermissionUtil perm = new FilePermissionUtil();
                    perm.setPathPermission(path);
                }
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
            .map(this::buildTarget).collect(Collectors.toList());
        obj.put("targets", targets);
        jobJson.put("job", job);
        obj.put("labels", jobJson);
        return obj;
    }

    private String buildTarget(Targets target) {
        if (target.getPort() == null) {
            return target.getHost();
        } else {
            return target.getHost() + ":" + target.getPort().toString();
        }

    }


    public void insertDefaultValues() {
        if (targetsRepository.findAll().isEmpty()) {
            System.out.println("************************** INIT DEFAULT TARGETS IN DATABASE ************************** ");
            String hostname = System.getenv("CRONDATA_SERVER_HOST");
            Targets cadvisor = new Targets();
            cadvisor.setId(1L);
            cadvisor.setHost(hostname);
            cadvisor.setPort(9001);
            cadvisor.setJob("cadvisor");
            cadvisor.setDescription("Container Advisor provides container users an understanding of the resource usage and" +
                " performance characteristics of their running containers.");
            Targets nodeExporter = new Targets();
            nodeExporter.setId(2L);
            nodeExporter.setHost("node-exporter");
            nodeExporter.setPort(9100);
            nodeExporter.setJob("node-exporter");
            nodeExporter.setDescription("Node Exporter exposes a wide variety of hardware- and kernel-related metrics.");
            Targets alertManager = new Targets();
            alertManager.setId(3L);
            alertManager.setHost("alertmanager");
            alertManager.setPort(9093);
            alertManager.setJob("alert-manager");
            alertManager.setDescription("The Alert Manager handles alerts sent by client applications" +
                " such as the Prometheus server.");
            targetsRepository.save(cadvisor);
            targetsRepository.save(nodeExporter);
            targetsRepository.save(alertManager);
        }
    }

}
