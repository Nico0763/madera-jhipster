package com.madera.app.web.rest;

import com.madera.app.MaderaApp;

import com.madera.app.domain.Provider;
import com.madera.app.repository.ProviderRepository;

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
 * Test class for the ProviderResource REST controller.
 *
 * @see ProviderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaderaApp.class)
public class ProviderResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_PC = "AAAAA";
    private static final String UPDATED_PC = "BBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBB";

    private static final String DEFAULT_MAIL = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private ProviderRepository providerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProviderMockMvc;

    private Provider provider;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProviderResource providerResource = new ProviderResource();
        ReflectionTestUtils.setField(providerResource, "providerRepository", providerRepository);
        this.restProviderMockMvc = MockMvcBuilders.standaloneSetup(providerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Provider createEntity(EntityManager em) {
        Provider provider = new Provider()
                .name(DEFAULT_NAME)
                .address(DEFAULT_ADDRESS)
                .pc(DEFAULT_PC)
                .city(DEFAULT_CITY)
                .country(DEFAULT_COUNTRY)
                .phone_number(DEFAULT_PHONE_NUMBER)
                .mail(DEFAULT_MAIL);
        return provider;
    }

    @Before
    public void initTest() {
        provider = createEntity(em);
    }

    @Test
    @Transactional
    public void createProvider() throws Exception {
        int databaseSizeBeforeCreate = providerRepository.findAll().size();

        // Create the Provider

        restProviderMockMvc.perform(post("/api/providers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(provider)))
                .andExpect(status().isCreated());

        // Validate the Provider in the database
        List<Provider> providers = providerRepository.findAll();
        assertThat(providers).hasSize(databaseSizeBeforeCreate + 1);
        Provider testProvider = providers.get(providers.size() - 1);
        assertThat(testProvider.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProvider.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testProvider.getPc()).isEqualTo(DEFAULT_PC);
        assertThat(testProvider.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testProvider.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testProvider.getPhone_number()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testProvider.getMail()).isEqualTo(DEFAULT_MAIL);
    }

    @Test
    @Transactional
    public void getAllProviders() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        // Get all the providers
        restProviderMockMvc.perform(get("/api/providers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(provider.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].pc").value(hasItem(DEFAULT_PC.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
                .andExpect(jsonPath("$.[*].phone_number").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL.toString())));
    }

    @Test
    @Transactional
    public void getProvider() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        // Get the provider
        restProviderMockMvc.perform(get("/api/providers/{id}", provider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(provider.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.pc").value(DEFAULT_PC.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.phone_number").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProvider() throws Exception {
        // Get the provider
        restProviderMockMvc.perform(get("/api/providers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProvider() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);
        int databaseSizeBeforeUpdate = providerRepository.findAll().size();

        // Update the provider
        Provider updatedProvider = providerRepository.findOne(provider.getId());
        updatedProvider
                .name(UPDATED_NAME)
                .address(UPDATED_ADDRESS)
                .pc(UPDATED_PC)
                .city(UPDATED_CITY)
                .country(UPDATED_COUNTRY)
                .phone_number(UPDATED_PHONE_NUMBER)
                .mail(UPDATED_MAIL);

        restProviderMockMvc.perform(put("/api/providers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProvider)))
                .andExpect(status().isOk());

        // Validate the Provider in the database
        List<Provider> providers = providerRepository.findAll();
        assertThat(providers).hasSize(databaseSizeBeforeUpdate);
        Provider testProvider = providers.get(providers.size() - 1);
        assertThat(testProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProvider.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testProvider.getPc()).isEqualTo(UPDATED_PC);
        assertThat(testProvider.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testProvider.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testProvider.getPhone_number()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testProvider.getMail()).isEqualTo(UPDATED_MAIL);
    }

    @Test
    @Transactional
    public void deleteProvider() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);
        int databaseSizeBeforeDelete = providerRepository.findAll().size();

        // Get the provider
        restProviderMockMvc.perform(delete("/api/providers/{id}", provider.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Provider> providers = providerRepository.findAll();
        assertThat(providers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
