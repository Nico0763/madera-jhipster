package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Pattern;
import com.madera.app.repository.PatternRepository;

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
 * Test class for the PatternResource REST controller.
 *
 * @see PatternResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class PatternResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    @Inject
    private PatternRepository patternRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPatternMockMvc;

    private Pattern pattern;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PatternResource patternResource = new PatternResource();
        ReflectionTestUtils.setField(patternResource, "patternRepository", patternRepository);
        this.restPatternMockMvc = MockMvcBuilders.standaloneSetup(patternResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pattern createEntity(EntityManager em) {
        Pattern pattern = new Pattern()
                .name(DEFAULT_NAME)
                .url(DEFAULT_URL);
        return pattern;
    }

    @Before
    public void initTest() {
        pattern = createEntity(em);
    }

    @Test
    @Transactional
    public void createPattern() throws Exception {
        int databaseSizeBeforeCreate = patternRepository.findAll().size();

        // Create the Pattern

        restPatternMockMvc.perform(post("/api/patterns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pattern)))
                .andExpect(status().isCreated());

        // Validate the Pattern in the database
        List<Pattern> patterns = patternRepository.findAll();
        assertThat(patterns).hasSize(databaseSizeBeforeCreate + 1);
        Pattern testPattern = patterns.get(patterns.size() - 1);
        assertThat(testPattern.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPattern.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void getAllPatterns() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);

        // Get all the patterns
        restPatternMockMvc.perform(get("/api/patterns?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pattern.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void getPattern() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);

        // Get the pattern
        restPatternMockMvc.perform(get("/api/patterns/{id}", pattern.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pattern.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPattern() throws Exception {
        // Get the pattern
        restPatternMockMvc.perform(get("/api/patterns/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePattern() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);
        int databaseSizeBeforeUpdate = patternRepository.findAll().size();

        // Update the pattern
        Pattern updatedPattern = patternRepository.findOne(pattern.getId());
        updatedPattern
                .name(UPDATED_NAME)
                .url(UPDATED_URL);

        restPatternMockMvc.perform(put("/api/patterns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPattern)))
                .andExpect(status().isOk());

        // Validate the Pattern in the database
        List<Pattern> patterns = patternRepository.findAll();
        assertThat(patterns).hasSize(databaseSizeBeforeUpdate);
        Pattern testPattern = patterns.get(patterns.size() - 1);
        assertThat(testPattern.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPattern.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void deletePattern() throws Exception {
        // Initialize the database
        patternRepository.saveAndFlush(pattern);
        int databaseSizeBeforeDelete = patternRepository.findAll().size();

        // Get the pattern
        restPatternMockMvc.perform(delete("/api/patterns/{id}", pattern.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pattern> patterns = patternRepository.findAll();
        assertThat(patterns).hasSize(databaseSizeBeforeDelete - 1);
    }
}
