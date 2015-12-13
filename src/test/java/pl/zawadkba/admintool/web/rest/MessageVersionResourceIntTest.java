package pl.zawadkba.admintool.web.rest;

import pl.zawadkba.admintool.Application;
import pl.zawadkba.admintool.domain.MessageVersion;
import pl.zawadkba.admintool.repository.MessageVersionRepository;

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
 * Test class for the MessageVersionResource REST controller.
 *
 * @see MessageVersionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MessageVersionResourceIntTest {

    private static final String DEFAULT_VERSION = "AAAAA";
    private static final String UPDATED_VERSION = "BBBBB";

    @Inject
    private MessageVersionRepository messageVersionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMessageVersionMockMvc;

    private MessageVersion messageVersion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MessageVersionResource messageVersionResource = new MessageVersionResource();
        ReflectionTestUtils.setField(messageVersionResource, "messageVersionRepository", messageVersionRepository);
        this.restMessageVersionMockMvc = MockMvcBuilders.standaloneSetup(messageVersionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        messageVersion = new MessageVersion();
        messageVersion.setVersion(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    public void createMessageVersion() throws Exception {
        int databaseSizeBeforeCreate = messageVersionRepository.findAll().size();

        // Create the MessageVersion

        restMessageVersionMockMvc.perform(post("/api/messageVersions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messageVersion)))
                .andExpect(status().isCreated());

        // Validate the MessageVersion in the database
        List<MessageVersion> messageVersions = messageVersionRepository.findAll();
        assertThat(messageVersions).hasSize(databaseSizeBeforeCreate + 1);
        MessageVersion testMessageVersion = messageVersions.get(messageVersions.size() - 1);
        assertThat(testMessageVersion.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    public void checkVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageVersionRepository.findAll().size();
        // set the field null
        messageVersion.setVersion(null);

        // Create the MessageVersion, which fails.

        restMessageVersionMockMvc.perform(post("/api/messageVersions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messageVersion)))
                .andExpect(status().isBadRequest());

        List<MessageVersion> messageVersions = messageVersionRepository.findAll();
        assertThat(messageVersions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMessageVersions() throws Exception {
        // Initialize the database
        messageVersionRepository.saveAndFlush(messageVersion);

        // Get all the messageVersions
        restMessageVersionMockMvc.perform(get("/api/messageVersions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(messageVersion.getId().intValue())))
                .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())));
    }

    @Test
    @Transactional
    public void getMessageVersion() throws Exception {
        // Initialize the database
        messageVersionRepository.saveAndFlush(messageVersion);

        // Get the messageVersion
        restMessageVersionMockMvc.perform(get("/api/messageVersions/{id}", messageVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(messageVersion.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMessageVersion() throws Exception {
        // Get the messageVersion
        restMessageVersionMockMvc.perform(get("/api/messageVersions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessageVersion() throws Exception {
        // Initialize the database
        messageVersionRepository.saveAndFlush(messageVersion);

		int databaseSizeBeforeUpdate = messageVersionRepository.findAll().size();

        // Update the messageVersion
        messageVersion.setVersion(UPDATED_VERSION);

        restMessageVersionMockMvc.perform(put("/api/messageVersions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messageVersion)))
                .andExpect(status().isOk());

        // Validate the MessageVersion in the database
        List<MessageVersion> messageVersions = messageVersionRepository.findAll();
        assertThat(messageVersions).hasSize(databaseSizeBeforeUpdate);
        MessageVersion testMessageVersion = messageVersions.get(messageVersions.size() - 1);
        assertThat(testMessageVersion.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void deleteMessageVersion() throws Exception {
        // Initialize the database
        messageVersionRepository.saveAndFlush(messageVersion);

		int databaseSizeBeforeDelete = messageVersionRepository.findAll().size();

        // Get the messageVersion
        restMessageVersionMockMvc.perform(delete("/api/messageVersions/{id}", messageVersion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MessageVersion> messageVersions = messageVersionRepository.findAll();
        assertThat(messageVersions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
