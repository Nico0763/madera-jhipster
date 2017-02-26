package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Cover_type;
import com.madera.app.repository.Cover_typeRepository;

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
 * Test class for the Cover_typeResource REST controller.
 *
 * @see Cover_typeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class Cover_typeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    @Inject
    private Cover_typeRepository cover_typeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCover_typeMockMvc;

    private Cover_type cover_type;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Cover_typeResource cover_typeResource = new Cover_typeResource();
        ReflectionTestUtils.setField(cover_typeResource, "cover_typeRepository", cover_typeRepository);
        this.restCover_typeMockMvc = MockMvcBuilders.standaloneSetup(cover_typeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cover_type createEntity(EntityManager em) {
        Cover_type cover_type = new Cover_type()
                .name(DEFAULT_NAME)
                .url(DEFAULT_URL);
        return cover_type;
    }

    @Before
    public void initTest() {
        cover_type = createEntity(em);
    }

    @Test
    @Transactional
    public void createCover_type() throws Exception {
        int databaseSizeBeforeCreate = cover_typeRepository.findAll().size();

        // Create the Cover_type

        restCover_typeMockMvc.perform(post("/api/cover-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cover_type)))
                .andExpect(status().isCreated());

        // Validate the Cover_type in the database
        List<Cover_type> cover_types = cover_typeRepository.findAll();
        assertThat(cover_types).hasSize(databaseSizeBeforeCreate + 1);
        Cover_type testCover_type = cover_types.get(cover_types.size() - 1);
        assertThat(testCover_type.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCover_type.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void getAllCover_types() throws Exception {
        // Initialize the database
        cover_typeRepository.saveAndFlush(cover_type);

        // Get all the cover_types
        restCover_typeMockMvc.perform(get("/api/cover-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cover_type.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void getCover_type() throws Exception {
        // Initialize the database
        cover_typeRepository.saveAndFlush(cover_type);

        // Get the cover_type
        restCover_typeMockMvc.perform(get("/api/cover-types/{id}", cover_type.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cover_type.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCover_type() throws Exception {
        // Get the cover_type
        restCover_typeMockMvc.perform(get("/api/cover-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCover_type() throws Exception {
        // Initialize the database
        cover_typeRepository.saveAndFlush(cover_type);
        int databaseSizeBeforeUpdate = cover_typeRepository.findAll().size();

        // Update the cover_type
        Cover_type updatedCover_type = cover_typeRepository.findOne(cover_type.getId());
        updatedCover_type
                .name(UPDATED_NAME)
                .url(UPDATED_URL);

        restCover_typeMockMvc.perform(put("/api/cover-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCover_type)))
                .andExpect(status().isOk());

        // Validate the Cover_type in the database
        List<Cover_type> cover_types = cover_typeRepository.findAll();
        assertThat(cover_types).hasSize(databaseSizeBeforeUpdate);
        Cover_type testCover_type = cover_types.get(cover_types.size() - 1);
        assertThat(testCover_type.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCover_type.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void deleteCover_type() throws Exception {
        // Initialize the database
        cover_typeRepository.saveAndFlush(cover_type);
        int databaseSizeBeforeDelete = cover_typeRepository.findAll().size();

        // Get the cover_type
        restCover_typeMockMvc.perform(delete("/api/cover-types/{id}", cover_type.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Cover_type> cover_types = cover_typeRepository.findAll();
        assertThat(cover_types).hasSize(databaseSizeBeforeDelete - 1);
    }
}
