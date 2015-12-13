package pl.zawadkba.admintool.web.rest;

import pl.zawadkba.admintool.Application;
import pl.zawadkba.admintool.domain.Broker;
import pl.zawadkba.admintool.repository.BrokerRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the BrokerResource REST controller.
 *
 * @see BrokerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BrokerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_COMMENT = "AAAAA";
    private static final String UPDATED_COMMENT = "BBBBB";

    @Inject
    private BrokerRepository brokerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBrokerMockMvc;

    private Broker broker;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BrokerResource brokerResource = new BrokerResource();
        ReflectionTestUtils.setField(brokerResource, "brokerRepository", brokerRepository);
        this.restBrokerMockMvc = MockMvcBuilders.standaloneSetup(brokerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        broker = new Broker();
        broker.setName(DEFAULT_NAME);
        broker.setComment(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void createBroker() throws Exception {
        int databaseSizeBeforeCreate = brokerRepository.findAll().size();

        // Create the Broker

        restBrokerMockMvc.perform(post("/api/brokers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(broker)))
                .andExpect(status().isCreated());

        // Validate the Broker in the database
        List<Broker> brokers = brokerRepository.findAll();
        assertThat(brokers).hasSize(databaseSizeBeforeCreate + 1);
        Broker testBroker = brokers.get(brokers.size() - 1);
        assertThat(testBroker.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBroker.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerRepository.findAll().size();
        // set the field null
        broker.setName(null);

        // Create the Broker, which fails.

        restBrokerMockMvc.perform(post("/api/brokers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(broker)))
                .andExpect(status().isBadRequest());

        List<Broker> brokers = brokerRepository.findAll();
        assertThat(brokers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBrokers() throws Exception {
        // Initialize the database
        brokerRepository.saveAndFlush(broker);

        // Get all the brokers
        restBrokerMockMvc.perform(get("/api/brokers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(broker.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void getBroker() throws Exception {
        // Initialize the database
        brokerRepository.saveAndFlush(broker);

        // Get the broker
        restBrokerMockMvc.perform(get("/api/brokers/{id}", broker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(broker.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBroker() throws Exception {
        // Get the broker
        restBrokerMockMvc.perform(get("/api/brokers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBroker() throws Exception {
        // Initialize the database
        brokerRepository.saveAndFlush(broker);

		int databaseSizeBeforeUpdate = brokerRepository.findAll().size();

        // Update the broker
        broker.setName(UPDATED_NAME);
        broker.setComment(UPDATED_COMMENT);

        restBrokerMockMvc.perform(put("/api/brokers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(broker)))
                .andExpect(status().isOk());

        // Validate the Broker in the database
        List<Broker> brokers = brokerRepository.findAll();
        assertThat(brokers).hasSize(databaseSizeBeforeUpdate);
        Broker testBroker = brokers.get(brokers.size() - 1);
        assertThat(testBroker.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBroker.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void deleteBroker() throws Exception {
        // Initialize the database
        brokerRepository.saveAndFlush(broker);

		int databaseSizeBeforeDelete = brokerRepository.findAll().size();

        // Get the broker
        restBrokerMockMvc.perform(delete("/api/brokers/{id}", broker.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Broker> brokers = brokerRepository.findAll();
        assertThat(brokers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
