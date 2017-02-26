package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Finition_ext;
import com.madera.app.repository.Finition_extRepository;

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
 * Test class for the Finition_extResource REST controller.
 *
 * @see Finition_extResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class Finition_extResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    @Inject
    private Finition_extRepository finition_extRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFinition_extMockMvc;

    private Finition_ext finition_ext;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Finition_extResource finition_extResource = new Finition_extResource();
        ReflectionTestUtils.setField(finition_extResource, "finition_extRepository", finition_extRepository);
        this.restFinition_extMockMvc = MockMvcBuilders.standaloneSetup(finition_extResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Finition_ext createEntity(EntityManager em) {
        Finition_ext finition_ext = new Finition_ext()
                .name(DEFAULT_NAME)
                .url(DEFAULT_URL);
        return finition_ext;
    }

    @Before
    public void initTest() {
        finition_ext = createEntity(em);
    }

    @Test
    @Transactional
    public void createFinition_ext() throws Exception {
        int databaseSizeBeforeCreate = finition_extRepository.findAll().size();

        // Create the Finition_ext

        restFinition_extMockMvc.perform(post("/api/finition-exts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(finition_ext)))
                .andExpect(status().isCreated());

        // Validate the Finition_ext in the database
        List<Finition_ext> finition_exts = finition_extRepository.findAll();
        assertThat(finition_exts).hasSize(databaseSizeBeforeCreate + 1);
        Finition_ext testFinition_ext = finition_exts.get(finition_exts.size() - 1);
        assertThat(testFinition_ext.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFinition_ext.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void getAllFinition_exts() throws Exception {
        // Initialize the database
        finition_extRepository.saveAndFlush(finition_ext);

        // Get all the finition_exts
        restFinition_extMockMvc.perform(get("/api/finition-exts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(finition_ext.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void getFinition_ext() throws Exception {
        // Initialize the database
        finition_extRepository.saveAndFlush(finition_ext);

        // Get the finition_ext
        restFinition_extMockMvc.perform(get("/api/finition-exts/{id}", finition_ext.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(finition_ext.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFinition_ext() throws Exception {
        // Get the finition_ext
        restFinition_extMockMvc.perform(get("/api/finition-exts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFinition_ext() throws Exception {
        // Initialize the database
        finition_extRepository.saveAndFlush(finition_ext);
        int databaseSizeBeforeUpdate = finition_extRepository.findAll().size();

        // Update the finition_ext
        Finition_ext updatedFinition_ext = finition_extRepository.findOne(finition_ext.getId());
        updatedFinition_ext
                .name(UPDATED_NAME)
                .url(UPDATED_URL);

        restFinition_extMockMvc.perform(put("/api/finition-exts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFinition_ext)))
                .andExpect(status().isOk());

        // Validate the Finition_ext in the database
        List<Finition_ext> finition_exts = finition_extRepository.findAll();
        assertThat(finition_exts).hasSize(databaseSizeBeforeUpdate);
        Finition_ext testFinition_ext = finition_exts.get(finition_exts.size() - 1);
        assertThat(testFinition_ext.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFinition_ext.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void deleteFinition_ext() throws Exception {
        // Initialize the database
        finition_extRepository.saveAndFlush(finition_ext);
        int databaseSizeBeforeDelete = finition_extRepository.findAll().size();

        // Get the finition_ext
        restFinition_extMockMvc.perform(delete("/api/finition-exts/{id}", finition_ext.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Finition_ext> finition_exts = finition_extRepository.findAll();
        assertThat(finition_exts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
