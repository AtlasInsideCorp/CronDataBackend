package com.ps.cromdata.service;

import com.ps.cromdata.domain.TargetInstances;
import com.ps.cromdata.repository.TargetInstancesRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.*;
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
            for (TargetInstances targetInstances : targets) {
                jsonArray.add(this.buildJson(targetInstances));
            }
//            for (Map.Entry<String, List<TargetInstances>> entry : grouped.entrySet()) {
//                jsonArray.add(this.buildJson(entry.getKey(), entry.getValue()));
//            }
            System.out.println("************************ TARGET CREATING ******************");
            try {
                Writer output = null;
//                File file = new File("/etc/prometheus/targets/cron_targets.json");
                File file = new File("d:\\crondata\\cron_targets.json");
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


    public JSONObject buildJson(TargetInstances targetInstances) throws IOException {
        JSONObject obj = new JSONObject();
        JSONObject jobJson = new JSONObject();
        JSONArray arrayTarget = new JSONArray();
        arrayTarget.add(targetInstances.getTargetHost() + ":" + targetInstances.getPort().toString());
        obj.put("targets", arrayTarget);
        jobJson.put("job", targetInstances.getJob());
        obj.put("labels", jobJson);
        return obj;
    }

    @PostConstruct
    public void init() {
        this.generatePromConfig();
    }
}


