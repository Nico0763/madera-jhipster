package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Component;
import com.madera.app.repository.ComponentRepository;

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
 * Test class for the ComponentResource REST controller.
 *
 * @see ComponentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class ComponentResourceIntTest {

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAA";
    private static final String UPDATED_REFERENCE = "BBBBB";

    @Inject
    private ComponentRepository componentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restComponentMockMvc;

    private Component component;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ComponentResource componentResource = new ComponentResource();
        ReflectionTestUtils.setField(componentResource, "componentRepository", componentRepository);
        this.restComponentMockMvc = MockMvcBuilders.standaloneSetup(componentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Component createEntity(EntityManager em) {
        Component component = new Component()
                .url(DEFAULT_URL)
                .reference(DEFAULT_REFERENCE);
        return component;
    }

    @Before
    public void initTest() {
        component = createEntity(em);
    }

    @Test
    @Transactional
    public void createComponent() throws Exception {
        int databaseSizeBeforeCreate = componentRepository.findAll().size();

        // Create the Component

        restComponentMockMvc.perform(post("/api/components")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(component)))
                .andExpect(status().isCreated());

        // Validate the Component in the database
        List<Component> components = componentRepository.findAll();
        assertThat(components).hasSize(databaseSizeBeforeCreate + 1);
        Component testComponent = components.get(components.size() - 1);
        assertThat(testComponent.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testComponent.getReference()).isEqualTo(DEFAULT_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllComponents() throws Exception {
        // Initialize the database
        componentRepository.saveAndFlush(component);

        // Get all the components
        restComponentMockMvc.perform(get("/api/components?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(component.getId().intValue())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())));
    }

    @Test
    @Transactional
    public void getComponent() throws Exception {
        // Initialize the database
        componentRepository.saveAndFlush(component);

        // Get the component
        restComponentMockMvc.perform(get("/api/components/{id}", component.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(component.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingComponent() throws Exception {
        // Get the component
        restComponentMockMvc.perform(get("/api/components/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComponent() throws Exception {
        // Initialize the database
        componentRepository.saveAndFlush(component);
        int databaseSizeBeforeUpdate = componentRepository.findAll().size();

        // Update the component
        Component updatedComponent = componentRepository.findOne(component.getId());
        updatedComponent
                .url(UPDATED_URL)
                .reference(UPDATED_REFERENCE);

        restComponentMockMvc.perform(put("/api/components")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedComponent)))
                .andExpect(status().isOk());

        // Validate the Component in the database
        List<Component> components = componentRepository.findAll();
        assertThat(components).hasSize(databaseSizeBeforeUpdate);
        Component testComponent = components.get(components.size() - 1);
        assertThat(testComponent.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testComponent.getReference()).isEqualTo(UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void deleteComponent() throws Exception {
        // Initialize the database
        componentRepository.saveAndFlush(component);
        int databaseSizeBeforeDelete = componentRepository.findAll().size();

        // Get the component
        restComponentMockMvc.perform(delete("/api/components/{id}", component.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Component> components = componentRepository.findAll();
        assertThat(components).hasSize(databaseSizeBeforeDelete - 1);
    }
}
