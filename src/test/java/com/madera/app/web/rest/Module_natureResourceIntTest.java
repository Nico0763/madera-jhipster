package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Module_nature;
import com.madera.app.repository.Module_natureRepository;

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
 * Test class for the Module_natureResource REST controller.
 *
 * @see Module_natureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class Module_natureResourceIntTest {

    private static final String DEFAULT_NATURE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NATURE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_CARACTERISTICS = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CARACTERISTICS = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private Module_natureRepository module_natureRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restModule_natureMockMvc;

    private Module_nature module_nature;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Module_natureResource module_natureResource = new Module_natureResource();
        ReflectionTestUtils.setField(module_natureResource, "module_natureRepository", module_natureRepository);
        this.restModule_natureMockMvc = MockMvcBuilders.standaloneSetup(module_natureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Module_nature createEntity(EntityManager em) {
        Module_nature module_nature = new Module_nature()
                .nature(DEFAULT_NATURE)
                .caracteristics(DEFAULT_CARACTERISTICS);
        return module_nature;
    }

    @Before
    public void initTest() {
        module_nature = createEntity(em);
    }

    @Test
    @Transactional
    public void createModule_nature() throws Exception {
        int databaseSizeBeforeCreate = module_natureRepository.findAll().size();

        // Create the Module_nature

        restModule_natureMockMvc.perform(post("/api/module-natures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(module_nature)))
                .andExpect(status().isCreated());

        // Validate the Module_nature in the database
        List<Module_nature> module_natures = module_natureRepository.findAll();
        assertThat(module_natures).hasSize(databaseSizeBeforeCreate + 1);
        Module_nature testModule_nature = module_natures.get(module_natures.size() - 1);
        assertThat(testModule_nature.getNature()).isEqualTo(DEFAULT_NATURE);
        assertThat(testModule_nature.getCaracteristics()).isEqualTo(DEFAULT_CARACTERISTICS);
    }

    @Test
    @Transactional
    public void getAllModule_natures() throws Exception {
        // Initialize the database
        module_natureRepository.saveAndFlush(module_nature);

        // Get all the module_natures
        restModule_natureMockMvc.perform(get("/api/module-natures?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(module_nature.getId().intValue())))
                .andExpect(jsonPath("$.[*].nature").value(hasItem(DEFAULT_NATURE.toString())))
                .andExpect(jsonPath("$.[*].caracteristics").value(hasItem(DEFAULT_CARACTERISTICS.toString())));
    }

    @Test
    @Transactional
    public void getModule_nature() throws Exception {
        // Initialize the database
        module_natureRepository.saveAndFlush(module_nature);

        // Get the module_nature
        restModule_natureMockMvc.perform(get("/api/module-natures/{id}", module_nature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(module_nature.getId().intValue()))
            .andExpect(jsonPath("$.nature").value(DEFAULT_NATURE.toString()))
            .andExpect(jsonPath("$.caracteristics").value(DEFAULT_CARACTERISTICS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingModule_nature() throws Exception {
        // Get the module_nature
        restModule_natureMockMvc.perform(get("/api/module-natures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModule_nature() throws Exception {
        // Initialize the database
        module_natureRepository.saveAndFlush(module_nature);
        int databaseSizeBeforeUpdate = module_natureRepository.findAll().size();

        // Update the module_nature
        Module_nature updatedModule_nature = module_natureRepository.findOne(module_nature.getId());
        updatedModule_nature
                .nature(UPDATED_NATURE)
                .caracteristics(UPDATED_CARACTERISTICS);

        restModule_natureMockMvc.perform(put("/api/module-natures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedModule_nature)))
                .andExpect(status().isOk());

        // Validate the Module_nature in the database
        List<Module_nature> module_natures = module_natureRepository.findAll();
        assertThat(module_natures).hasSize(databaseSizeBeforeUpdate);
        Module_nature testModule_nature = module_natures.get(module_natures.size() - 1);
        assertThat(testModule_nature.getNature()).isEqualTo(UPDATED_NATURE);
        assertThat(testModule_nature.getCaracteristics()).isEqualTo(UPDATED_CARACTERISTICS);
    }

    @Test
    @Transactional
    public void deleteModule_nature() throws Exception {
        // Initialize the database
        module_natureRepository.saveAndFlush(module_nature);
        int databaseSizeBeforeDelete = module_natureRepository.findAll().size();

        // Get the module_nature
        restModule_natureMockMvc.perform(delete("/api/module-natures/{id}", module_nature.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Module_nature> module_natures = module_natureRepository.findAll();
        assertThat(module_natures).hasSize(databaseSizeBeforeDelete - 1);
    }
}
