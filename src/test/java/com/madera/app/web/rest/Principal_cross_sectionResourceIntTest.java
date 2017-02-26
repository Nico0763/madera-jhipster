package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Principal_cross_section;
import com.madera.app.repository.Principal_cross_sectionRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the Principal_cross_sectionResource REST controller.
 *
 * @see Principal_cross_sectionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class Principal_cross_sectionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    @Inject
    private Principal_cross_sectionRepository principal_cross_sectionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPrincipal_cross_sectionMockMvc;

    private Principal_cross_section principal_cross_section;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Principal_cross_sectionResource principal_cross_sectionResource = new Principal_cross_sectionResource();
        ReflectionTestUtils.setField(principal_cross_sectionResource, "principal_cross_sectionRepository", principal_cross_sectionRepository);
        this.restPrincipal_cross_sectionMockMvc = MockMvcBuilders.standaloneSetup(principal_cross_sectionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Principal_cross_section createEntity(EntityManager em) {
        Principal_cross_section principal_cross_section = new Principal_cross_section()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .url(DEFAULT_URL);
        return principal_cross_section;
    }

    @Before
    public void initTest() {
        principal_cross_section = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrincipal_cross_section() throws Exception {
        int databaseSizeBeforeCreate = principal_cross_sectionRepository.findAll().size();

        // Create the Principal_cross_section

        restPrincipal_cross_sectionMockMvc.perform(post("/api/principal-cross-sections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(principal_cross_section)))
                .andExpect(status().isCreated());

        // Validate the Principal_cross_section in the database
        List<Principal_cross_section> principal_cross_sections = principal_cross_sectionRepository.findAll();
        assertThat(principal_cross_sections).hasSize(databaseSizeBeforeCreate + 1);
        Principal_cross_section testPrincipal_cross_section = principal_cross_sections.get(principal_cross_sections.size() - 1);
        assertThat(testPrincipal_cross_section.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrincipal_cross_section.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPrincipal_cross_section.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void getAllPrincipal_cross_sections() throws Exception {
        // Initialize the database
        principal_cross_sectionRepository.saveAndFlush(principal_cross_section);

        // Get all the principal_cross_sections
        restPrincipal_cross_sectionMockMvc.perform(get("/api/principal-cross-sections?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(principal_cross_section.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void getPrincipal_cross_section() throws Exception {
        // Initialize the database
        principal_cross_sectionRepository.saveAndFlush(principal_cross_section);

        // Get the principal_cross_section
        restPrincipal_cross_sectionMockMvc.perform(get("/api/principal-cross-sections/{id}", principal_cross_section.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(principal_cross_section.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrincipal_cross_section() throws Exception {
        // Get the principal_cross_section
        restPrincipal_cross_sectionMockMvc.perform(get("/api/principal-cross-sections/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrincipal_cross_section() throws Exception {
        // Initialize the database
        principal_cross_sectionRepository.saveAndFlush(principal_cross_section);
        int databaseSizeBeforeUpdate = principal_cross_sectionRepository.findAll().size();

        // Update the principal_cross_section
        Principal_cross_section updatedPrincipal_cross_section = principal_cross_sectionRepository.findOne(principal_cross_section.getId());
        updatedPrincipal_cross_section
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .url(UPDATED_URL);

        restPrincipal_cross_sectionMockMvc.perform(put("/api/principal-cross-sections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPrincipal_cross_section)))
                .andExpect(status().isOk());

        // Validate the Principal_cross_section in the database
        List<Principal_cross_section> principal_cross_sections = principal_cross_sectionRepository.findAll();
        assertThat(principal_cross_sections).hasSize(databaseSizeBeforeUpdate);
        Principal_cross_section testPrincipal_cross_section = principal_cross_sections.get(principal_cross_sections.size() - 1);
        assertThat(testPrincipal_cross_section.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrincipal_cross_section.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPrincipal_cross_section.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void deletePrincipal_cross_section() throws Exception {
        // Initialize the database
        principal_cross_sectionRepository.saveAndFlush(principal_cross_section);
        int databaseSizeBeforeDelete = principal_cross_sectionRepository.findAll().size();

        // Get the principal_cross_section
        restPrincipal_cross_sectionMockMvc.perform(delete("/api/principal-cross-sections/{id}", principal_cross_section.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Principal_cross_section> principal_cross_sections = principal_cross_sectionRepository.findAll();
        assertThat(principal_cross_sections).hasSize(databaseSizeBeforeDelete - 1);
    }
}
