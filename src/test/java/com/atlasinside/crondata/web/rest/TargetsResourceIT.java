package com.atlasinside.crondata.web.rest;

import com.atlasinside.crondata.CronDataBackendApp;
import com.atlasinside.crondata.domain.Targets;
import com.atlasinside.crondata.repository.TargetsRepository;
import com.atlasinside.crondata.service.TargetsService;
import com.atlasinside.crondata.service.TargetsQueryService;

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
 * Integration tests for the {@link TargetsResource} REST controller.
 */
@SpringBootTest(classes = CronDataBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TargetsResourceIT {

    private static final String DEFAULT_HOST = "AAAAAAAAAA";
    private static final String UPDATED_HOST = "BBBBBBBBBB";

    private static final Integer DEFAULT_PORT = 1;
    private static final Integer UPDATED_PORT = 2;
    private static final Integer SMALLER_PORT = 1 - 1;

    private static final String DEFAULT_JOB = "AAAAAAAAAA";
    private static final String UPDATED_JOB = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private TargetsRepository targetsRepository;

    @Autowired
    private TargetsService targetsService;

    @Autowired
    private TargetsQueryService targetsQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTargetsMockMvc;

    private Targets targets;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Targets createEntity(EntityManager em) {
        Targets targets = new Targets()
            .host(DEFAULT_HOST)
            .port(DEFAULT_PORT)
            .job(DEFAULT_JOB)
            .description(DEFAULT_DESCRIPTION);
        return targets;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Targets createUpdatedEntity(EntityManager em) {
        Targets targets = new Targets()
            .host(UPDATED_HOST)
            .port(UPDATED_PORT)
            .job(UPDATED_JOB)
            .description(UPDATED_DESCRIPTION);
        return targets;
    }

    @BeforeEach
    public void initTest() {
        targets = createEntity(em);
    }

    @Test
    @Transactional
    public void createTargets() throws Exception {
        int databaseSizeBeforeCreate = targetsRepository.findAll().size();
        // Create the Targets
        restTargetsMockMvc.perform(post("/api/targets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(targets)))
            .andExpect(status().isCreated());

        // Validate the Targets in the database
        List<Targets> targetsList = targetsRepository.findAll();
        assertThat(targetsList).hasSize(databaseSizeBeforeCreate + 1);
        Targets testTargets = targetsList.get(targetsList.size() - 1);
        assertThat(testTargets.getHost()).isEqualTo(DEFAULT_HOST);
        assertThat(testTargets.getPort()).isEqualTo(DEFAULT_PORT);
        assertThat(testTargets.getJob()).isEqualTo(DEFAULT_JOB);
        assertThat(testTargets.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTargetsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = targetsRepository.findAll().size();

        // Create the Targets with an existing ID
        targets.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTargetsMockMvc.perform(post("/api/targets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(targets)))
            .andExpect(status().isBadRequest());

        // Validate the Targets in the database
        List<Targets> targetsList = targetsRepository.findAll();
        assertThat(targetsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkHostIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetsRepository.findAll().size();
        // set the field null
        targets.setHost(null);

        // Create the Targets, which fails.


        restTargetsMockMvc.perform(post("/api/targets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(targets)))
            .andExpect(status().isBadRequest());

        List<Targets> targetsList = targetsRepository.findAll();
        assertThat(targetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPortIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetsRepository.findAll().size();
        // set the field null
        targets.setPort(null);

        // Create the Targets, which fails.


        restTargetsMockMvc.perform(post("/api/targets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(targets)))
            .andExpect(status().isBadRequest());

        List<Targets> targetsList = targetsRepository.findAll();
        assertThat(targetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJobIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetsRepository.findAll().size();
        // set the field null
        targets.setJob(null);

        // Create the Targets, which fails.


        restTargetsMockMvc.perform(post("/api/targets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(targets)))
            .andExpect(status().isBadRequest());

        List<Targets> targetsList = targetsRepository.findAll();
        assertThat(targetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTargets() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList
        restTargetsMockMvc.perform(get("/api/targets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(targets.getId().intValue())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].job").value(hasItem(DEFAULT_JOB)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void getTargets() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get the targets
        restTargetsMockMvc.perform(get("/api/targets/{id}", targets.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(targets.getId().intValue()))
            .andExpect(jsonPath("$.host").value(DEFAULT_HOST))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT))
            .andExpect(jsonPath("$.job").value(DEFAULT_JOB))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getTargetsByIdFiltering() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        Long id = targets.getId();

        defaultTargetsShouldBeFound("id.equals=" + id);
        defaultTargetsShouldNotBeFound("id.notEquals=" + id);

        defaultTargetsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTargetsShouldNotBeFound("id.greaterThan=" + id);

        defaultTargetsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTargetsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTargetsByHostIsEqualToSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where host equals to DEFAULT_HOST
        defaultTargetsShouldBeFound("host.equals=" + DEFAULT_HOST);

        // Get all the targetsList where host equals to UPDATED_HOST
        defaultTargetsShouldNotBeFound("host.equals=" + UPDATED_HOST);
    }

    @Test
    @Transactional
    public void getAllTargetsByHostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where host not equals to DEFAULT_HOST
        defaultTargetsShouldNotBeFound("host.notEquals=" + DEFAULT_HOST);

        // Get all the targetsList where host not equals to UPDATED_HOST
        defaultTargetsShouldBeFound("host.notEquals=" + UPDATED_HOST);
    }

    @Test
    @Transactional
    public void getAllTargetsByHostIsInShouldWork() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where host in DEFAULT_HOST or UPDATED_HOST
        defaultTargetsShouldBeFound("host.in=" + DEFAULT_HOST + "," + UPDATED_HOST);

        // Get all the targetsList where host equals to UPDATED_HOST
        defaultTargetsShouldNotBeFound("host.in=" + UPDATED_HOST);
    }

    @Test
    @Transactional
    public void getAllTargetsByHostIsNullOrNotNull() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where host is not null
        defaultTargetsShouldBeFound("host.specified=true");

        // Get all the targetsList where host is null
        defaultTargetsShouldNotBeFound("host.specified=false");
    }
                @Test
    @Transactional
    public void getAllTargetsByHostContainsSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where host contains DEFAULT_HOST
        defaultTargetsShouldBeFound("host.contains=" + DEFAULT_HOST);

        // Get all the targetsList where host contains UPDATED_HOST
        defaultTargetsShouldNotBeFound("host.contains=" + UPDATED_HOST);
    }

    @Test
    @Transactional
    public void getAllTargetsByHostNotContainsSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where host does not contain DEFAULT_HOST
        defaultTargetsShouldNotBeFound("host.doesNotContain=" + DEFAULT_HOST);

        // Get all the targetsList where host does not contain UPDATED_HOST
        defaultTargetsShouldBeFound("host.doesNotContain=" + UPDATED_HOST);
    }


    @Test
    @Transactional
    public void getAllTargetsByPortIsEqualToSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where port equals to DEFAULT_PORT
        defaultTargetsShouldBeFound("port.equals=" + DEFAULT_PORT);

        // Get all the targetsList where port equals to UPDATED_PORT
        defaultTargetsShouldNotBeFound("port.equals=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    public void getAllTargetsByPortIsNotEqualToSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where port not equals to DEFAULT_PORT
        defaultTargetsShouldNotBeFound("port.notEquals=" + DEFAULT_PORT);

        // Get all the targetsList where port not equals to UPDATED_PORT
        defaultTargetsShouldBeFound("port.notEquals=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    public void getAllTargetsByPortIsInShouldWork() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where port in DEFAULT_PORT or UPDATED_PORT
        defaultTargetsShouldBeFound("port.in=" + DEFAULT_PORT + "," + UPDATED_PORT);

        // Get all the targetsList where port equals to UPDATED_PORT
        defaultTargetsShouldNotBeFound("port.in=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    public void getAllTargetsByPortIsNullOrNotNull() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where port is not null
        defaultTargetsShouldBeFound("port.specified=true");

        // Get all the targetsList where port is null
        defaultTargetsShouldNotBeFound("port.specified=false");
    }

    @Test
    @Transactional
    public void getAllTargetsByPortIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where port is greater than or equal to DEFAULT_PORT
        defaultTargetsShouldBeFound("port.greaterThanOrEqual=" + DEFAULT_PORT);

        // Get all the targetsList where port is greater than or equal to UPDATED_PORT
        defaultTargetsShouldNotBeFound("port.greaterThanOrEqual=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    public void getAllTargetsByPortIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where port is less than or equal to DEFAULT_PORT
        defaultTargetsShouldBeFound("port.lessThanOrEqual=" + DEFAULT_PORT);

        // Get all the targetsList where port is less than or equal to SMALLER_PORT
        defaultTargetsShouldNotBeFound("port.lessThanOrEqual=" + SMALLER_PORT);
    }

    @Test
    @Transactional
    public void getAllTargetsByPortIsLessThanSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where port is less than DEFAULT_PORT
        defaultTargetsShouldNotBeFound("port.lessThan=" + DEFAULT_PORT);

        // Get all the targetsList where port is less than UPDATED_PORT
        defaultTargetsShouldBeFound("port.lessThan=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    public void getAllTargetsByPortIsGreaterThanSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where port is greater than DEFAULT_PORT
        defaultTargetsShouldNotBeFound("port.greaterThan=" + DEFAULT_PORT);

        // Get all the targetsList where port is greater than SMALLER_PORT
        defaultTargetsShouldBeFound("port.greaterThan=" + SMALLER_PORT);
    }


    @Test
    @Transactional
    public void getAllTargetsByJobIsEqualToSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where job equals to DEFAULT_JOB
        defaultTargetsShouldBeFound("job.equals=" + DEFAULT_JOB);

        // Get all the targetsList where job equals to UPDATED_JOB
        defaultTargetsShouldNotBeFound("job.equals=" + UPDATED_JOB);
    }

    @Test
    @Transactional
    public void getAllTargetsByJobIsNotEqualToSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where job not equals to DEFAULT_JOB
        defaultTargetsShouldNotBeFound("job.notEquals=" + DEFAULT_JOB);

        // Get all the targetsList where job not equals to UPDATED_JOB
        defaultTargetsShouldBeFound("job.notEquals=" + UPDATED_JOB);
    }

    @Test
    @Transactional
    public void getAllTargetsByJobIsInShouldWork() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where job in DEFAULT_JOB or UPDATED_JOB
        defaultTargetsShouldBeFound("job.in=" + DEFAULT_JOB + "," + UPDATED_JOB);

        // Get all the targetsList where job equals to UPDATED_JOB
        defaultTargetsShouldNotBeFound("job.in=" + UPDATED_JOB);
    }

    @Test
    @Transactional
    public void getAllTargetsByJobIsNullOrNotNull() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where job is not null
        defaultTargetsShouldBeFound("job.specified=true");

        // Get all the targetsList where job is null
        defaultTargetsShouldNotBeFound("job.specified=false");
    }
                @Test
    @Transactional
    public void getAllTargetsByJobContainsSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where job contains DEFAULT_JOB
        defaultTargetsShouldBeFound("job.contains=" + DEFAULT_JOB);

        // Get all the targetsList where job contains UPDATED_JOB
        defaultTargetsShouldNotBeFound("job.contains=" + UPDATED_JOB);
    }

    @Test
    @Transactional
    public void getAllTargetsByJobNotContainsSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where job does not contain DEFAULT_JOB
        defaultTargetsShouldNotBeFound("job.doesNotContain=" + DEFAULT_JOB);

        // Get all the targetsList where job does not contain UPDATED_JOB
        defaultTargetsShouldBeFound("job.doesNotContain=" + UPDATED_JOB);
    }


    @Test
    @Transactional
    public void getAllTargetsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where description equals to DEFAULT_DESCRIPTION
        defaultTargetsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the targetsList where description equals to UPDATED_DESCRIPTION
        defaultTargetsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTargetsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where description not equals to DEFAULT_DESCRIPTION
        defaultTargetsShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the targetsList where description not equals to UPDATED_DESCRIPTION
        defaultTargetsShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTargetsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTargetsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the targetsList where description equals to UPDATED_DESCRIPTION
        defaultTargetsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTargetsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where description is not null
        defaultTargetsShouldBeFound("description.specified=true");

        // Get all the targetsList where description is null
        defaultTargetsShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllTargetsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where description contains DEFAULT_DESCRIPTION
        defaultTargetsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the targetsList where description contains UPDATED_DESCRIPTION
        defaultTargetsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTargetsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        targetsRepository.saveAndFlush(targets);

        // Get all the targetsList where description does not contain DEFAULT_DESCRIPTION
        defaultTargetsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the targetsList where description does not contain UPDATED_DESCRIPTION
        defaultTargetsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTargetsShouldBeFound(String filter) throws Exception {
        restTargetsMockMvc.perform(get("/api/targets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(targets.getId().intValue())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].job").value(hasItem(DEFAULT_JOB)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restTargetsMockMvc.perform(get("/api/targets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTargetsShouldNotBeFound(String filter) throws Exception {
        restTargetsMockMvc.perform(get("/api/targets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTargetsMockMvc.perform(get("/api/targets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTargets() throws Exception {
        // Get the targets
        restTargetsMockMvc.perform(get("/api/targets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTargets() throws Exception {
        // Initialize the database
        targetsService.save(targets);

        int databaseSizeBeforeUpdate = targetsRepository.findAll().size();

        // Update the targets
        Targets updatedTargets = targetsRepository.findById(targets.getId()).get();
        // Disconnect from session so that the updates on updatedTargets are not directly saved in db
        em.detach(updatedTargets);
        updatedTargets
            .host(UPDATED_HOST)
            .port(UPDATED_PORT)
            .job(UPDATED_JOB)
            .description(UPDATED_DESCRIPTION);

        restTargetsMockMvc.perform(put("/api/targets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTargets)))
            .andExpect(status().isOk());

        // Validate the Targets in the database
        List<Targets> targetsList = targetsRepository.findAll();
        assertThat(targetsList).hasSize(databaseSizeBeforeUpdate);
        Targets testTargets = targetsList.get(targetsList.size() - 1);
        assertThat(testTargets.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testTargets.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testTargets.getJob()).isEqualTo(UPDATED_JOB);
        assertThat(testTargets.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingTargets() throws Exception {
        int databaseSizeBeforeUpdate = targetsRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTargetsMockMvc.perform(put("/api/targets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(targets)))
            .andExpect(status().isBadRequest());

        // Validate the Targets in the database
        List<Targets> targetsList = targetsRepository.findAll();
        assertThat(targetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTargets() throws Exception {
        // Initialize the database
        targetsService.save(targets);

        int databaseSizeBeforeDelete = targetsRepository.findAll().size();

        // Delete the targets
        restTargetsMockMvc.perform(delete("/api/targets/{id}", targets.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Targets> targetsList = targetsRepository.findAll();
        assertThat(targetsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
