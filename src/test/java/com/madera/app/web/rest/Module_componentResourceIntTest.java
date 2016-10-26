package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Module_component;
import com.madera.app.repository.Module_componentRepository;

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
 * Test class for the Module_componentResource REST controller.
 *
 * @see Module_componentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class Module_componentResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 999;
    private static final Integer UPDATED_QUANTITY = 998;

    @Inject
    private Module_componentRepository module_componentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restModule_componentMockMvc;

    private Module_component module_component;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Module_componentResource module_componentResource = new Module_componentResource();
        ReflectionTestUtils.setField(module_componentResource, "module_componentRepository", module_componentRepository);
        this.restModule_componentMockMvc = MockMvcBuilders.standaloneSetup(module_componentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Module_component createEntity(EntityManager em) {
        Module_component module_component = new Module_component()
                .quantity(DEFAULT_QUANTITY);
        return module_component;
    }

    @Before
    public void initTest() {
        module_component = createEntity(em);
    }

    @Test
    @Transactional
    public void createModule_component() throws Exception {
        int databaseSizeBeforeCreate = module_componentRepository.findAll().size();

        // Create the Module_component

        restModule_componentMockMvc.perform(post("/api/module-components")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(module_component)))
                .andExpect(status().isCreated());

        // Validate the Module_component in the database
        List<Module_component> module_components = module_componentRepository.findAll();
        assertThat(module_components).hasSize(databaseSizeBeforeCreate + 1);
        Module_component testModule_component = module_components.get(module_components.size() - 1);
        assertThat(testModule_component.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllModule_components() throws Exception {
        // Initialize the database
        module_componentRepository.saveAndFlush(module_component);

        // Get all the module_components
        restModule_componentMockMvc.perform(get("/api/module-components?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(module_component.getId().intValue())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void getModule_component() throws Exception {
        // Initialize the database
        module_componentRepository.saveAndFlush(module_component);

        // Get the module_component
        restModule_componentMockMvc.perform(get("/api/module-components/{id}", module_component.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(module_component.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingModule_component() throws Exception {
        // Get the module_component
        restModule_componentMockMvc.perform(get("/api/module-components/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModule_component() throws Exception {
        // Initialize the database
        module_componentRepository.saveAndFlush(module_component);
        int databaseSizeBeforeUpdate = module_componentRepository.findAll().size();

        // Update the module_component
        Module_component updatedModule_component = module_componentRepository.findOne(module_component.getId());
        updatedModule_component
                .quantity(UPDATED_QUANTITY);

        restModule_componentMockMvc.perform(put("/api/module-components")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedModule_component)))
                .andExpect(status().isOk());

        // Validate the Module_component in the database
        List<Module_component> module_components = module_componentRepository.findAll();
        assertThat(module_components).hasSize(databaseSizeBeforeUpdate);
        Module_component testModule_component = module_components.get(module_components.size() - 1);
        assertThat(testModule_component.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void deleteModule_component() throws Exception {
        // Initialize the database
        module_componentRepository.saveAndFlush(module_component);
        int databaseSizeBeforeDelete = module_componentRepository.findAll().size();

        // Get the module_component
        restModule_componentMockMvc.perform(delete("/api/module-components/{id}", module_component.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Module_component> module_components = module_componentRepository.findAll();
        assertThat(module_components).hasSize(databaseSizeBeforeDelete - 1);
    }
}
