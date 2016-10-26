package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Deadline;
import com.madera.app.repository.DeadlineRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DeadlineResource REST controller.
 *
 * @see DeadlineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class DeadlineResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Float DEFAULT_PERCENTAGE = 1F;
    private static final Float UPDATED_PERCENTAGE = 2F;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private DeadlineRepository deadlineRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDeadlineMockMvc;

    private Deadline deadline;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DeadlineResource deadlineResource = new DeadlineResource();
        ReflectionTestUtils.setField(deadlineResource, "deadlineRepository", deadlineRepository);
        this.restDeadlineMockMvc = MockMvcBuilders.standaloneSetup(deadlineResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deadline createEntity(EntityManager em) {
        Deadline deadline = new Deadline()
                .name(DEFAULT_NAME)
                .percentage(DEFAULT_PERCENTAGE)
                .date(DEFAULT_DATE);
        return deadline;
    }

    @Before
    public void initTest() {
        deadline = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeadline() throws Exception {
        int databaseSizeBeforeCreate = deadlineRepository.findAll().size();

        // Create the Deadline

        restDeadlineMockMvc.perform(post("/api/deadlines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deadline)))
                .andExpect(status().isCreated());

        // Validate the Deadline in the database
        List<Deadline> deadlines = deadlineRepository.findAll();
        assertThat(deadlines).hasSize(databaseSizeBeforeCreate + 1);
        Deadline testDeadline = deadlines.get(deadlines.size() - 1);
        assertThat(testDeadline.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDeadline.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
        assertThat(testDeadline.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void getAllDeadlines() throws Exception {
        // Initialize the database
        deadlineRepository.saveAndFlush(deadline);

        // Get all the deadlines
        restDeadlineMockMvc.perform(get("/api/deadlines?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(deadline.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getDeadline() throws Exception {
        // Initialize the database
        deadlineRepository.saveAndFlush(deadline);

        // Get the deadline
        restDeadlineMockMvc.perform(get("/api/deadlines/{id}", deadline.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deadline.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDeadline() throws Exception {
        // Get the deadline
        restDeadlineMockMvc.perform(get("/api/deadlines/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeadline() throws Exception {
        // Initialize the database
        deadlineRepository.saveAndFlush(deadline);
        int databaseSizeBeforeUpdate = deadlineRepository.findAll().size();

        // Update the deadline
        Deadline updatedDeadline = deadlineRepository.findOne(deadline.getId());
        updatedDeadline
                .name(UPDATED_NAME)
                .percentage(UPDATED_PERCENTAGE)
                .date(UPDATED_DATE);

        restDeadlineMockMvc.perform(put("/api/deadlines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDeadline)))
                .andExpect(status().isOk());

        // Validate the Deadline in the database
        List<Deadline> deadlines = deadlineRepository.findAll();
        assertThat(deadlines).hasSize(databaseSizeBeforeUpdate);
        Deadline testDeadline = deadlines.get(deadlines.size() - 1);
        assertThat(testDeadline.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeadline.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
        assertThat(testDeadline.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteDeadline() throws Exception {
        // Initialize the database
        deadlineRepository.saveAndFlush(deadline);
        int databaseSizeBeforeDelete = deadlineRepository.findAll().size();

        // Get the deadline
        restDeadlineMockMvc.perform(delete("/api/deadlines/{id}", deadline.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Deadline> deadlines = deadlineRepository.findAll();
        assertThat(deadlines).hasSize(databaseSizeBeforeDelete - 1);
    }
}
