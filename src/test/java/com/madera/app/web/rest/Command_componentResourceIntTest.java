package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Command_component;
import com.madera.app.repository.Command_componentRepository;

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
 * Test class for the Command_componentResource REST controller.
 *
 * @see Command_componentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class Command_componentResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 999;
    private static final Integer UPDATED_QUANTITY = 998;

    @Inject
    private Command_componentRepository command_componentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCommand_componentMockMvc;

    private Command_component command_component;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Command_componentResource command_componentResource = new Command_componentResource();
        ReflectionTestUtils.setField(command_componentResource, "command_componentRepository", command_componentRepository);
        this.restCommand_componentMockMvc = MockMvcBuilders.standaloneSetup(command_componentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Command_component createEntity(EntityManager em) {
        Command_component command_component = new Command_component()
                .quantity(DEFAULT_QUANTITY);
        return command_component;
    }

    @Before
    public void initTest() {
        command_component = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommand_component() throws Exception {
        int databaseSizeBeforeCreate = command_componentRepository.findAll().size();

        // Create the Command_component

        restCommand_componentMockMvc.perform(post("/api/command-components")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(command_component)))
                .andExpect(status().isCreated());

        // Validate the Command_component in the database
        List<Command_component> command_components = command_componentRepository.findAll();
        assertThat(command_components).hasSize(databaseSizeBeforeCreate + 1);
        Command_component testCommand_component = command_components.get(command_components.size() - 1);
        assertThat(testCommand_component.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllCommand_components() throws Exception {
        // Initialize the database
        command_componentRepository.saveAndFlush(command_component);

        // Get all the command_components
        restCommand_componentMockMvc.perform(get("/api/command-components?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(command_component.getId().intValue())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void getCommand_component() throws Exception {
        // Initialize the database
        command_componentRepository.saveAndFlush(command_component);

        // Get the command_component
        restCommand_componentMockMvc.perform(get("/api/command-components/{id}", command_component.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(command_component.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingCommand_component() throws Exception {
        // Get the command_component
        restCommand_componentMockMvc.perform(get("/api/command-components/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommand_component() throws Exception {
        // Initialize the database
        command_componentRepository.saveAndFlush(command_component);
        int databaseSizeBeforeUpdate = command_componentRepository.findAll().size();

        // Update the command_component
        Command_component updatedCommand_component = command_componentRepository.findOne(command_component.getId());
        updatedCommand_component
                .quantity(UPDATED_QUANTITY);

        restCommand_componentMockMvc.perform(put("/api/command-components")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCommand_component)))
                .andExpect(status().isOk());

        // Validate the Command_component in the database
        List<Command_component> command_components = command_componentRepository.findAll();
        assertThat(command_components).hasSize(databaseSizeBeforeUpdate);
        Command_component testCommand_component = command_components.get(command_components.size() - 1);
        assertThat(testCommand_component.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void deleteCommand_component() throws Exception {
        // Initialize the database
        command_componentRepository.saveAndFlush(command_component);
        int databaseSizeBeforeDelete = command_componentRepository.findAll().size();

        // Get the command_component
        restCommand_componentMockMvc.perform(delete("/api/command-components/{id}", command_component.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Command_component> command_components = command_componentRepository.findAll();
        assertThat(command_components).hasSize(databaseSizeBeforeDelete - 1);
    }
}
