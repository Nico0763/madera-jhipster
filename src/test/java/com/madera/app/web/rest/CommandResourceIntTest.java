package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Command;
import com.madera.app.repository.CommandRepository;

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
 * Test class for the CommandResource REST controller.
 *
 * @see CommandResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class CommandResourceIntTest {

    private static final String DEFAULT_REFERENCE = "AAAAA";
    private static final String UPDATED_REFERENCE = "BBBBB";

    private static final Integer DEFAULT_STATE = 9;
    private static final Integer UPDATED_STATE = 8;

    @Inject
    private CommandRepository commandRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCommandMockMvc;

    private Command command;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommandResource commandResource = new CommandResource();
        ReflectionTestUtils.setField(commandResource, "commandRepository", commandRepository);
        this.restCommandMockMvc = MockMvcBuilders.standaloneSetup(commandResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Command createEntity(EntityManager em) {
        Command command = new Command()
                .reference(DEFAULT_REFERENCE)
                .state(DEFAULT_STATE);
        return command;
    }

    @Before
    public void initTest() {
        command = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommand() throws Exception {
        int databaseSizeBeforeCreate = commandRepository.findAll().size();

        // Create the Command

        restCommandMockMvc.perform(post("/api/commands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(command)))
                .andExpect(status().isCreated());

        // Validate the Command in the database
        List<Command> commands = commandRepository.findAll();
        assertThat(commands).hasSize(databaseSizeBeforeCreate + 1);
        Command testCommand = commands.get(commands.size() - 1);
        assertThat(testCommand.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testCommand.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void getAllCommands() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commands
        restCommandMockMvc.perform(get("/api/commands?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(command.getId().intValue())))
                .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)));
    }

    @Test
    @Transactional
    public void getCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get the command
        restCommandMockMvc.perform(get("/api/commands/{id}", command.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(command.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE));
    }

    @Test
    @Transactional
    public void getNonExistingCommand() throws Exception {
        // Get the command
        restCommandMockMvc.perform(get("/api/commands/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();

        // Update the command
        Command updatedCommand = commandRepository.findOne(command.getId());
        updatedCommand
                .reference(UPDATED_REFERENCE)
                .state(UPDATED_STATE);

        restCommandMockMvc.perform(put("/api/commands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCommand)))
                .andExpect(status().isOk());

        // Validate the Command in the database
        List<Command> commands = commandRepository.findAll();
        assertThat(commands).hasSize(databaseSizeBeforeUpdate);
        Command testCommand = commands.get(commands.size() - 1);
        assertThat(testCommand.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testCommand.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    public void deleteCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);
        int databaseSizeBeforeDelete = commandRepository.findAll().size();

        // Get the command
        restCommandMockMvc.perform(delete("/api/commands/{id}", command.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Command> commands = commandRepository.findAll();
        assertThat(commands).hasSize(databaseSizeBeforeDelete - 1);
    }
}
