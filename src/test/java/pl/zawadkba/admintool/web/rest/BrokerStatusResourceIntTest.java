package pl.zawadkba.admintool.web.rest;

import pl.zawadkba.admintool.Application;
import pl.zawadkba.admintool.domain.BrokerStatus;
import pl.zawadkba.admintool.repository.BrokerStatusRepository;

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
 * Test class for the BrokerStatusResource REST controller.
 *
 * @see BrokerStatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BrokerStatusResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";

    @Inject
    private BrokerStatusRepository brokerStatusRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBrokerStatusMockMvc;

    private BrokerStatus brokerStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BrokerStatusResource brokerStatusResource = new BrokerStatusResource();
        ReflectionTestUtils.setField(brokerStatusResource, "brokerStatusRepository", brokerStatusRepository);
        this.restBrokerStatusMockMvc = MockMvcBuilders.standaloneSetup(brokerStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        brokerStatus = new BrokerStatus();
        brokerStatus.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createBrokerStatus() throws Exception {
        int databaseSizeBeforeCreate = brokerStatusRepository.findAll().size();

        // Create the BrokerStatus

        restBrokerStatusMockMvc.perform(post("/api/brokerStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(brokerStatus)))
                .andExpect(status().isCreated());

        // Validate the BrokerStatus in the database
        List<BrokerStatus> brokerStatuss = brokerStatusRepository.findAll();
        assertThat(brokerStatuss).hasSize(databaseSizeBeforeCreate + 1);
        BrokerStatus testBrokerStatus = brokerStatuss.get(brokerStatuss.size() - 1);
        assertThat(testBrokerStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerStatusRepository.findAll().size();
        // set the field null
        brokerStatus.setStatus(null);

        // Create the BrokerStatus, which fails.

        restBrokerStatusMockMvc.perform(post("/api/brokerStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(brokerStatus)))
                .andExpect(status().isBadRequest());

        List<BrokerStatus> brokerStatuss = brokerStatusRepository.findAll();
        assertThat(brokerStatuss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBrokerStatuss() throws Exception {
        // Initialize the database
        brokerStatusRepository.saveAndFlush(brokerStatus);

        // Get all the brokerStatuss
        restBrokerStatusMockMvc.perform(get("/api/brokerStatuss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(brokerStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getBrokerStatus() throws Exception {
        // Initialize the database
        brokerStatusRepository.saveAndFlush(brokerStatus);

        // Get the brokerStatus
        restBrokerStatusMockMvc.perform(get("/api/brokerStatuss/{id}", brokerStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(brokerStatus.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBrokerStatus() throws Exception {
        // Get the brokerStatus
        restBrokerStatusMockMvc.perform(get("/api/brokerStatuss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBrokerStatus() throws Exception {
        // Initialize the database
        brokerStatusRepository.saveAndFlush(brokerStatus);

		int databaseSizeBeforeUpdate = brokerStatusRepository.findAll().size();

        // Update the brokerStatus
        brokerStatus.setStatus(UPDATED_STATUS);

        restBrokerStatusMockMvc.perform(put("/api/brokerStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(brokerStatus)))
                .andExpect(status().isOk());

        // Validate the BrokerStatus in the database
        List<BrokerStatus> brokerStatuss = brokerStatusRepository.findAll();
        assertThat(brokerStatuss).hasSize(databaseSizeBeforeUpdate);
        BrokerStatus testBrokerStatus = brokerStatuss.get(brokerStatuss.size() - 1);
        assertThat(testBrokerStatus.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteBrokerStatus() throws Exception {
        // Initialize the database
        brokerStatusRepository.saveAndFlush(brokerStatus);

		int databaseSizeBeforeDelete = brokerStatusRepository.findAll().size();

        // Get the brokerStatus
        restBrokerStatusMockMvc.perform(delete("/api/brokerStatuss/{id}", brokerStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BrokerStatus> brokerStatuss = brokerStatusRepository.findAll();
        assertThat(brokerStatuss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
