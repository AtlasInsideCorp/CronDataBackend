package com.atlasinside.crondata.web.rest;

import com.atlasinside.crondata.CronDataBackendApp;
import com.atlasinside.crondata.domain.Applications;
import com.atlasinside.crondata.repository.ApplicationsRepository;
import com.atlasinside.crondata.service.ApplicationsQueryService;
import com.atlasinside.crondata.domain.AppCategory;
import com.atlasinside.crondata.service.ApplicationsService;

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
 * Integration tests for the {@link ApplicationsResource} REST controller.
 */
@SpringBootTest(classes = CronDataBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ApplicationsResourceIT {

    private static final String DEFAULT_APP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_APP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_APP_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_APP_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_APP_SHORT = "AAAAAAAAAA";
    private static final String UPDATED_APP_SHORT = "BBBBBBBBBB";

    private static final String DEFAULT_APP_ICON = "AAAAAAAAAA";
    private static final String UPDATED_APP_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_APP_PATH_RESOURCE = "AAAAAAAAAA";
    private static final String UPDATED_APP_PATH_RESOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_APP_INTALL = "AAAAAAAAAA";
    private static final String UPDATED_APP_INTALL = "BBBBBBBBBB";

    @Autowired
    private ApplicationsRepository applicationsRepository;

    @Autowired
    private ApplicationsService applicationsService;

    @Autowired
    private ApplicationsQueryService applicationsQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationsMockMvc;

    private Applications applications;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Applications createEntity(EntityManager em) {
        Applications applications = new Applications()
            .appName(DEFAULT_APP_NAME)
            .appDescription(DEFAULT_APP_DESCRIPTION)
            .appShort(DEFAULT_APP_SHORT)
            .appIcon(DEFAULT_APP_ICON)
            .appPathResource(DEFAULT_APP_PATH_RESOURCE)
            .appIntall(DEFAULT_APP_INTALL);
        return applications;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Applications createUpdatedEntity(EntityManager em) {
        Applications applications = new Applications()
            .appName(UPDATED_APP_NAME)
            .appDescription(UPDATED_APP_DESCRIPTION)
            .appShort(UPDATED_APP_SHORT)
            .appIcon(UPDATED_APP_ICON)
            .appPathResource(UPDATED_APP_PATH_RESOURCE)
            .appIntall(UPDATED_APP_INTALL);
        return applications;
    }

    @BeforeEach
    public void initTest() {
        applications = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplications() throws Exception {
        int databaseSizeBeforeCreate = applicationsRepository.findAll().size();
        // Create the Applications
        restApplicationsMockMvc.perform(post("/api/applications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applications)))
            .andExpect(status().isCreated());

        // Validate the Applications in the database
        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeCreate + 1);
        Applications testApplications = applicationsList.get(applicationsList.size() - 1);
        assertThat(testApplications.getAppName()).isEqualTo(DEFAULT_APP_NAME);
        assertThat(testApplications.getAppDescription()).isEqualTo(DEFAULT_APP_DESCRIPTION);
        assertThat(testApplications.getAppShort()).isEqualTo(DEFAULT_APP_SHORT);
        assertThat(testApplications.getAppIcon()).isEqualTo(DEFAULT_APP_ICON);
        assertThat(testApplications.getAppPathResource()).isEqualTo(DEFAULT_APP_PATH_RESOURCE);
        assertThat(testApplications.getInstallationLink()).isEqualTo(DEFAULT_APP_INTALL);
    }

    @Test
    @Transactional
    public void createApplicationsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationsRepository.findAll().size();

        // Create the Applications with an existing ID
        applications.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationsMockMvc.perform(post("/api/applications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applications)))
            .andExpect(status().isBadRequest());

        // Validate the Applications in the database
        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkAppNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationsRepository.findAll().size();
        // set the field null
        applications.setAppName(null);

        // Create the Applications, which fails.


        restApplicationsMockMvc.perform(post("/api/applications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applications)))
            .andExpect(status().isBadRequest());

        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAppShortIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationsRepository.findAll().size();
        // set the field null
        applications.setAppShort(null);

        // Create the Applications, which fails.


        restApplicationsMockMvc.perform(post("/api/applications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applications)))
            .andExpect(status().isBadRequest());

        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAppPathResourceIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationsRepository.findAll().size();
        // set the field null
        applications.setAppPathResource(null);

        // Create the Applications, which fails.


        restApplicationsMockMvc.perform(post("/api/applications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applications)))
            .andExpect(status().isBadRequest());

        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplications() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList
        restApplicationsMockMvc.perform(get("/api/applications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applications.getId().intValue())))
            .andExpect(jsonPath("$.[*].appName").value(hasItem(DEFAULT_APP_NAME)))
            .andExpect(jsonPath("$.[*].appDescription").value(hasItem(DEFAULT_APP_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].appShort").value(hasItem(DEFAULT_APP_SHORT)))
            .andExpect(jsonPath("$.[*].appIcon").value(hasItem(DEFAULT_APP_ICON)))
            .andExpect(jsonPath("$.[*].appPathResource").value(hasItem(DEFAULT_APP_PATH_RESOURCE)))
            .andExpect(jsonPath("$.[*].appIntall").value(hasItem(DEFAULT_APP_INTALL)));
    }

    @Test
    @Transactional
    public void getApplications() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get the applications
        restApplicationsMockMvc.perform(get("/api/applications/{id}", applications.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applications.getId().intValue()))
            .andExpect(jsonPath("$.appName").value(DEFAULT_APP_NAME))
            .andExpect(jsonPath("$.appDescription").value(DEFAULT_APP_DESCRIPTION))
            .andExpect(jsonPath("$.appShort").value(DEFAULT_APP_SHORT))
            .andExpect(jsonPath("$.appIcon").value(DEFAULT_APP_ICON))
            .andExpect(jsonPath("$.appPathResource").value(DEFAULT_APP_PATH_RESOURCE))
            .andExpect(jsonPath("$.appIntall").value(DEFAULT_APP_INTALL));
    }


    @Test
    @Transactional
    public void getApplicationsByIdFiltering() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        Long id = applications.getId();

        defaultApplicationsShouldBeFound("id.equals=" + id);
        defaultApplicationsShouldNotBeFound("id.notEquals=" + id);

        defaultApplicationsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultApplicationsShouldNotBeFound("id.greaterThan=" + id);

        defaultApplicationsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultApplicationsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllApplicationsByAppNameIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appName equals to DEFAULT_APP_NAME
        defaultApplicationsShouldBeFound("appName.equals=" + DEFAULT_APP_NAME);

        // Get all the applicationsList where appName equals to UPDATED_APP_NAME
        defaultApplicationsShouldNotBeFound("appName.equals=" + UPDATED_APP_NAME);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appName not equals to DEFAULT_APP_NAME
        defaultApplicationsShouldNotBeFound("appName.notEquals=" + DEFAULT_APP_NAME);

        // Get all the applicationsList where appName not equals to UPDATED_APP_NAME
        defaultApplicationsShouldBeFound("appName.notEquals=" + UPDATED_APP_NAME);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppNameIsInShouldWork() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appName in DEFAULT_APP_NAME or UPDATED_APP_NAME
        defaultApplicationsShouldBeFound("appName.in=" + DEFAULT_APP_NAME + "," + UPDATED_APP_NAME);

        // Get all the applicationsList where appName equals to UPDATED_APP_NAME
        defaultApplicationsShouldNotBeFound("appName.in=" + UPDATED_APP_NAME);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appName is not null
        defaultApplicationsShouldBeFound("appName.specified=true");

        // Get all the applicationsList where appName is null
        defaultApplicationsShouldNotBeFound("appName.specified=false");
    }
                @Test
    @Transactional
    public void getAllApplicationsByAppNameContainsSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appName contains DEFAULT_APP_NAME
        defaultApplicationsShouldBeFound("appName.contains=" + DEFAULT_APP_NAME);

        // Get all the applicationsList where appName contains UPDATED_APP_NAME
        defaultApplicationsShouldNotBeFound("appName.contains=" + UPDATED_APP_NAME);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppNameNotContainsSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appName does not contain DEFAULT_APP_NAME
        defaultApplicationsShouldNotBeFound("appName.doesNotContain=" + DEFAULT_APP_NAME);

        // Get all the applicationsList where appName does not contain UPDATED_APP_NAME
        defaultApplicationsShouldBeFound("appName.doesNotContain=" + UPDATED_APP_NAME);
    }


    @Test
    @Transactional
    public void getAllApplicationsByAppDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appDescription equals to DEFAULT_APP_DESCRIPTION
        defaultApplicationsShouldBeFound("appDescription.equals=" + DEFAULT_APP_DESCRIPTION);

        // Get all the applicationsList where appDescription equals to UPDATED_APP_DESCRIPTION
        defaultApplicationsShouldNotBeFound("appDescription.equals=" + UPDATED_APP_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appDescription not equals to DEFAULT_APP_DESCRIPTION
        defaultApplicationsShouldNotBeFound("appDescription.notEquals=" + DEFAULT_APP_DESCRIPTION);

        // Get all the applicationsList where appDescription not equals to UPDATED_APP_DESCRIPTION
        defaultApplicationsShouldBeFound("appDescription.notEquals=" + UPDATED_APP_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appDescription in DEFAULT_APP_DESCRIPTION or UPDATED_APP_DESCRIPTION
        defaultApplicationsShouldBeFound("appDescription.in=" + DEFAULT_APP_DESCRIPTION + "," + UPDATED_APP_DESCRIPTION);

        // Get all the applicationsList where appDescription equals to UPDATED_APP_DESCRIPTION
        defaultApplicationsShouldNotBeFound("appDescription.in=" + UPDATED_APP_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appDescription is not null
        defaultApplicationsShouldBeFound("appDescription.specified=true");

        // Get all the applicationsList where appDescription is null
        defaultApplicationsShouldNotBeFound("appDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllApplicationsByAppDescriptionContainsSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appDescription contains DEFAULT_APP_DESCRIPTION
        defaultApplicationsShouldBeFound("appDescription.contains=" + DEFAULT_APP_DESCRIPTION);

        // Get all the applicationsList where appDescription contains UPDATED_APP_DESCRIPTION
        defaultApplicationsShouldNotBeFound("appDescription.contains=" + UPDATED_APP_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appDescription does not contain DEFAULT_APP_DESCRIPTION
        defaultApplicationsShouldNotBeFound("appDescription.doesNotContain=" + DEFAULT_APP_DESCRIPTION);

        // Get all the applicationsList where appDescription does not contain UPDATED_APP_DESCRIPTION
        defaultApplicationsShouldBeFound("appDescription.doesNotContain=" + UPDATED_APP_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllApplicationsByAppShortIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appShort equals to DEFAULT_APP_SHORT
        defaultApplicationsShouldBeFound("appShort.equals=" + DEFAULT_APP_SHORT);

        // Get all the applicationsList where appShort equals to UPDATED_APP_SHORT
        defaultApplicationsShouldNotBeFound("appShort.equals=" + UPDATED_APP_SHORT);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppShortIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appShort not equals to DEFAULT_APP_SHORT
        defaultApplicationsShouldNotBeFound("appShort.notEquals=" + DEFAULT_APP_SHORT);

        // Get all the applicationsList where appShort not equals to UPDATED_APP_SHORT
        defaultApplicationsShouldBeFound("appShort.notEquals=" + UPDATED_APP_SHORT);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppShortIsInShouldWork() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appShort in DEFAULT_APP_SHORT or UPDATED_APP_SHORT
        defaultApplicationsShouldBeFound("appShort.in=" + DEFAULT_APP_SHORT + "," + UPDATED_APP_SHORT);

        // Get all the applicationsList where appShort equals to UPDATED_APP_SHORT
        defaultApplicationsShouldNotBeFound("appShort.in=" + UPDATED_APP_SHORT);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppShortIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appShort is not null
        defaultApplicationsShouldBeFound("appShort.specified=true");

        // Get all the applicationsList where appShort is null
        defaultApplicationsShouldNotBeFound("appShort.specified=false");
    }
                @Test
    @Transactional
    public void getAllApplicationsByAppShortContainsSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appShort contains DEFAULT_APP_SHORT
        defaultApplicationsShouldBeFound("appShort.contains=" + DEFAULT_APP_SHORT);

        // Get all the applicationsList where appShort contains UPDATED_APP_SHORT
        defaultApplicationsShouldNotBeFound("appShort.contains=" + UPDATED_APP_SHORT);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppShortNotContainsSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appShort does not contain DEFAULT_APP_SHORT
        defaultApplicationsShouldNotBeFound("appShort.doesNotContain=" + DEFAULT_APP_SHORT);

        // Get all the applicationsList where appShort does not contain UPDATED_APP_SHORT
        defaultApplicationsShouldBeFound("appShort.doesNotContain=" + UPDATED_APP_SHORT);
    }


    @Test
    @Transactional
    public void getAllApplicationsByAppIconIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appIcon equals to DEFAULT_APP_ICON
        defaultApplicationsShouldBeFound("appIcon.equals=" + DEFAULT_APP_ICON);

        // Get all the applicationsList where appIcon equals to UPDATED_APP_ICON
        defaultApplicationsShouldNotBeFound("appIcon.equals=" + UPDATED_APP_ICON);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppIconIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appIcon not equals to DEFAULT_APP_ICON
        defaultApplicationsShouldNotBeFound("appIcon.notEquals=" + DEFAULT_APP_ICON);

        // Get all the applicationsList where appIcon not equals to UPDATED_APP_ICON
        defaultApplicationsShouldBeFound("appIcon.notEquals=" + UPDATED_APP_ICON);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppIconIsInShouldWork() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appIcon in DEFAULT_APP_ICON or UPDATED_APP_ICON
        defaultApplicationsShouldBeFound("appIcon.in=" + DEFAULT_APP_ICON + "," + UPDATED_APP_ICON);

        // Get all the applicationsList where appIcon equals to UPDATED_APP_ICON
        defaultApplicationsShouldNotBeFound("appIcon.in=" + UPDATED_APP_ICON);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppIconIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appIcon is not null
        defaultApplicationsShouldBeFound("appIcon.specified=true");

        // Get all the applicationsList where appIcon is null
        defaultApplicationsShouldNotBeFound("appIcon.specified=false");
    }
                @Test
    @Transactional
    public void getAllApplicationsByAppIconContainsSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appIcon contains DEFAULT_APP_ICON
        defaultApplicationsShouldBeFound("appIcon.contains=" + DEFAULT_APP_ICON);

        // Get all the applicationsList where appIcon contains UPDATED_APP_ICON
        defaultApplicationsShouldNotBeFound("appIcon.contains=" + UPDATED_APP_ICON);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppIconNotContainsSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appIcon does not contain DEFAULT_APP_ICON
        defaultApplicationsShouldNotBeFound("appIcon.doesNotContain=" + DEFAULT_APP_ICON);

        // Get all the applicationsList where appIcon does not contain UPDATED_APP_ICON
        defaultApplicationsShouldBeFound("appIcon.doesNotContain=" + UPDATED_APP_ICON);
    }


    @Test
    @Transactional
    public void getAllApplicationsByAppPathResourceIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appPathResource equals to DEFAULT_APP_PATH_RESOURCE
        defaultApplicationsShouldBeFound("appPathResource.equals=" + DEFAULT_APP_PATH_RESOURCE);

        // Get all the applicationsList where appPathResource equals to UPDATED_APP_PATH_RESOURCE
        defaultApplicationsShouldNotBeFound("appPathResource.equals=" + UPDATED_APP_PATH_RESOURCE);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppPathResourceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appPathResource not equals to DEFAULT_APP_PATH_RESOURCE
        defaultApplicationsShouldNotBeFound("appPathResource.notEquals=" + DEFAULT_APP_PATH_RESOURCE);

        // Get all the applicationsList where appPathResource not equals to UPDATED_APP_PATH_RESOURCE
        defaultApplicationsShouldBeFound("appPathResource.notEquals=" + UPDATED_APP_PATH_RESOURCE);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppPathResourceIsInShouldWork() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appPathResource in DEFAULT_APP_PATH_RESOURCE or UPDATED_APP_PATH_RESOURCE
        defaultApplicationsShouldBeFound("appPathResource.in=" + DEFAULT_APP_PATH_RESOURCE + "," + UPDATED_APP_PATH_RESOURCE);

        // Get all the applicationsList where appPathResource equals to UPDATED_APP_PATH_RESOURCE
        defaultApplicationsShouldNotBeFound("appPathResource.in=" + UPDATED_APP_PATH_RESOURCE);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppPathResourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appPathResource is not null
        defaultApplicationsShouldBeFound("appPathResource.specified=true");

        // Get all the applicationsList where appPathResource is null
        defaultApplicationsShouldNotBeFound("appPathResource.specified=false");
    }
                @Test
    @Transactional
    public void getAllApplicationsByAppPathResourceContainsSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appPathResource contains DEFAULT_APP_PATH_RESOURCE
        defaultApplicationsShouldBeFound("appPathResource.contains=" + DEFAULT_APP_PATH_RESOURCE);

        // Get all the applicationsList where appPathResource contains UPDATED_APP_PATH_RESOURCE
        defaultApplicationsShouldNotBeFound("appPathResource.contains=" + UPDATED_APP_PATH_RESOURCE);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppPathResourceNotContainsSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appPathResource does not contain DEFAULT_APP_PATH_RESOURCE
        defaultApplicationsShouldNotBeFound("appPathResource.doesNotContain=" + DEFAULT_APP_PATH_RESOURCE);

        // Get all the applicationsList where appPathResource does not contain UPDATED_APP_PATH_RESOURCE
        defaultApplicationsShouldBeFound("appPathResource.doesNotContain=" + UPDATED_APP_PATH_RESOURCE);
    }


    @Test
    @Transactional
    public void getAllApplicationsByAppIntallIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appIntall equals to DEFAULT_APP_INTALL
        defaultApplicationsShouldBeFound("appIntall.equals=" + DEFAULT_APP_INTALL);

        // Get all the applicationsList where appIntall equals to UPDATED_APP_INTALL
        defaultApplicationsShouldNotBeFound("appIntall.equals=" + UPDATED_APP_INTALL);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppIntallIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appIntall not equals to DEFAULT_APP_INTALL
        defaultApplicationsShouldNotBeFound("appIntall.notEquals=" + DEFAULT_APP_INTALL);

        // Get all the applicationsList where appIntall not equals to UPDATED_APP_INTALL
        defaultApplicationsShouldBeFound("appIntall.notEquals=" + UPDATED_APP_INTALL);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppIntallIsInShouldWork() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appIntall in DEFAULT_APP_INTALL or UPDATED_APP_INTALL
        defaultApplicationsShouldBeFound("appIntall.in=" + DEFAULT_APP_INTALL + "," + UPDATED_APP_INTALL);

        // Get all the applicationsList where appIntall equals to UPDATED_APP_INTALL
        defaultApplicationsShouldNotBeFound("appIntall.in=" + UPDATED_APP_INTALL);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppIntallIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appIntall is not null
        defaultApplicationsShouldBeFound("appIntall.specified=true");

        // Get all the applicationsList where appIntall is null
        defaultApplicationsShouldNotBeFound("appIntall.specified=false");
    }
                @Test
    @Transactional
    public void getAllApplicationsByAppIntallContainsSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appIntall contains DEFAULT_APP_INTALL
        defaultApplicationsShouldBeFound("appIntall.contains=" + DEFAULT_APP_INTALL);

        // Get all the applicationsList where appIntall contains UPDATED_APP_INTALL
        defaultApplicationsShouldNotBeFound("appIntall.contains=" + UPDATED_APP_INTALL);
    }

    @Test
    @Transactional
    public void getAllApplicationsByAppIntallNotContainsSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);

        // Get all the applicationsList where appIntall does not contain DEFAULT_APP_INTALL
        defaultApplicationsShouldNotBeFound("appIntall.doesNotContain=" + DEFAULT_APP_INTALL);

        // Get all the applicationsList where appIntall does not contain UPDATED_APP_INTALL
        defaultApplicationsShouldBeFound("appIntall.doesNotContain=" + UPDATED_APP_INTALL);
    }


    @Test
    @Transactional
    public void getAllApplicationsByApp_categoryIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationsRepository.saveAndFlush(applications);
        AppCategory app_category = AppCategoryResourceIT.createEntity(em);
        em.persist(app_category);
        em.flush();
        applications.setAppCategory(app_category);
        applicationsRepository.saveAndFlush(applications);
        Long app_categoryId = app_category.getId();

        // Get all the applicationsList where app_category equals to app_categoryId
        defaultApplicationsShouldBeFound("app_categoryId.equals=" + app_categoryId);

        // Get all the applicationsList where app_category equals to app_categoryId + 1
        defaultApplicationsShouldNotBeFound("app_categoryId.equals=" + (app_categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultApplicationsShouldBeFound(String filter) throws Exception {
        restApplicationsMockMvc.perform(get("/api/applications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applications.getId().intValue())))
            .andExpect(jsonPath("$.[*].appName").value(hasItem(DEFAULT_APP_NAME)))
            .andExpect(jsonPath("$.[*].appDescription").value(hasItem(DEFAULT_APP_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].appShort").value(hasItem(DEFAULT_APP_SHORT)))
            .andExpect(jsonPath("$.[*].appIcon").value(hasItem(DEFAULT_APP_ICON)))
            .andExpect(jsonPath("$.[*].appPathResource").value(hasItem(DEFAULT_APP_PATH_RESOURCE)))
            .andExpect(jsonPath("$.[*].appIntall").value(hasItem(DEFAULT_APP_INTALL)));

        // Check, that the count call also returns 1
        restApplicationsMockMvc.perform(get("/api/applications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultApplicationsShouldNotBeFound(String filter) throws Exception {
        restApplicationsMockMvc.perform(get("/api/applications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApplicationsMockMvc.perform(get("/api/applications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingApplications() throws Exception {
        // Get the applications
        restApplicationsMockMvc.perform(get("/api/applications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplications() throws Exception {
        // Initialize the database
        applicationsService.save(applications);

        int databaseSizeBeforeUpdate = applicationsRepository.findAll().size();

        // Update the applications
        Applications updatedApplications = applicationsRepository.findById(applications.getId()).get();
        // Disconnect from session so that the updates on updatedApplications are not directly saved in db
        em.detach(updatedApplications);
        updatedApplications
            .appName(UPDATED_APP_NAME)
            .appDescription(UPDATED_APP_DESCRIPTION)
            .appShort(UPDATED_APP_SHORT)
            .appIcon(UPDATED_APP_ICON)
            .appPathResource(UPDATED_APP_PATH_RESOURCE)
            .appIntall(UPDATED_APP_INTALL);

        restApplicationsMockMvc.perform(put("/api/applications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedApplications)))
            .andExpect(status().isOk());

        // Validate the Applications in the database
        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeUpdate);
        Applications testApplications = applicationsList.get(applicationsList.size() - 1);
        assertThat(testApplications.getAppName()).isEqualTo(UPDATED_APP_NAME);
        assertThat(testApplications.getAppDescription()).isEqualTo(UPDATED_APP_DESCRIPTION);
        assertThat(testApplications.getAppShort()).isEqualTo(UPDATED_APP_SHORT);
        assertThat(testApplications.getAppIcon()).isEqualTo(UPDATED_APP_ICON);
        assertThat(testApplications.getAppPathResource()).isEqualTo(UPDATED_APP_PATH_RESOURCE);
        assertThat(testApplications.getInstallationLink()).isEqualTo(UPDATED_APP_INTALL);
    }

    @Test
    @Transactional
    public void updateNonExistingApplications() throws Exception {
        int databaseSizeBeforeUpdate = applicationsRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationsMockMvc.perform(put("/api/applications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applications)))
            .andExpect(status().isBadRequest());

        // Validate the Applications in the database
        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApplications() throws Exception {
        // Initialize the database
        applicationsService.save(applications);

        int databaseSizeBeforeDelete = applicationsRepository.findAll().size();

        // Delete the applications
        restApplicationsMockMvc.perform(delete("/api/applications/{id}", applications.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
