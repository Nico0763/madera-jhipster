package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Insulating_type;
import com.madera.app.repository.Insulating_typeRepository;

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
 * Test class for the Insulating_typeResource REST controller.
 *
 * @see Insulating_typeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class Insulating_typeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private Insulating_typeRepository insulating_typeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restInsulating_typeMockMvc;

    private Insulating_type insulating_type;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Insulating_typeResource insulating_typeResource = new Insulating_typeResource();
        ReflectionTestUtils.setField(insulating_typeResource, "insulating_typeRepository", insulating_typeRepository);
        this.restInsulating_typeMockMvc = MockMvcBuilders.standaloneSetup(insulating_typeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Insulating_type createEntity(EntityManager em) {
        Insulating_type insulating_type = new Insulating_type()
                .name(DEFAULT_NAME);
        return insulating_type;
    }

    @Before
    public void initTest() {
        insulating_type = createEntity(em);
    }

    @Test
    @Transactional
    public void createInsulating_type() throws Exception {
        int databaseSizeBeforeCreate = insulating_typeRepository.findAll().size();

        // Create the Insulating_type

        restInsulating_typeMockMvc.perform(post("/api/insulating-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(insulating_type)))
                .andExpect(status().isCreated());

        // Validate the Insulating_type in the database
        List<Insulating_type> insulating_types = insulating_typeRepository.findAll();
        assertThat(insulating_types).hasSize(databaseSizeBeforeCreate + 1);
        Insulating_type testInsulating_type = insulating_types.get(insulating_types.size() - 1);
        assertThat(testInsulating_type.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllInsulating_types() throws Exception {
        // Initialize the database
        insulating_typeRepository.saveAndFlush(insulating_type);

        // Get all the insulating_types
        restInsulating_typeMockMvc.perform(get("/api/insulating-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(insulating_type.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getInsulating_type() throws Exception {
        // Initialize the database
        insulating_typeRepository.saveAndFlush(insulating_type);

        // Get the insulating_type
        restInsulating_typeMockMvc.perform(get("/api/insulating-types/{id}", insulating_type.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(insulating_type.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInsulating_type() throws Exception {
        // Get the insulating_type
        restInsulating_typeMockMvc.perform(get("/api/insulating-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInsulating_type() throws Exception {
        // Initialize the database
        insulating_typeRepository.saveAndFlush(insulating_type);
        int databaseSizeBeforeUpdate = insulating_typeRepository.findAll().size();

        // Update the insulating_type
        Insulating_type updatedInsulating_type = insulating_typeRepository.findOne(insulating_type.getId());
        updatedInsulating_type
                .name(UPDATED_NAME);

        restInsulating_typeMockMvc.perform(put("/api/insulating-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInsulating_type)))
                .andExpect(status().isOk());

        // Validate the Insulating_type in the database
        List<Insulating_type> insulating_types = insulating_typeRepository.findAll();
        assertThat(insulating_types).hasSize(databaseSizeBeforeUpdate);
        Insulating_type testInsulating_type = insulating_types.get(insulating_types.size() - 1);
        assertThat(testInsulating_type.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteInsulating_type() throws Exception {
        // Initialize the database
        insulating_typeRepository.saveAndFlush(insulating_type);
        int databaseSizeBeforeDelete = insulating_typeRepository.findAll().size();

        // Get the insulating_type
        restInsulating_typeMockMvc.perform(delete("/api/insulating-types/{id}", insulating_type.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Insulating_type> insulating_types = insulating_typeRepository.findAll();
        assertThat(insulating_types).hasSize(databaseSizeBeforeDelete - 1);
    }
}
