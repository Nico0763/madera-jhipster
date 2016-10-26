package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Component_nature;
import com.madera.app.repository.Component_natureRepository;

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
 * Test class for the Component_natureResource REST controller.
 *
 * @see Component_natureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class Component_natureResourceIntTest {

    private static final String DEFAULT_NATURE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NATURE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_CARACTERISTICS = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CARACTERISTICS = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private Component_natureRepository component_natureRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restComponent_natureMockMvc;

    private Component_nature component_nature;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Component_natureResource component_natureResource = new Component_natureResource();
        ReflectionTestUtils.setField(component_natureResource, "component_natureRepository", component_natureRepository);
        this.restComponent_natureMockMvc = MockMvcBuilders.standaloneSetup(component_natureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Component_nature createEntity(EntityManager em) {
        Component_nature component_nature = new Component_nature()
                .nature(DEFAULT_NATURE)
                .caracteristics(DEFAULT_CARACTERISTICS);
        return component_nature;
    }

    @Before
    public void initTest() {
        component_nature = createEntity(em);
    }

    @Test
    @Transactional
    public void createComponent_nature() throws Exception {
        int databaseSizeBeforeCreate = component_natureRepository.findAll().size();

        // Create the Component_nature

        restComponent_natureMockMvc.perform(post("/api/component-natures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(component_nature)))
                .andExpect(status().isCreated());

        // Validate the Component_nature in the database
        List<Component_nature> component_natures = component_natureRepository.findAll();
        assertThat(component_natures).hasSize(databaseSizeBeforeCreate + 1);
        Component_nature testComponent_nature = component_natures.get(component_natures.size() - 1);
        assertThat(testComponent_nature.getNature()).isEqualTo(DEFAULT_NATURE);
        assertThat(testComponent_nature.getCaracteristics()).isEqualTo(DEFAULT_CARACTERISTICS);
    }

    @Test
    @Transactional
    public void getAllComponent_natures() throws Exception {
        // Initialize the database
        component_natureRepository.saveAndFlush(component_nature);

        // Get all the component_natures
        restComponent_natureMockMvc.perform(get("/api/component-natures?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(component_nature.getId().intValue())))
                .andExpect(jsonPath("$.[*].nature").value(hasItem(DEFAULT_NATURE.toString())))
                .andExpect(jsonPath("$.[*].caracteristics").value(hasItem(DEFAULT_CARACTERISTICS.toString())));
    }

    @Test
    @Transactional
    public void getComponent_nature() throws Exception {
        // Initialize the database
        component_natureRepository.saveAndFlush(component_nature);

        // Get the component_nature
        restComponent_natureMockMvc.perform(get("/api/component-natures/{id}", component_nature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(component_nature.getId().intValue()))
            .andExpect(jsonPath("$.nature").value(DEFAULT_NATURE.toString()))
            .andExpect(jsonPath("$.caracteristics").value(DEFAULT_CARACTERISTICS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingComponent_nature() throws Exception {
        // Get the component_nature
        restComponent_natureMockMvc.perform(get("/api/component-natures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComponent_nature() throws Exception {
        // Initialize the database
        component_natureRepository.saveAndFlush(component_nature);
        int databaseSizeBeforeUpdate = component_natureRepository.findAll().size();

        // Update the component_nature
        Component_nature updatedComponent_nature = component_natureRepository.findOne(component_nature.getId());
        updatedComponent_nature
                .nature(UPDATED_NATURE)
                .caracteristics(UPDATED_CARACTERISTICS);

        restComponent_natureMockMvc.perform(put("/api/component-natures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedComponent_nature)))
                .andExpect(status().isOk());

        // Validate the Component_nature in the database
        List<Component_nature> component_natures = component_natureRepository.findAll();
        assertThat(component_natures).hasSize(databaseSizeBeforeUpdate);
        Component_nature testComponent_nature = component_natures.get(component_natures.size() - 1);
        assertThat(testComponent_nature.getNature()).isEqualTo(UPDATED_NATURE);
        assertThat(testComponent_nature.getCaracteristics()).isEqualTo(UPDATED_CARACTERISTICS);
    }

    @Test
    @Transactional
    public void deleteComponent_nature() throws Exception {
        // Initialize the database
        component_natureRepository.saveAndFlush(component_nature);
        int databaseSizeBeforeDelete = component_natureRepository.findAll().size();

        // Get the component_nature
        restComponent_natureMockMvc.perform(delete("/api/component-natures/{id}", component_nature.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Component_nature> component_natures = component_natureRepository.findAll();
        assertThat(component_natures).hasSize(databaseSizeBeforeDelete - 1);
    }
}
