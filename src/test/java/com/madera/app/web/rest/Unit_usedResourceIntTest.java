package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Unit_used;
import com.madera.app.repository.Unit_usedRepository;

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
 * Test class for the Unit_usedResource REST controller.
 *
 * @see Unit_usedResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class Unit_usedResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_REGULAR_EXPRESSION = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_REGULAR_EXPRESSION = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private Unit_usedRepository unit_usedRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUnit_usedMockMvc;

    private Unit_used unit_used;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Unit_usedResource unit_usedResource = new Unit_usedResource();
        ReflectionTestUtils.setField(unit_usedResource, "unit_usedRepository", unit_usedRepository);
        this.restUnit_usedMockMvc = MockMvcBuilders.standaloneSetup(unit_usedResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unit_used createEntity(EntityManager em) {
        Unit_used unit_used = new Unit_used()
                .name(DEFAULT_NAME)
                .regular_expression(DEFAULT_REGULAR_EXPRESSION);
        return unit_used;
    }

    @Before
    public void initTest() {
        unit_used = createEntity(em);
    }

    @Test
    @Transactional
    public void createUnit_used() throws Exception {
        int databaseSizeBeforeCreate = unit_usedRepository.findAll().size();

        // Create the Unit_used

        restUnit_usedMockMvc.perform(post("/api/unit-useds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(unit_used)))
                .andExpect(status().isCreated());

        // Validate the Unit_used in the database
        List<Unit_used> unit_useds = unit_usedRepository.findAll();
        assertThat(unit_useds).hasSize(databaseSizeBeforeCreate + 1);
        Unit_used testUnit_used = unit_useds.get(unit_useds.size() - 1);
        assertThat(testUnit_used.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnit_used.getRegular_expression()).isEqualTo(DEFAULT_REGULAR_EXPRESSION);
    }

    @Test
    @Transactional
    public void getAllUnit_useds() throws Exception {
        // Initialize the database
        unit_usedRepository.saveAndFlush(unit_used);

        // Get all the unit_useds
        restUnit_usedMockMvc.perform(get("/api/unit-useds?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(unit_used.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].regular_expression").value(hasItem(DEFAULT_REGULAR_EXPRESSION.toString())));
    }

    @Test
    @Transactional
    public void getUnit_used() throws Exception {
        // Initialize the database
        unit_usedRepository.saveAndFlush(unit_used);

        // Get the unit_used
        restUnit_usedMockMvc.perform(get("/api/unit-useds/{id}", unit_used.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(unit_used.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.regular_expression").value(DEFAULT_REGULAR_EXPRESSION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUnit_used() throws Exception {
        // Get the unit_used
        restUnit_usedMockMvc.perform(get("/api/unit-useds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnit_used() throws Exception {
        // Initialize the database
        unit_usedRepository.saveAndFlush(unit_used);
        int databaseSizeBeforeUpdate = unit_usedRepository.findAll().size();

        // Update the unit_used
        Unit_used updatedUnit_used = unit_usedRepository.findOne(unit_used.getId());
        updatedUnit_used
                .name(UPDATED_NAME)
                .regular_expression(UPDATED_REGULAR_EXPRESSION);

        restUnit_usedMockMvc.perform(put("/api/unit-useds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUnit_used)))
                .andExpect(status().isOk());

        // Validate the Unit_used in the database
        List<Unit_used> unit_useds = unit_usedRepository.findAll();
        assertThat(unit_useds).hasSize(databaseSizeBeforeUpdate);
        Unit_used testUnit_used = unit_useds.get(unit_useds.size() - 1);
        assertThat(testUnit_used.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnit_used.getRegular_expression()).isEqualTo(UPDATED_REGULAR_EXPRESSION);
    }

    @Test
    @Transactional
    public void deleteUnit_used() throws Exception {
        // Initialize the database
        unit_usedRepository.saveAndFlush(unit_used);
        int databaseSizeBeforeDelete = unit_usedRepository.findAll().size();

        // Get the unit_used
        restUnit_usedMockMvc.perform(delete("/api/unit-useds/{id}", unit_used.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Unit_used> unit_useds = unit_usedRepository.findAll();
        assertThat(unit_useds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
