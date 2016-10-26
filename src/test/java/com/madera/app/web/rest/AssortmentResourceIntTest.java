package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Assortment;
import com.madera.app.repository.AssortmentRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AssortmentResource REST controller.
 *
 * @see AssortmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class AssortmentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final byte[] DEFAULT_SKELETON_CONCEPTION_MODE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SKELETON_CONCEPTION_MODE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_SKELETON_CONCEPTION_MODE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SKELETON_CONCEPTION_MODE_CONTENT_TYPE = "image/png";

    @Inject
    private AssortmentRepository assortmentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAssortmentMockMvc;

    private Assortment assortment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AssortmentResource assortmentResource = new AssortmentResource();
        ReflectionTestUtils.setField(assortmentResource, "assortmentRepository", assortmentRepository);
        this.restAssortmentMockMvc = MockMvcBuilders.standaloneSetup(assortmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assortment createEntity(EntityManager em) {
        Assortment assortment = new Assortment()
                .name(DEFAULT_NAME)
                .skeleton_conception_mode(DEFAULT_SKELETON_CONCEPTION_MODE)
                .skeleton_conception_modeContentType(DEFAULT_SKELETON_CONCEPTION_MODE_CONTENT_TYPE);
        return assortment;
    }

    @Before
    public void initTest() {
        assortment = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssortment() throws Exception {
        int databaseSizeBeforeCreate = assortmentRepository.findAll().size();

        // Create the Assortment

        restAssortmentMockMvc.perform(post("/api/assortments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assortment)))
                .andExpect(status().isCreated());

        // Validate the Assortment in the database
        List<Assortment> assortments = assortmentRepository.findAll();
        assertThat(assortments).hasSize(databaseSizeBeforeCreate + 1);
        Assortment testAssortment = assortments.get(assortments.size() - 1);
        assertThat(testAssortment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAssortment.getSkeleton_conception_mode()).isEqualTo(DEFAULT_SKELETON_CONCEPTION_MODE);
        assertThat(testAssortment.getSkeleton_conception_modeContentType()).isEqualTo(DEFAULT_SKELETON_CONCEPTION_MODE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllAssortments() throws Exception {
        // Initialize the database
        assortmentRepository.saveAndFlush(assortment);

        // Get all the assortments
        restAssortmentMockMvc.perform(get("/api/assortments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(assortment.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].skeleton_conception_modeContentType").value(hasItem(DEFAULT_SKELETON_CONCEPTION_MODE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].skeleton_conception_mode").value(hasItem(Base64Utils.encodeToString(DEFAULT_SKELETON_CONCEPTION_MODE))));
    }

    @Test
    @Transactional
    public void getAssortment() throws Exception {
        // Initialize the database
        assortmentRepository.saveAndFlush(assortment);

        // Get the assortment
        restAssortmentMockMvc.perform(get("/api/assortments/{id}", assortment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(assortment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.skeleton_conception_modeContentType").value(DEFAULT_SKELETON_CONCEPTION_MODE_CONTENT_TYPE))
            .andExpect(jsonPath("$.skeleton_conception_mode").value(Base64Utils.encodeToString(DEFAULT_SKELETON_CONCEPTION_MODE)));
    }

    @Test
    @Transactional
    public void getNonExistingAssortment() throws Exception {
        // Get the assortment
        restAssortmentMockMvc.perform(get("/api/assortments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssortment() throws Exception {
        // Initialize the database
        assortmentRepository.saveAndFlush(assortment);
        int databaseSizeBeforeUpdate = assortmentRepository.findAll().size();

        // Update the assortment
        Assortment updatedAssortment = assortmentRepository.findOne(assortment.getId());
        updatedAssortment
                .name(UPDATED_NAME)
                .skeleton_conception_mode(UPDATED_SKELETON_CONCEPTION_MODE)
                .skeleton_conception_modeContentType(UPDATED_SKELETON_CONCEPTION_MODE_CONTENT_TYPE);

        restAssortmentMockMvc.perform(put("/api/assortments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAssortment)))
                .andExpect(status().isOk());

        // Validate the Assortment in the database
        List<Assortment> assortments = assortmentRepository.findAll();
        assertThat(assortments).hasSize(databaseSizeBeforeUpdate);
        Assortment testAssortment = assortments.get(assortments.size() - 1);
        assertThat(testAssortment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAssortment.getSkeleton_conception_mode()).isEqualTo(UPDATED_SKELETON_CONCEPTION_MODE);
        assertThat(testAssortment.getSkeleton_conception_modeContentType()).isEqualTo(UPDATED_SKELETON_CONCEPTION_MODE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteAssortment() throws Exception {
        // Initialize the database
        assortmentRepository.saveAndFlush(assortment);
        int databaseSizeBeforeDelete = assortmentRepository.findAll().size();

        // Get the assortment
        restAssortmentMockMvc.perform(delete("/api/assortments/{id}", assortment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Assortment> assortments = assortmentRepository.findAll();
        assertThat(assortments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
