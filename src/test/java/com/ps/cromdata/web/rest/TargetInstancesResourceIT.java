package com.ps.cromdata.web.rest;

import com.ps.cromdata.CronDataBackendApp;
import com.ps.cromdata.domain.TargetInstances;
import com.ps.cromdata.repository.TargetInstancesRepository;

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
 * Integration tests for the {@link TargetInstancesResource} REST controller.
 */
@SpringBootTest(classes = CronDataBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TargetInstancesResourceIT {

    private static final String DEFAULT_TARGET_HOST = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_HOST = "BBBBBBBBBB";

    private static final String DEFAULT_JOB = "AAAAAAAAAA";
    private static final String UPDATED_JOB = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private TargetInstancesRepository targetInstancesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTargetInstancesMockMvc;

    private TargetInstances targetInstances;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TargetInstances createEntity(EntityManager em) {
        TargetInstances targetInstances = new TargetInstances()
            .target_host(DEFAULT_TARGET_HOST)
            .job(DEFAULT_JOB)
            .description(DEFAULT_DESCRIPTION);
        return targetInstances;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TargetInstances createUpdatedEntity(EntityManager em) {
        TargetInstances targetInstances = new TargetInstances()
            .target_host(UPDATED_TARGET_HOST)
            .job(UPDATED_JOB)
            .description(UPDATED_DESCRIPTION);
        return targetInstances;
    }

    @BeforeEach
    public void initTest() {
        targetInstances = createEntity(em);
    }

    @Test
    @Transactional
    public void createTargetInstances() throws Exception {
        int databaseSizeBeforeCreate = targetInstancesRepository.findAll().size();
        // Create the TargetInstances
        restTargetInstancesMockMvc.perform(post("/api/target-instances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(targetInstances)))
            .andExpect(status().isCreated());

        // Validate the TargetInstances in the database
        List<TargetInstances> targetInstancesList = targetInstancesRepository.findAll();
        assertThat(targetInstancesList).hasSize(databaseSizeBeforeCreate + 1);
        TargetInstances testTargetInstances = targetInstancesList.get(targetInstancesList.size() - 1);
        assertThat(testTargetInstances.getTargetHost()).isEqualTo(DEFAULT_TARGET_HOST);
        assertThat(testTargetInstances.getJob()).isEqualTo(DEFAULT_JOB);
        assertThat(testTargetInstances.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTargetInstancesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = targetInstancesRepository.findAll().size();

        // Create the TargetInstances with an existing ID
        targetInstances.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTargetInstancesMockMvc.perform(post("/api/target-instances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(targetInstances)))
            .andExpect(status().isBadRequest());

        // Validate the TargetInstances in the database
        List<TargetInstances> targetInstancesList = targetInstancesRepository.findAll();
        assertThat(targetInstancesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTarget_hostIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetInstancesRepository.findAll().size();
        // set the field null
        targetInstances.setTargetHost(null);

        // Create the TargetInstances, which fails.


        restTargetInstancesMockMvc.perform(post("/api/target-instances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(targetInstances)))
            .andExpect(status().isBadRequest());

        List<TargetInstances> targetInstancesList = targetInstancesRepository.findAll();
        assertThat(targetInstancesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJobIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetInstancesRepository.findAll().size();
        // set the field null
        targetInstances.setJob(null);

        // Create the TargetInstances, which fails.


        restTargetInstancesMockMvc.perform(post("/api/target-instances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(targetInstances)))
            .andExpect(status().isBadRequest());

        List<TargetInstances> targetInstancesList = targetInstancesRepository.findAll();
        assertThat(targetInstancesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTargetInstances() throws Exception {
        // Initialize the database
        targetInstancesRepository.saveAndFlush(targetInstances);

        // Get all the targetInstancesList
        restTargetInstancesMockMvc.perform(get("/api/target-instances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(targetInstances.getId().intValue())))
            .andExpect(jsonPath("$.[*].target_host").value(hasItem(DEFAULT_TARGET_HOST)))
            .andExpect(jsonPath("$.[*].job").value(hasItem(DEFAULT_JOB)))
            .andExpect(jsonPath("$.[*].zone").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void getTargetInstances() throws Exception {
        // Initialize the database
        targetInstancesRepository.saveAndFlush(targetInstances);

        // Get the targetInstances
        restTargetInstancesMockMvc.perform(get("/api/target-instances/{id}", targetInstances.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(targetInstances.getId().intValue()))
            .andExpect(jsonPath("$.target_host").value(DEFAULT_TARGET_HOST))
            .andExpect(jsonPath("$.job").value(DEFAULT_JOB))
            .andExpect(jsonPath("$.zone").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingTargetInstances() throws Exception {
        // Get the targetInstances
        restTargetInstancesMockMvc.perform(get("/api/target-instances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTargetInstances() throws Exception {
        // Initialize the database
        targetInstancesRepository.saveAndFlush(targetInstances);

        int databaseSizeBeforeUpdate = targetInstancesRepository.findAll().size();

        // Update the targetInstances
        TargetInstances updatedTargetInstances = targetInstancesRepository.findById(targetInstances.getId()).get();
        // Disconnect from session so that the updates on updatedTargetInstances are not directly saved in db
        em.detach(updatedTargetInstances);
        updatedTargetInstances
            .target_host(UPDATED_TARGET_HOST)
            .job(UPDATED_JOB)
            .description(UPDATED_DESCRIPTION);

        restTargetInstancesMockMvc.perform(put("/api/target-instances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTargetInstances)))
            .andExpect(status().isOk());

        // Validate the TargetInstances in the database
        List<TargetInstances> targetInstancesList = targetInstancesRepository.findAll();
        assertThat(targetInstancesList).hasSize(databaseSizeBeforeUpdate);
        TargetInstances testTargetInstances = targetInstancesList.get(targetInstancesList.size() - 1);
        assertThat(testTargetInstances.getTargetHost()).isEqualTo(UPDATED_TARGET_HOST);
        assertThat(testTargetInstances.getJob()).isEqualTo(UPDATED_JOB);
        assertThat(testTargetInstances.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingTargetInstances() throws Exception {
        int databaseSizeBeforeUpdate = targetInstancesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTargetInstancesMockMvc.perform(put("/api/target-instances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(targetInstances)))
            .andExpect(status().isBadRequest());

        // Validate the TargetInstances in the database
        List<TargetInstances> targetInstancesList = targetInstancesRepository.findAll();
        assertThat(targetInstancesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTargetInstances() throws Exception {
        // Initialize the database
        targetInstancesRepository.saveAndFlush(targetInstances);

        int databaseSizeBeforeDelete = targetInstancesRepository.findAll().size();

        // Delete the targetInstances
        restTargetInstancesMockMvc.perform(delete("/api/target-instances/{id}", targetInstances.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TargetInstances> targetInstancesList = targetInstancesRepository.findAll();
        assertThat(targetInstancesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
