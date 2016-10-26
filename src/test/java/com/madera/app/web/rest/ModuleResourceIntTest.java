package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Module;
import com.madera.app.repository.ModuleRepository;

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
 * Test class for the ModuleResource REST controller.
 *
 * @see ModuleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class ModuleResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final byte[] DEFAULT_CCTP = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CCTP = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_CCTP_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CCTP_CONTENT_TYPE = "image/png";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    @Inject
    private ModuleRepository moduleRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restModuleMockMvc;

    private Module module;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ModuleResource moduleResource = new ModuleResource();
        ReflectionTestUtils.setField(moduleResource, "moduleRepository", moduleRepository);
        this.restModuleMockMvc = MockMvcBuilders.standaloneSetup(moduleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Module createEntity(EntityManager em) {
        Module module = new Module()
                .name(DEFAULT_NAME)
                .cctp(DEFAULT_CCTP)
                .cctpContentType(DEFAULT_CCTP_CONTENT_TYPE)
                .price(DEFAULT_PRICE);
        return module;
    }

    @Before
    public void initTest() {
        module = createEntity(em);
    }

    @Test
    @Transactional
    public void createModule() throws Exception {
        int databaseSizeBeforeCreate = moduleRepository.findAll().size();

        // Create the Module

        restModuleMockMvc.perform(post("/api/modules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(module)))
                .andExpect(status().isCreated());

        // Validate the Module in the database
        List<Module> modules = moduleRepository.findAll();
        assertThat(modules).hasSize(databaseSizeBeforeCreate + 1);
        Module testModule = modules.get(modules.size() - 1);
        assertThat(testModule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModule.getCctp()).isEqualTo(DEFAULT_CCTP);
        assertThat(testModule.getCctpContentType()).isEqualTo(DEFAULT_CCTP_CONTENT_TYPE);
        assertThat(testModule.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void getAllModules() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the modules
        restModuleMockMvc.perform(get("/api/modules?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(module.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].cctpContentType").value(hasItem(DEFAULT_CCTP_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].cctp").value(hasItem(Base64Utils.encodeToString(DEFAULT_CCTP))))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void getModule() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get the module
        restModuleMockMvc.perform(get("/api/modules/{id}", module.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(module.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.cctpContentType").value(DEFAULT_CCTP_CONTENT_TYPE))
            .andExpect(jsonPath("$.cctp").value(Base64Utils.encodeToString(DEFAULT_CCTP)))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingModule() throws Exception {
        // Get the module
        restModuleMockMvc.perform(get("/api/modules/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModule() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);
        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();

        // Update the module
        Module updatedModule = moduleRepository.findOne(module.getId());
        updatedModule
                .name(UPDATED_NAME)
                .cctp(UPDATED_CCTP)
                .cctpContentType(UPDATED_CCTP_CONTENT_TYPE)
                .price(UPDATED_PRICE);

        restModuleMockMvc.perform(put("/api/modules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedModule)))
                .andExpect(status().isOk());

        // Validate the Module in the database
        List<Module> modules = moduleRepository.findAll();
        assertThat(modules).hasSize(databaseSizeBeforeUpdate);
        Module testModule = modules.get(modules.size() - 1);
        assertThat(testModule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModule.getCctp()).isEqualTo(UPDATED_CCTP);
        assertThat(testModule.getCctpContentType()).isEqualTo(UPDATED_CCTP_CONTENT_TYPE);
        assertThat(testModule.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void deleteModule() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);
        int databaseSizeBeforeDelete = moduleRepository.findAll().size();

        // Get the module
        restModuleMockMvc.perform(delete("/api/modules/{id}", module.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Module> modules = moduleRepository.findAll();
        assertThat(modules).hasSize(databaseSizeBeforeDelete - 1);
    }
}
