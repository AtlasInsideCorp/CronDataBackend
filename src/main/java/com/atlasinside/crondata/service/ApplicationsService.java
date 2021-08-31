package com.atlasinside.crondata.service;

import com.atlasinside.crondata.config.Constants;
import com.atlasinside.crondata.domain.Applications;
import com.atlasinside.crondata.repository.ApplicationsRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Applications}.
 */
@Service
@Transactional
public class ApplicationsService {

    private final Logger log = LoggerFactory.getLogger(ApplicationsService.class);

    private final ApplicationsRepository applicationsRepository;
    private final ApplicationModuleService applicationModuleService;

    public ApplicationsService(ApplicationsRepository applicationsRepository,
                               ApplicationModuleService applicationModuleService) {
        this.applicationsRepository = applicationsRepository;
        this.applicationModuleService = applicationModuleService;
    }

    /**
     * Save a applications.
     *
     * @param applications the entity to save.
     * @return the persisted entity.
     */
    public Applications save(Applications applications) {
        log.debug("Request to save Applications : {}", applications);
        return applicationsRepository.save(applications);
    }

    /**
     * Get all the applications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Applications> findAll(Pageable pageable) {
        log.debug("Request to get all Applications");
        return applicationsRepository.findAll(pageable);
    }


    /**
     * Get one applications by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Applications> findOne(Long id) {
        log.debug("Request to get Applications : {}", id);
        return applicationsRepository.findById(id);
    }

    /**
     * Delete the applications by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Applications : {}", id);
        applicationsRepository.deleteById(id);
    }

    public Applications activateModule(Applications app) throws IOException, ParseException {
        String dashboardsLocation = Constants.MODULES_PATH + app.getAppPathResource() + "/dashboards/";
        if (Files.exists(Paths.get(dashboardsLocation))) {
            JSONParser parser = new JSONParser();
            String uids = "";
            File[] dashboards = applicationModuleService.getFileListFromFolder(dashboardsLocation);
            for (File dashboard : dashboards) {
                Object obj = parser.parse(new FileReader(dashboard.getPath()));
                JSONObject jsonObject = (JSONObject) obj;
                String response = applicationModuleService.saveDashboard(jsonObject);
                uids = String.join(",", uids, response);
            }
            if (uids.startsWith(","))
                uids = uids.substring(1, uids.length());
            app.setUid(uids);
        }
        String location = Constants.MODULES_PATH + app.getAppPathResource() + "/rules/";
        if (Files.exists(Paths.get(dashboardsLocation))) {
            applicationModuleService.copyDirectory(location);
        }
        app.setIsInstalled(true);
        return app;
    }

    public Applications deactivateModule(Applications app) throws IOException {
        if (app.getUid() != null) {
            String[] dashboards = app.getUid().split(",");
            for (String uid : dashboards) {
                applicationModuleService.deleteDashboard(uid);
            }
        }
        String location = Constants.MODULES_PATH + app.getAppPathResource() + "/rules/";
        if (Files.exists(Paths.get(location))) {
            if (Files.exists(Paths.get(location)))
                applicationModuleService.deleteRules(location);
        }
        app.setIsInstalled(false);
        app.setUid(null);
        return app;
    }


}
