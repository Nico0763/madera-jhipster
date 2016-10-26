package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Quotation;
import com.madera.app.repository.QuotationRepository;

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
 * Test class for the QuotationResource REST controller.
 *
 * @see QuotationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class QuotationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_STATE = 9;
    private static final Integer UPDATED_STATE = 8;

    private static final Float DEFAULT_COMMERCIAL_PERCENTAGE = 1F;
    private static final Float UPDATED_COMMERCIAL_PERCENTAGE = 2F;

    private static final String DEFAULT_REFERENCE = "AAAAA";
    private static final String UPDATED_REFERENCE = "BBBBB";

    @Inject
    private QuotationRepository quotationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restQuotationMockMvc;

    private Quotation quotation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QuotationResource quotationResource = new QuotationResource();
        ReflectionTestUtils.setField(quotationResource, "quotationRepository", quotationRepository);
        this.restQuotationMockMvc = MockMvcBuilders.standaloneSetup(quotationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quotation createEntity(EntityManager em) {
        Quotation quotation = new Quotation()
                .name(DEFAULT_NAME)
                .date(DEFAULT_DATE)
                .state(DEFAULT_STATE)
                .commercial_percentage(DEFAULT_COMMERCIAL_PERCENTAGE)
                .reference(DEFAULT_REFERENCE);
        return quotation;
    }

    @Before
    public void initTest() {
        quotation = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuotation() throws Exception {
        int databaseSizeBeforeCreate = quotationRepository.findAll().size();

        // Create the Quotation

        restQuotationMockMvc.perform(post("/api/quotations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(quotation)))
                .andExpect(status().isCreated());

        // Validate the Quotation in the database
        List<Quotation> quotations = quotationRepository.findAll();
        assertThat(quotations).hasSize(databaseSizeBeforeCreate + 1);
        Quotation testQuotation = quotations.get(quotations.size() - 1);
        assertThat(testQuotation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuotation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testQuotation.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testQuotation.getCommercial_percentage()).isEqualTo(DEFAULT_COMMERCIAL_PERCENTAGE);
        assertThat(testQuotation.getReference()).isEqualTo(DEFAULT_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllQuotations() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotations
        restQuotationMockMvc.perform(get("/api/quotations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(quotation.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
                .andExpect(jsonPath("$.[*].commercial_percentage").value(hasItem(DEFAULT_COMMERCIAL_PERCENTAGE.doubleValue())))
                .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())));
    }

    @Test
    @Transactional
    public void getQuotation() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get the quotation
        restQuotationMockMvc.perform(get("/api/quotations/{id}", quotation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quotation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.commercial_percentage").value(DEFAULT_COMMERCIAL_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQuotation() throws Exception {
        // Get the quotation
        restQuotationMockMvc.perform(get("/api/quotations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuotation() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);
        int databaseSizeBeforeUpdate = quotationRepository.findAll().size();

        // Update the quotation
        Quotation updatedQuotation = quotationRepository.findOne(quotation.getId());
        updatedQuotation
                .name(UPDATED_NAME)
                .date(UPDATED_DATE)
                .state(UPDATED_STATE)
                .commercial_percentage(UPDATED_COMMERCIAL_PERCENTAGE)
                .reference(UPDATED_REFERENCE);

        restQuotationMockMvc.perform(put("/api/quotations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedQuotation)))
                .andExpect(status().isOk());

        // Validate the Quotation in the database
        List<Quotation> quotations = quotationRepository.findAll();
        assertThat(quotations).hasSize(databaseSizeBeforeUpdate);
        Quotation testQuotation = quotations.get(quotations.size() - 1);
        assertThat(testQuotation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuotation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testQuotation.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testQuotation.getCommercial_percentage()).isEqualTo(UPDATED_COMMERCIAL_PERCENTAGE);
        assertThat(testQuotation.getReference()).isEqualTo(UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void deleteQuotation() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);
        int databaseSizeBeforeDelete = quotationRepository.findAll().size();

        // Get the quotation
        restQuotationMockMvc.perform(delete("/api/quotations/{id}", quotation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Quotation> quotations = quotationRepository.findAll();
        assertThat(quotations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
