package com.atlasinside.crondata.web.rest;

import com.atlasinside.crondata.CronDataBackendApp;
import com.atlasinside.crondata.repository.AlertsRepository;
import com.atlasinside.crondata.domain.Alerts;
import com.atlasinside.crondata.service.AlertsService;
import com.atlasinside.crondata.service.AlertsQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AlertsResource} REST controller.
 */
@SpringBootTest(classes = CronDataBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AlertsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SEVERITY = "AAAAAAAAAA";
    private static final String UPDATED_SEVERITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_READED = false;
    private static final Boolean UPDATED_READED = false;

    private static final Instant DEFAULT_RECEIVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECEIVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AlertsRepository alertsRepository;

    @Autowired
    private AlertsService alertsService;

    @Autowired
    private AlertsQueryService alertsQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlertsMockMvc;

    private Alerts alerts;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alerts createEntity(EntityManager em) {
        Alerts alerts = new Alerts()
            .name(DEFAULT_NAME)
            .summary(DEFAULT_SUMMARY)
            .description(DEFAULT_DESCRIPTION)
            .severity(DEFAULT_SEVERITY)
            .status(DEFAULT_STATUS)
            .readed(DEFAULT_READED)
            .receivedAt(DEFAULT_RECEIVED_AT);
        return alerts;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alerts createUpdatedEntity(EntityManager em) {
        Alerts alerts = new Alerts()
            .name(UPDATED_NAME)
            .summary(UPDATED_SUMMARY)
            .description(UPDATED_DESCRIPTION)
            .severity(UPDATED_SEVERITY)
            .status(UPDATED_STATUS)
            .readed(UPDATED_READED)
            .receivedAt(UPDATED_RECEIVED_AT);
        return alerts;
    }

    @BeforeEach
    public void initTest() {
        alerts = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlerts() throws Exception {
        int databaseSizeBeforeCreate = alertsRepository.findAll().size();
        // Create the Alerts
        restAlertsMockMvc.perform(post("/api/alerts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alerts)))
            .andExpect(status().isCreated());

        // Validate the Alerts in the database
        List<Alerts> alertsList = alertsRepository.findAll();
        assertThat(alertsList).hasSize(databaseSizeBeforeCreate + 1);
        Alerts testAlerts = alertsList.get(alertsList.size() - 1);
        assertThat(testAlerts.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAlerts.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testAlerts.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAlerts.getSeverity()).isEqualTo(DEFAULT_SEVERITY);
        assertThat(testAlerts.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAlerts.getReaded()).isEqualTo(DEFAULT_READED);
        assertThat(testAlerts.getReceivedAt()).isEqualTo(DEFAULT_RECEIVED_AT);
    }

    @Test
    @Transactional
    public void createAlertsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = alertsRepository.findAll().size();

        // Create the Alerts with an existing ID
        alerts.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlertsMockMvc.perform(post("/api/alerts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alerts)))
            .andExpect(status().isBadRequest());

        // Validate the Alerts in the database
        List<Alerts> alertsList = alertsRepository.findAll();
        assertThat(alertsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAlerts() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList
        restAlertsMockMvc.perform(get("/api/alerts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alerts.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].readed").value(hasItem(DEFAULT_READED)))
            .andExpect(jsonPath("$.[*].receivedAt").value(hasItem(DEFAULT_RECEIVED_AT.toString())));
    }

    @Test
    @Transactional
    public void getAlerts() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get the alerts
        restAlertsMockMvc.perform(get("/api/alerts/{id}", alerts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alerts.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.severity").value(DEFAULT_SEVERITY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.readed").value(DEFAULT_READED))
            .andExpect(jsonPath("$.receivedAt").value(DEFAULT_RECEIVED_AT.toString()));
    }


    @Test
    @Transactional
    public void getAlertsByIdFiltering() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        Long id = alerts.getId();

        defaultAlertsShouldBeFound("id.equals=" + id);
        defaultAlertsShouldNotBeFound("id.notEquals=" + id);

        defaultAlertsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAlertsShouldNotBeFound("id.greaterThan=" + id);

        defaultAlertsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAlertsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAlertsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where name equals to DEFAULT_NAME
        defaultAlertsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the alertsList where name equals to UPDATED_NAME
        defaultAlertsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAlertsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where name not equals to DEFAULT_NAME
        defaultAlertsShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the alertsList where name not equals to UPDATED_NAME
        defaultAlertsShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAlertsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAlertsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the alertsList where name equals to UPDATED_NAME
        defaultAlertsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAlertsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where name is not null
        defaultAlertsShouldBeFound("name.specified=true");

        // Get all the alertsList where name is null
        defaultAlertsShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlertsByNameContainsSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where name contains DEFAULT_NAME
        defaultAlertsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the alertsList where name contains UPDATED_NAME
        defaultAlertsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAlertsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where name does not contain DEFAULT_NAME
        defaultAlertsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the alertsList where name does not contain UPDATED_NAME
        defaultAlertsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllAlertsBySummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where summary equals to DEFAULT_SUMMARY
        defaultAlertsShouldBeFound("summary.equals=" + DEFAULT_SUMMARY);

        // Get all the alertsList where summary equals to UPDATED_SUMMARY
        defaultAlertsShouldNotBeFound("summary.equals=" + UPDATED_SUMMARY);
    }

    @Test
    @Transactional
    public void getAllAlertsBySummaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where summary not equals to DEFAULT_SUMMARY
        defaultAlertsShouldNotBeFound("summary.notEquals=" + DEFAULT_SUMMARY);

        // Get all the alertsList where summary not equals to UPDATED_SUMMARY
        defaultAlertsShouldBeFound("summary.notEquals=" + UPDATED_SUMMARY);
    }

    @Test
    @Transactional
    public void getAllAlertsBySummaryIsInShouldWork() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where summary in DEFAULT_SUMMARY or UPDATED_SUMMARY
        defaultAlertsShouldBeFound("summary.in=" + DEFAULT_SUMMARY + "," + UPDATED_SUMMARY);

        // Get all the alertsList where summary equals to UPDATED_SUMMARY
        defaultAlertsShouldNotBeFound("summary.in=" + UPDATED_SUMMARY);
    }

    @Test
    @Transactional
    public void getAllAlertsBySummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where summary is not null
        defaultAlertsShouldBeFound("summary.specified=true");

        // Get all the alertsList where summary is null
        defaultAlertsShouldNotBeFound("summary.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlertsBySummaryContainsSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where summary contains DEFAULT_SUMMARY
        defaultAlertsShouldBeFound("summary.contains=" + DEFAULT_SUMMARY);

        // Get all the alertsList where summary contains UPDATED_SUMMARY
        defaultAlertsShouldNotBeFound("summary.contains=" + UPDATED_SUMMARY);
    }

    @Test
    @Transactional
    public void getAllAlertsBySummaryNotContainsSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where summary does not contain DEFAULT_SUMMARY
        defaultAlertsShouldNotBeFound("summary.doesNotContain=" + DEFAULT_SUMMARY);

        // Get all the alertsList where summary does not contain UPDATED_SUMMARY
        defaultAlertsShouldBeFound("summary.doesNotContain=" + UPDATED_SUMMARY);
    }


    @Test
    @Transactional
    public void getAllAlertsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where description equals to DEFAULT_DESCRIPTION
        defaultAlertsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the alertsList where description equals to UPDATED_DESCRIPTION
        defaultAlertsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAlertsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where description not equals to DEFAULT_DESCRIPTION
        defaultAlertsShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the alertsList where description not equals to UPDATED_DESCRIPTION
        defaultAlertsShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAlertsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAlertsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the alertsList where description equals to UPDATED_DESCRIPTION
        defaultAlertsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAlertsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where description is not null
        defaultAlertsShouldBeFound("description.specified=true");

        // Get all the alertsList where description is null
        defaultAlertsShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlertsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where description contains DEFAULT_DESCRIPTION
        defaultAlertsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the alertsList where description contains UPDATED_DESCRIPTION
        defaultAlertsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAlertsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where description does not contain DEFAULT_DESCRIPTION
        defaultAlertsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the alertsList where description does not contain UPDATED_DESCRIPTION
        defaultAlertsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllAlertsBySeverityIsEqualToSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where severity equals to DEFAULT_SEVERITY
        defaultAlertsShouldBeFound("severity.equals=" + DEFAULT_SEVERITY);

        // Get all the alertsList where severity equals to UPDATED_SEVERITY
        defaultAlertsShouldNotBeFound("severity.equals=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    public void getAllAlertsBySeverityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where severity not equals to DEFAULT_SEVERITY
        defaultAlertsShouldNotBeFound("severity.notEquals=" + DEFAULT_SEVERITY);

        // Get all the alertsList where severity not equals to UPDATED_SEVERITY
        defaultAlertsShouldBeFound("severity.notEquals=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    public void getAllAlertsBySeverityIsInShouldWork() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where severity in DEFAULT_SEVERITY or UPDATED_SEVERITY
        defaultAlertsShouldBeFound("severity.in=" + DEFAULT_SEVERITY + "," + UPDATED_SEVERITY);

        // Get all the alertsList where severity equals to UPDATED_SEVERITY
        defaultAlertsShouldNotBeFound("severity.in=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    public void getAllAlertsBySeverityIsNullOrNotNull() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where severity is not null
        defaultAlertsShouldBeFound("severity.specified=true");

        // Get all the alertsList where severity is null
        defaultAlertsShouldNotBeFound("severity.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlertsBySeverityContainsSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where severity contains DEFAULT_SEVERITY
        defaultAlertsShouldBeFound("severity.contains=" + DEFAULT_SEVERITY);

        // Get all the alertsList where severity contains UPDATED_SEVERITY
        defaultAlertsShouldNotBeFound("severity.contains=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    public void getAllAlertsBySeverityNotContainsSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where severity does not contain DEFAULT_SEVERITY
        defaultAlertsShouldNotBeFound("severity.doesNotContain=" + DEFAULT_SEVERITY);

        // Get all the alertsList where severity does not contain UPDATED_SEVERITY
        defaultAlertsShouldBeFound("severity.doesNotContain=" + UPDATED_SEVERITY);
    }


    @Test
    @Transactional
    public void getAllAlertsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where status equals to DEFAULT_STATUS
        defaultAlertsShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the alertsList where status equals to UPDATED_STATUS
        defaultAlertsShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAlertsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where status not equals to DEFAULT_STATUS
        defaultAlertsShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the alertsList where status not equals to UPDATED_STATUS
        defaultAlertsShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAlertsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultAlertsShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the alertsList where status equals to UPDATED_STATUS
        defaultAlertsShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAlertsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where status is not null
        defaultAlertsShouldBeFound("status.specified=true");

        // Get all the alertsList where status is null
        defaultAlertsShouldNotBeFound("status.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlertsByStatusContainsSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where status contains DEFAULT_STATUS
        defaultAlertsShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the alertsList where status contains UPDATED_STATUS
        defaultAlertsShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAlertsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where status does not contain DEFAULT_STATUS
        defaultAlertsShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the alertsList where status does not contain UPDATED_STATUS
        defaultAlertsShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }


    @Test
    @Transactional
    public void getAllAlertsByReadedIsEqualToSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where readed equals to DEFAULT_READED
        defaultAlertsShouldBeFound("readed.equals=" + DEFAULT_READED);

        // Get all the alertsList where readed equals to UPDATED_READED
        defaultAlertsShouldNotBeFound("readed.equals=" + UPDATED_READED);
    }

    @Test
    @Transactional
    public void getAllAlertsByReadedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where readed not equals to DEFAULT_READED
        defaultAlertsShouldNotBeFound("readed.notEquals=" + DEFAULT_READED);

        // Get all the alertsList where readed not equals to UPDATED_READED
        defaultAlertsShouldBeFound("readed.notEquals=" + UPDATED_READED);
    }

    @Test
    @Transactional
    public void getAllAlertsByReadedIsInShouldWork() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where readed in DEFAULT_READED or UPDATED_READED
        defaultAlertsShouldBeFound("readed.in=" + DEFAULT_READED + "," + UPDATED_READED);

        // Get all the alertsList where readed equals to UPDATED_READED
        defaultAlertsShouldNotBeFound("readed.in=" + UPDATED_READED);
    }

    @Test
    @Transactional
    public void getAllAlertsByReadedIsNullOrNotNull() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where readed is not null
        defaultAlertsShouldBeFound("readed.specified=true");

        // Get all the alertsList where readed is null
        defaultAlertsShouldNotBeFound("readed.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlertsByReadedContainsSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where readed contains DEFAULT_READED
        defaultAlertsShouldBeFound("readed.contains=" + DEFAULT_READED);

        // Get all the alertsList where readed contains UPDATED_READED
        defaultAlertsShouldNotBeFound("readed.contains=" + UPDATED_READED);
    }

    @Test
    @Transactional
    public void getAllAlertsByReadedNotContainsSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where readed does not contain DEFAULT_READED
        defaultAlertsShouldNotBeFound("readed.doesNotContain=" + DEFAULT_READED);

        // Get all the alertsList where readed does not contain UPDATED_READED
        defaultAlertsShouldBeFound("readed.doesNotContain=" + UPDATED_READED);
    }


    @Test
    @Transactional
    public void getAllAlertsByReceivedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where receivedAt equals to DEFAULT_RECEIVED_AT
        defaultAlertsShouldBeFound("receivedAt.equals=" + DEFAULT_RECEIVED_AT);

        // Get all the alertsList where receivedAt equals to UPDATED_RECEIVED_AT
        defaultAlertsShouldNotBeFound("receivedAt.equals=" + UPDATED_RECEIVED_AT);
    }

    @Test
    @Transactional
    public void getAllAlertsByReceivedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where receivedAt not equals to DEFAULT_RECEIVED_AT
        defaultAlertsShouldNotBeFound("receivedAt.notEquals=" + DEFAULT_RECEIVED_AT);

        // Get all the alertsList where receivedAt not equals to UPDATED_RECEIVED_AT
        defaultAlertsShouldBeFound("receivedAt.notEquals=" + UPDATED_RECEIVED_AT);
    }

    @Test
    @Transactional
    public void getAllAlertsByReceivedAtIsInShouldWork() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where receivedAt in DEFAULT_RECEIVED_AT or UPDATED_RECEIVED_AT
        defaultAlertsShouldBeFound("receivedAt.in=" + DEFAULT_RECEIVED_AT + "," + UPDATED_RECEIVED_AT);

        // Get all the alertsList where receivedAt equals to UPDATED_RECEIVED_AT
        defaultAlertsShouldNotBeFound("receivedAt.in=" + UPDATED_RECEIVED_AT);
    }

    @Test
    @Transactional
    public void getAllAlertsByReceivedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        alertsRepository.saveAndFlush(alerts);

        // Get all the alertsList where receivedAt is not null
        defaultAlertsShouldBeFound("receivedAt.specified=true");

        // Get all the alertsList where receivedAt is null
        defaultAlertsShouldNotBeFound("receivedAt.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlertsShouldBeFound(String filter) throws Exception {
        restAlertsMockMvc.perform(get("/api/alerts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alerts.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].readed").value(hasItem(DEFAULT_READED)))
            .andExpect(jsonPath("$.[*].receivedAt").value(hasItem(DEFAULT_RECEIVED_AT.toString())));

        // Check, that the count call also returns 1
        restAlertsMockMvc.perform(get("/api/alerts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlertsShouldNotBeFound(String filter) throws Exception {
        restAlertsMockMvc.perform(get("/api/alerts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlertsMockMvc.perform(get("/api/alerts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAlerts() throws Exception {
        // Get the alerts
        restAlertsMockMvc.perform(get("/api/alerts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlerts() throws Exception {
        // Initialize the database
        alertsService.save(alerts);

        int databaseSizeBeforeUpdate = alertsRepository.findAll().size();

        // Update the alerts
        Alerts updatedAlerts = alertsRepository.findById(alerts.getId()).get();
        // Disconnect from session so that the updates on updatedAlerts are not directly saved in db
        em.detach(updatedAlerts);
        updatedAlerts
            .name(UPDATED_NAME)
            .summary(UPDATED_SUMMARY)
            .description(UPDATED_DESCRIPTION)
            .severity(UPDATED_SEVERITY)
            .status(UPDATED_STATUS)
            .readed(UPDATED_READED)
            .receivedAt(UPDATED_RECEIVED_AT);

        restAlertsMockMvc.perform(put("/api/alerts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAlerts)))
            .andExpect(status().isOk());

        // Validate the Alerts in the database
        List<Alerts> alertsList = alertsRepository.findAll();
        assertThat(alertsList).hasSize(databaseSizeBeforeUpdate);
        Alerts testAlerts = alertsList.get(alertsList.size() - 1);
        assertThat(testAlerts.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAlerts.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testAlerts.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAlerts.getSeverity()).isEqualTo(UPDATED_SEVERITY);
        assertThat(testAlerts.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAlerts.getReaded()).isEqualTo(UPDATED_READED);
        assertThat(testAlerts.getReceivedAt()).isEqualTo(UPDATED_RECEIVED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingAlerts() throws Exception {
        int databaseSizeBeforeUpdate = alertsRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertsMockMvc.perform(put("/api/alerts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(alerts)))
            .andExpect(status().isBadRequest());

        // Validate the Alerts in the database
        List<Alerts> alertsList = alertsRepository.findAll();
        assertThat(alertsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAlerts() throws Exception {
        // Initialize the database
        alertsService.save(alerts);

        int databaseSizeBeforeDelete = alertsRepository.findAll().size();

        // Delete the alerts
        restAlertsMockMvc.perform(delete("/api/alerts/{id}", alerts.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Alerts> alertsList = alertsRepository.findAll();
        assertThat(alertsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
