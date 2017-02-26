package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Frame;
import com.madera.app.repository.FrameRepository;

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
 * Test class for the FrameResource REST controller.
 *
 * @see FrameResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class FrameResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    @Inject
    private FrameRepository frameRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFrameMockMvc;

    private Frame frame;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FrameResource frameResource = new FrameResource();
        ReflectionTestUtils.setField(frameResource, "frameRepository", frameRepository);
        this.restFrameMockMvc = MockMvcBuilders.standaloneSetup(frameResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Frame createEntity(EntityManager em) {
        Frame frame = new Frame()
                .name(DEFAULT_NAME)
                .url(DEFAULT_URL);
        return frame;
    }

    @Before
    public void initTest() {
        frame = createEntity(em);
    }

    @Test
    @Transactional
    public void createFrame() throws Exception {
        int databaseSizeBeforeCreate = frameRepository.findAll().size();

        // Create the Frame

        restFrameMockMvc.perform(post("/api/frames")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(frame)))
                .andExpect(status().isCreated());

        // Validate the Frame in the database
        List<Frame> frames = frameRepository.findAll();
        assertThat(frames).hasSize(databaseSizeBeforeCreate + 1);
        Frame testFrame = frames.get(frames.size() - 1);
        assertThat(testFrame.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFrame.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void getAllFrames() throws Exception {
        // Initialize the database
        frameRepository.saveAndFlush(frame);

        // Get all the frames
        restFrameMockMvc.perform(get("/api/frames?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(frame.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void getFrame() throws Exception {
        // Initialize the database
        frameRepository.saveAndFlush(frame);

        // Get the frame
        restFrameMockMvc.perform(get("/api/frames/{id}", frame.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(frame.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFrame() throws Exception {
        // Get the frame
        restFrameMockMvc.perform(get("/api/frames/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFrame() throws Exception {
        // Initialize the database
        frameRepository.saveAndFlush(frame);
        int databaseSizeBeforeUpdate = frameRepository.findAll().size();

        // Update the frame
        Frame updatedFrame = frameRepository.findOne(frame.getId());
        updatedFrame
                .name(UPDATED_NAME)
                .url(UPDATED_URL);

        restFrameMockMvc.perform(put("/api/frames")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFrame)))
                .andExpect(status().isOk());

        // Validate the Frame in the database
        List<Frame> frames = frameRepository.findAll();
        assertThat(frames).hasSize(databaseSizeBeforeUpdate);
        Frame testFrame = frames.get(frames.size() - 1);
        assertThat(testFrame.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFrame.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void deleteFrame() throws Exception {
        // Initialize the database
        frameRepository.saveAndFlush(frame);
        int databaseSizeBeforeDelete = frameRepository.findAll().size();

        // Get the frame
        restFrameMockMvc.perform(delete("/api/frames/{id}", frame.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Frame> frames = frameRepository.findAll();
        assertThat(frames).hasSize(databaseSizeBeforeDelete - 1);
    }
}
