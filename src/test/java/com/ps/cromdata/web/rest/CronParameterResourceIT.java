package com.ps.cromdata.web.rest;

import com.ps.cromdata.CronDataBackendApp;
import com.ps.cromdata.domain.CronParameter;
import com.ps.cromdata.repository.CronParameterRepository;

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
 * Integration tests for the {@link CronParameterResource} REST controller.
 */
@SpringBootTest(classes = CronDataBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CronParameterResourceIT {

    private static final String DEFAULT_PARAM_SHORT = "AAAAAAAAAA";
    private static final String UPDATED_PARAM_SHORT = "BBBBBBBBBB";

    private static final String DEFAULT_PARAM_LONG = "AAAAAAAAAA";
    private static final String UPDATED_PARAM_LONG = "BBBBBBBBBB";

    private static final Integer DEFAULT_CONFIG_ID = 1;
    private static final Integer UPDATED_CONFIG_ID = 2;

    private static final String DEFAULT_PARAM_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PARAM_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PARAM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PARAM_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private CronParameterRepository cronParameterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCronParameterMockMvc;

    private CronParameter cronParameter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CronParameter createEntity(EntityManager em) {
        CronParameter cronParameter = new CronParameter()
            .param_short(DEFAULT_PARAM_SHORT)
            .param_long(DEFAULT_PARAM_LONG)
            .config_id(DEFAULT_CONFIG_ID)
            .param_type(DEFAULT_PARAM_TYPE)
            .param_description(DEFAULT_PARAM_DESCRIPTION);
        return cronParameter;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CronParameter createUpdatedEntity(EntityManager em) {
        CronParameter cronParameter = new CronParameter()
            .param_short(UPDATED_PARAM_SHORT)
            .param_long(UPDATED_PARAM_LONG)
            .config_id(UPDATED_CONFIG_ID)
            .param_type(UPDATED_PARAM_TYPE)
            .param_description(UPDATED_PARAM_DESCRIPTION);
        return cronParameter;
    }

    @BeforeEach
    public void initTest() {
        cronParameter = createEntity(em);
    }

    @Test
    @Transactional
    public void createCronParameter() throws Exception {
        int databaseSizeBeforeCreate = cronParameterRepository.findAll().size();
        // Create the CronParameter
        restCronParameterMockMvc.perform(post("/api/cron-parameters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronParameter)))
            .andExpect(status().isCreated());

        // Validate the CronParameter in the database
        List<CronParameter> cronParameterList = cronParameterRepository.findAll();
        assertThat(cronParameterList).hasSize(databaseSizeBeforeCreate + 1);
        CronParameter testCronParameter = cronParameterList.get(cronParameterList.size() - 1);
        assertThat(testCronParameter.getParam_short()).isEqualTo(DEFAULT_PARAM_SHORT);
        assertThat(testCronParameter.getParam_long()).isEqualTo(DEFAULT_PARAM_LONG);
        assertThat(testCronParameter.getConfig_id()).isEqualTo(DEFAULT_CONFIG_ID);
        assertThat(testCronParameter.getParam_type()).isEqualTo(DEFAULT_PARAM_TYPE);
        assertThat(testCronParameter.getParam_description()).isEqualTo(DEFAULT_PARAM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createCronParameterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cronParameterRepository.findAll().size();

        // Create the CronParameter with an existing ID
        cronParameter.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCronParameterMockMvc.perform(post("/api/cron-parameters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronParameter)))
            .andExpect(status().isBadRequest());

        // Validate the CronParameter in the database
        List<CronParameter> cronParameterList = cronParameterRepository.findAll();
        assertThat(cronParameterList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkParam_shortIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronParameterRepository.findAll().size();
        // set the field null
        cronParameter.setParam_short(null);

        // Create the CronParameter, which fails.


        restCronParameterMockMvc.perform(post("/api/cron-parameters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronParameter)))
            .andExpect(status().isBadRequest());

        List<CronParameter> cronParameterList = cronParameterRepository.findAll();
        assertThat(cronParameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkParam_longIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronParameterRepository.findAll().size();
        // set the field null
        cronParameter.setParam_long(null);

        // Create the CronParameter, which fails.


        restCronParameterMockMvc.perform(post("/api/cron-parameters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronParameter)))
            .andExpect(status().isBadRequest());

        List<CronParameter> cronParameterList = cronParameterRepository.findAll();
        assertThat(cronParameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConfig_idIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronParameterRepository.findAll().size();
        // set the field null
        cronParameter.setConfig_id(null);

        // Create the CronParameter, which fails.


        restCronParameterMockMvc.perform(post("/api/cron-parameters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronParameter)))
            .andExpect(status().isBadRequest());

        List<CronParameter> cronParameterList = cronParameterRepository.findAll();
        assertThat(cronParameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkParam_typeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronParameterRepository.findAll().size();
        // set the field null
        cronParameter.setParam_type(null);

        // Create the CronParameter, which fails.


        restCronParameterMockMvc.perform(post("/api/cron-parameters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronParameter)))
            .andExpect(status().isBadRequest());

        List<CronParameter> cronParameterList = cronParameterRepository.findAll();
        assertThat(cronParameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCronParameters() throws Exception {
        // Initialize the database
        cronParameterRepository.saveAndFlush(cronParameter);

        // Get all the cronParameterList
        restCronParameterMockMvc.perform(get("/api/cron-parameters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cronParameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].param_short").value(hasItem(DEFAULT_PARAM_SHORT)))
            .andExpect(jsonPath("$.[*].param_long").value(hasItem(DEFAULT_PARAM_LONG)))
            .andExpect(jsonPath("$.[*].config_id").value(hasItem(DEFAULT_CONFIG_ID)))
            .andExpect(jsonPath("$.[*].param_type").value(hasItem(DEFAULT_PARAM_TYPE)))
            .andExpect(jsonPath("$.[*].param_description").value(hasItem(DEFAULT_PARAM_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getCronParameter() throws Exception {
        // Initialize the database
        cronParameterRepository.saveAndFlush(cronParameter);

        // Get the cronParameter
        restCronParameterMockMvc.perform(get("/api/cron-parameters/{id}", cronParameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cronParameter.getId().intValue()))
            .andExpect(jsonPath("$.param_short").value(DEFAULT_PARAM_SHORT))
            .andExpect(jsonPath("$.param_long").value(DEFAULT_PARAM_LONG))
            .andExpect(jsonPath("$.config_id").value(DEFAULT_CONFIG_ID))
            .andExpect(jsonPath("$.param_type").value(DEFAULT_PARAM_TYPE))
            .andExpect(jsonPath("$.param_description").value(DEFAULT_PARAM_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingCronParameter() throws Exception {
        // Get the cronParameter
        restCronParameterMockMvc.perform(get("/api/cron-parameters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCronParameter() throws Exception {
        // Initialize the database
        cronParameterRepository.saveAndFlush(cronParameter);

        int databaseSizeBeforeUpdate = cronParameterRepository.findAll().size();

        // Update the cronParameter
        CronParameter updatedCronParameter = cronParameterRepository.findById(cronParameter.getId()).get();
        // Disconnect from session so that the updates on updatedCronParameter are not directly saved in db
        em.detach(updatedCronParameter);
        updatedCronParameter
            .param_short(UPDATED_PARAM_SHORT)
            .param_long(UPDATED_PARAM_LONG)
            .config_id(UPDATED_CONFIG_ID)
            .param_type(UPDATED_PARAM_TYPE)
            .param_description(UPDATED_PARAM_DESCRIPTION);

        restCronParameterMockMvc.perform(put("/api/cron-parameters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCronParameter)))
            .andExpect(status().isOk());

        // Validate the CronParameter in the database
        List<CronParameter> cronParameterList = cronParameterRepository.findAll();
        assertThat(cronParameterList).hasSize(databaseSizeBeforeUpdate);
        CronParameter testCronParameter = cronParameterList.get(cronParameterList.size() - 1);
        assertThat(testCronParameter.getParam_short()).isEqualTo(UPDATED_PARAM_SHORT);
        assertThat(testCronParameter.getParam_long()).isEqualTo(UPDATED_PARAM_LONG);
        assertThat(testCronParameter.getConfig_id()).isEqualTo(UPDATED_CONFIG_ID);
        assertThat(testCronParameter.getParam_type()).isEqualTo(UPDATED_PARAM_TYPE);
        assertThat(testCronParameter.getParam_description()).isEqualTo(UPDATED_PARAM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingCronParameter() throws Exception {
        int databaseSizeBeforeUpdate = cronParameterRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCronParameterMockMvc.perform(put("/api/cron-parameters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronParameter)))
            .andExpect(status().isBadRequest());

        // Validate the CronParameter in the database
        List<CronParameter> cronParameterList = cronParameterRepository.findAll();
        assertThat(cronParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCronParameter() throws Exception {
        // Initialize the database
        cronParameterRepository.saveAndFlush(cronParameter);

        int databaseSizeBeforeDelete = cronParameterRepository.findAll().size();

        // Delete the cronParameter
        restCronParameterMockMvc.perform(delete("/api/cron-parameters/{id}", cronParameter.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CronParameter> cronParameterList = cronParameterRepository.findAll();
        assertThat(cronParameterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
