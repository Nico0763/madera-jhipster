package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Component_product;
import com.madera.app.repository.Component_productRepository;

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
 * Test class for the Component_productResource REST controller.
 *
 * @see Component_productResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class Component_productResourceIntTest {

    private static final Integer DEFAULT_ANGLE = 9;
    private static final Integer UPDATED_ANGLE = 8;

    private static final Float DEFAULT_LENGTH = 1F;
    private static final Float UPDATED_LENGTH = 2F;

    @Inject
    private Component_productRepository component_productRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restComponent_productMockMvc;

    private Component_product component_product;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Component_productResource component_productResource = new Component_productResource();
        ReflectionTestUtils.setField(component_productResource, "component_productRepository", component_productRepository);
        this.restComponent_productMockMvc = MockMvcBuilders.standaloneSetup(component_productResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Component_product createEntity(EntityManager em) {
        Component_product component_product = new Component_product()
                .angle(DEFAULT_ANGLE)
                .length(DEFAULT_LENGTH);
        return component_product;
    }

    @Before
    public void initTest() {
        component_product = createEntity(em);
    }

    @Test
    @Transactional
    public void createComponent_product() throws Exception {
        int databaseSizeBeforeCreate = component_productRepository.findAll().size();

        // Create the Component_product

        restComponent_productMockMvc.perform(post("/api/component-products")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(component_product)))
                .andExpect(status().isCreated());

        // Validate the Component_product in the database
        List<Component_product> component_products = component_productRepository.findAll();
        assertThat(component_products).hasSize(databaseSizeBeforeCreate + 1);
        Component_product testComponent_product = component_products.get(component_products.size() - 1);
        assertThat(testComponent_product.getAngle()).isEqualTo(DEFAULT_ANGLE);
        assertThat(testComponent_product.getLength()).isEqualTo(DEFAULT_LENGTH);
    }

    @Test
    @Transactional
    public void getAllComponent_products() throws Exception {
        // Initialize the database
        component_productRepository.saveAndFlush(component_product);

        // Get all the component_products
        restComponent_productMockMvc.perform(get("/api/component-products?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(component_product.getId().intValue())))
                .andExpect(jsonPath("$.[*].angle").value(hasItem(DEFAULT_ANGLE)))
                .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())));
    }

    @Test
    @Transactional
    public void getComponent_product() throws Exception {
        // Initialize the database
        component_productRepository.saveAndFlush(component_product);

        // Get the component_product
        restComponent_productMockMvc.perform(get("/api/component-products/{id}", component_product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(component_product.getId().intValue()))
            .andExpect(jsonPath("$.angle").value(DEFAULT_ANGLE))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingComponent_product() throws Exception {
        // Get the component_product
        restComponent_productMockMvc.perform(get("/api/component-products/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComponent_product() throws Exception {
        // Initialize the database
        component_productRepository.saveAndFlush(component_product);
        int databaseSizeBeforeUpdate = component_productRepository.findAll().size();

        // Update the component_product
        Component_product updatedComponent_product = component_productRepository.findOne(component_product.getId());
        updatedComponent_product
                .angle(UPDATED_ANGLE)
                .length(UPDATED_LENGTH);

        restComponent_productMockMvc.perform(put("/api/component-products")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedComponent_product)))
                .andExpect(status().isOk());

        // Validate the Component_product in the database
        List<Component_product> component_products = component_productRepository.findAll();
        assertThat(component_products).hasSize(databaseSizeBeforeUpdate);
        Component_product testComponent_product = component_products.get(component_products.size() - 1);
        assertThat(testComponent_product.getAngle()).isEqualTo(UPDATED_ANGLE);
        assertThat(testComponent_product.getLength()).isEqualTo(UPDATED_LENGTH);
    }

    @Test
    @Transactional
    public void deleteComponent_product() throws Exception {
        // Initialize the database
        component_productRepository.saveAndFlush(component_product);
        int databaseSizeBeforeDelete = component_productRepository.findAll().size();

        // Get the component_product
        restComponent_productMockMvc.perform(delete("/api/component-products/{id}", component_product.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Component_product> component_products = component_productRepository.findAll();
        assertThat(component_products).hasSize(databaseSizeBeforeDelete - 1);
    }
}
