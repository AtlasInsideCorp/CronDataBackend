package com.ps.cromdata.web.rest;

import com.ps.cromdata.CronDataBackendApp;
import com.ps.cromdata.domain.CronConfig;
import com.ps.cromdata.repository.CronConfigRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CronConfigResource} REST controller.
 */
@SpringBootTest(classes = CronDataBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CronConfigResourceIT {

    private static final String DEFAULT_CONFIG_SHORT = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_SHORT = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_LONG = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_LONG = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RESTART_REQUIRED = false;
    private static final Boolean UPDATED_RESTART_REQUIRED = true;

    @Autowired
    private CronConfigRepository cronConfigRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCronConfigMockMvc;

    private CronConfig cronConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CronConfig createEntity(EntityManager em) {
        CronConfig cronConfig = new CronConfig()
            .config_short(DEFAULT_CONFIG_SHORT)
            .config_long(DEFAULT_CONFIG_LONG)
            .config_description(DEFAULT_CONFIG_DESCRIPTION)
            .restart_required(DEFAULT_RESTART_REQUIRED);
        return cronConfig;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CronConfig createUpdatedEntity(EntityManager em) {
        CronConfig cronConfig = new CronConfig()
            .config_short(UPDATED_CONFIG_SHORT)
            .config_long(UPDATED_CONFIG_LONG)
            .config_description(UPDATED_CONFIG_DESCRIPTION)
            .restart_required(UPDATED_RESTART_REQUIRED);
        return cronConfig;
    }

    @BeforeEach
    public void initTest() {
        cronConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createCronConfig() throws Exception {
        int databaseSizeBeforeCreate = cronConfigRepository.findAll().size();
        // Create the CronConfig
        restCronConfigMockMvc.perform(post("/api/cron-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronConfig)))
            .andExpect(status().isCreated());

        // Validate the CronConfig in the database
        List<CronConfig> cronConfigList = cronConfigRepository.findAll();
        assertThat(cronConfigList).hasSize(databaseSizeBeforeCreate + 1);
        CronConfig testCronConfig = cronConfigList.get(cronConfigList.size() - 1);
        assertThat(testCronConfig.getConfig_short()).isEqualTo(DEFAULT_CONFIG_SHORT);
        assertThat(testCronConfig.getConfig_long()).isEqualTo(DEFAULT_CONFIG_LONG);
        assertThat(testCronConfig.getConfig_description()).isEqualTo(DEFAULT_CONFIG_DESCRIPTION);
        assertThat(testCronConfig.isRestart_required()).isEqualTo(DEFAULT_RESTART_REQUIRED);
    }

    @Test
    @Transactional
    public void createCronConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cronConfigRepository.findAll().size();

        // Create the CronConfig with an existing ID
        cronConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCronConfigMockMvc.perform(post("/api/cron-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronConfig)))
            .andExpect(status().isBadRequest());

        // Validate the CronConfig in the database
        List<CronConfig> cronConfigList = cronConfigRepository.findAll();
        assertThat(cronConfigList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkConfig_shortIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronConfigRepository.findAll().size();
        // set the field null
        cronConfig.setConfig_short(null);

        // Create the CronConfig, which fails.


        restCronConfigMockMvc.perform(post("/api/cron-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronConfig)))
            .andExpect(status().isBadRequest());

        List<CronConfig> cronConfigList = cronConfigRepository.findAll();
        assertThat(cronConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConfig_longIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronConfigRepository.findAll().size();
        // set the field null
        cronConfig.setConfig_long(null);

        // Create the CronConfig, which fails.


        restCronConfigMockMvc.perform(post("/api/cron-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronConfig)))
            .andExpect(status().isBadRequest());

        List<CronConfig> cronConfigList = cronConfigRepository.findAll();
        assertThat(cronConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRestart_requiredIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronConfigRepository.findAll().size();
        // set the field null
        cronConfig.setRestart_required(null);

        // Create the CronConfig, which fails.


        restCronConfigMockMvc.perform(post("/api/cron-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronConfig)))
            .andExpect(status().isBadRequest());

        List<CronConfig> cronConfigList = cronConfigRepository.findAll();
        assertThat(cronConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCronConfigs() throws Exception {
        // Initialize the database
        cronConfigRepository.saveAndFlush(cronConfig);

        // Get all the cronConfigList
        restCronConfigMockMvc.perform(get("/api/cron-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cronConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].config_short").value(hasItem(DEFAULT_CONFIG_SHORT)))
            .andExpect(jsonPath("$.[*].config_long").value(hasItem(DEFAULT_CONFIG_LONG)))
            .andExpect(jsonPath("$.[*].config_description").value(hasItem(DEFAULT_CONFIG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].restart_required").value(hasItem(DEFAULT_RESTART_REQUIRED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCronConfig() throws Exception {
        // Initialize the database
        cronConfigRepository.saveAndFlush(cronConfig);

        // Get the cronConfig
        restCronConfigMockMvc.perform(get("/api/cron-configs/{id}", cronConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cronConfig.getId().intValue()))
            .andExpect(jsonPath("$.config_short").value(DEFAULT_CONFIG_SHORT))
            .andExpect(jsonPath("$.config_long").value(DEFAULT_CONFIG_LONG))
            .andExpect(jsonPath("$.config_description").value(DEFAULT_CONFIG_DESCRIPTION))
            .andExpect(jsonPath("$.restart_required").value(DEFAULT_RESTART_REQUIRED.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingCronConfig() throws Exception {
        // Get the cronConfig
        restCronConfigMockMvc.perform(get("/api/cron-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCronConfig() throws Exception {
        // Initialize the database
        cronConfigRepository.saveAndFlush(cronConfig);

        int databaseSizeBeforeUpdate = cronConfigRepository.findAll().size();

        // Update the cronConfig
        CronConfig updatedCronConfig = cronConfigRepository.findById(cronConfig.getId()).get();
        // Disconnect from session so that the updates on updatedCronConfig are not directly saved in db
        em.detach(updatedCronConfig);
        updatedCronConfig
            .config_short(UPDATED_CONFIG_SHORT)
            .config_long(UPDATED_CONFIG_LONG)
            .config_description(UPDATED_CONFIG_DESCRIPTION)
            .restart_required(UPDATED_RESTART_REQUIRED);

        restCronConfigMockMvc.perform(put("/api/cron-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCronConfig)))
            .andExpect(status().isOk());

        // Validate the CronConfig in the database
        List<CronConfig> cronConfigList = cronConfigRepository.findAll();
        assertThat(cronConfigList).hasSize(databaseSizeBeforeUpdate);
        CronConfig testCronConfig = cronConfigList.get(cronConfigList.size() - 1);
        assertThat(testCronConfig.getConfig_short()).isEqualTo(UPDATED_CONFIG_SHORT);
        assertThat(testCronConfig.getConfig_long()).isEqualTo(UPDATED_CONFIG_LONG);
        assertThat(testCronConfig.getConfig_description()).isEqualTo(UPDATED_CONFIG_DESCRIPTION);
        assertThat(testCronConfig.isRestart_required()).isEqualTo(UPDATED_RESTART_REQUIRED);
    }

    @Test
    @Transactional
    public void updateNonExistingCronConfig() throws Exception {
        int databaseSizeBeforeUpdate = cronConfigRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCronConfigMockMvc.perform(put("/api/cron-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronConfig)))
            .andExpect(status().isBadRequest());

        // Validate the CronConfig in the database
        List<CronConfig> cronConfigList = cronConfigRepository.findAll();
        assertThat(cronConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCronConfig() throws Exception {
        // Initialize the database
        cronConfigRepository.saveAndFlush(cronConfig);

        int databaseSizeBeforeDelete = cronConfigRepository.findAll().size();

        // Delete the cronConfig
        restCronConfigMockMvc.perform(delete("/api/cron-configs/{id}", cronConfig.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CronConfig> cronConfigList = cronConfigRepository.findAll();
        assertThat(cronConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
