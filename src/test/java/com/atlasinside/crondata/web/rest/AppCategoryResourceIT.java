package com.atlasinside.crondata.web.rest;

import com.atlasinside.crondata.CronDataBackendApp;
import com.atlasinside.crondata.repository.AppCategoryRepository;
import com.atlasinside.crondata.domain.AppCategory;
import com.atlasinside.crondata.service.AppCategoryService;

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
 * Integration tests for the {@link AppCategoryResource} REST controller.
 */
@SpringBootTest(classes = CronDataBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AppCategoryResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private AppCategoryRepository appCategoryRepository;

    @Autowired
    private AppCategoryService appCategoryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppCategoryMockMvc;

    private AppCategory appCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppCategory createEntity(EntityManager em) {
        AppCategory appCategory = new AppCategory()
            .category_name(DEFAULT_CATEGORY_NAME)
            .category_description(DEFAULT_CATEGORY_DESCRIPTION);
        return appCategory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppCategory createUpdatedEntity(EntityManager em) {
        AppCategory appCategory = new AppCategory()
            .category_name(UPDATED_CATEGORY_NAME)
            .category_description(UPDATED_CATEGORY_DESCRIPTION);
        return appCategory;
    }

    @BeforeEach
    public void initTest() {
        appCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppCategory() throws Exception {
        int databaseSizeBeforeCreate = appCategoryRepository.findAll().size();
        // Create the AppCategory
        restAppCategoryMockMvc.perform(post("/api/app-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appCategory)))
            .andExpect(status().isCreated());

        // Validate the AppCategory in the database
        List<AppCategory> appCategoryList = appCategoryRepository.findAll();
        assertThat(appCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        AppCategory testAppCategory = appCategoryList.get(appCategoryList.size() - 1);
        assertThat(testAppCategory.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testAppCategory.getCategoryDescription()).isEqualTo(DEFAULT_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createAppCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appCategoryRepository.findAll().size();

        // Create the AppCategory with an existing ID
        appCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppCategoryMockMvc.perform(post("/api/app-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appCategory)))
            .andExpect(status().isBadRequest());

        // Validate the AppCategory in the database
        List<AppCategory> appCategoryList = appCategoryRepository.findAll();
        assertThat(appCategoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCategory_nameIsRequired() throws Exception {
        int databaseSizeBeforeTest = appCategoryRepository.findAll().size();
        // set the field null
        appCategory.setCategoryName(null);

        // Create the AppCategory, which fails.


        restAppCategoryMockMvc.perform(post("/api/app-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appCategory)))
            .andExpect(status().isBadRequest());

        List<AppCategory> appCategoryList = appCategoryRepository.findAll();
        assertThat(appCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAppCategories() throws Exception {
        // Initialize the database
        appCategoryRepository.saveAndFlush(appCategory);

        // Get all the appCategoryList
        restAppCategoryMockMvc.perform(get("/api/app-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].category_name").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].category_description").value(hasItem(DEFAULT_CATEGORY_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void getAppCategory() throws Exception {
        // Initialize the database
        appCategoryRepository.saveAndFlush(appCategory);

        // Get the appCategory
        restAppCategoryMockMvc.perform(get("/api/app-categories/{id}", appCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appCategory.getId().intValue()))
            .andExpect(jsonPath("$.category_name").value(DEFAULT_CATEGORY_NAME))
            .andExpect(jsonPath("$.category_description").value(DEFAULT_CATEGORY_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingAppCategory() throws Exception {
        // Get the appCategory
        restAppCategoryMockMvc.perform(get("/api/app-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppCategory() throws Exception {
        // Initialize the database
        appCategoryService.save(appCategory);

        int databaseSizeBeforeUpdate = appCategoryRepository.findAll().size();

        // Update the appCategory
        AppCategory updatedAppCategory = appCategoryRepository.findById(appCategory.getId()).get();
        // Disconnect from session so that the updates on updatedAppCategory are not directly saved in db
        em.detach(updatedAppCategory);
        updatedAppCategory
            .category_name(UPDATED_CATEGORY_NAME)
            .category_description(UPDATED_CATEGORY_DESCRIPTION);

        restAppCategoryMockMvc.perform(put("/api/app-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAppCategory)))
            .andExpect(status().isOk());

        // Validate the AppCategory in the database
        List<AppCategory> appCategoryList = appCategoryRepository.findAll();
        assertThat(appCategoryList).hasSize(databaseSizeBeforeUpdate);
        AppCategory testAppCategory = appCategoryList.get(appCategoryList.size() - 1);
        assertThat(testAppCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testAppCategory.getCategoryDescription()).isEqualTo(UPDATED_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingAppCategory() throws Exception {
        int databaseSizeBeforeUpdate = appCategoryRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppCategoryMockMvc.perform(put("/api/app-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appCategory)))
            .andExpect(status().isBadRequest());

        // Validate the AppCategory in the database
        List<AppCategory> appCategoryList = appCategoryRepository.findAll();
        assertThat(appCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAppCategory() throws Exception {
        // Initialize the database
        appCategoryService.save(appCategory);

        int databaseSizeBeforeDelete = appCategoryRepository.findAll().size();

        // Delete the appCategory
        restAppCategoryMockMvc.perform(delete("/api/app-categories/{id}", appCategory.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppCategory> appCategoryList = appCategoryRepository.findAll();
        assertThat(appCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
