package com.ps.cromdata.service;

import com.ps.cromdata.domain.TargetInstances;
import com.ps.cromdata.repository.TargetInstancesRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
            System.out.println(targets);
            Map<String, List<TargetInstances>> grouped =
                targets.stream().collect(Collectors.groupingBy(TargetInstances::getJob));
            JSONArray jsonArray = new JSONArray();
            for (Map.Entry<String, List<TargetInstances>> entry : grouped.entrySet()) {
                jsonArray.add(this.buildJson(entry.getKey(), entry.getValue()));
            }
            String path = System.getenv("PROMETHEUS_TARGET_CONFIG_PATH")+"\\cron_targets.json";
            File file = new File(path);
            file.getParentFile().mkdirs();
            file.createNewFile();
            try (FileWriter fileWriter = new FileWriter(path, false)) {
                fileWriter.write(jsonArray.toJSONString());
                fileWriter.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception ignored) {

        }
    }


    public JSONObject buildJson(String job, List<TargetInstances> targetInstances) throws IOException {
        JSONObject obj = new JSONObject();
        JSONObject jobJson = new JSONObject();
        List<String> targets = targetInstances.stream().map(TargetInstances::getTargetHost).collect(Collectors.toList());
        obj.put("targets", targets);
        jobJson.put("job", job);
        obj.put("labels", jobJson);
        return obj;
    }
}


