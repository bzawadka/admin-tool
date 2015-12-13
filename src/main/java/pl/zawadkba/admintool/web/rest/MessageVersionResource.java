package pl.zawadkba.admintool.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.zawadkba.admintool.domain.MessageVersion;
import pl.zawadkba.admintool.repository.MessageVersionRepository;
import pl.zawadkba.admintool.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MessageVersion.
 */
@RestController
@RequestMapping("/api")
public class MessageVersionResource {

    private final Logger log = LoggerFactory.getLogger(MessageVersionResource.class);

    @Inject
    private MessageVersionRepository messageVersionRepository;

    /**
     * POST  /messageVersions -> Create a new messageVersion.
     */
    @RequestMapping(value = "/messageVersions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MessageVersion> createMessageVersion(@Valid @RequestBody MessageVersion messageVersion) throws URISyntaxException {
        log.debug("REST request to save MessageVersion : {}", messageVersion);
        if (messageVersion.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new messageVersion cannot already have an ID").body(null);
        }
        MessageVersion result = messageVersionRepository.save(messageVersion);
        return ResponseEntity.created(new URI("/api/messageVersions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("messageVersion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /messageVersions -> Updates an existing messageVersion.
     */
    @RequestMapping(value = "/messageVersions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MessageVersion> updateMessageVersion(@Valid @RequestBody MessageVersion messageVersion) throws URISyntaxException {
        log.debug("REST request to update MessageVersion : {}", messageVersion);
        if (messageVersion.getId() == null) {
            return createMessageVersion(messageVersion);
        }
        MessageVersion result = messageVersionRepository.save(messageVersion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("messageVersion", messageVersion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /messageVersions -> get all the messageVersions.
     */
    @RequestMapping(value = "/messageVersions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MessageVersion> getAllMessageVersions() {
        log.debug("REST request to get all MessageVersions");
        return messageVersionRepository.findAll();
    }

    /**
     * GET  /messageVersions/:id -> get the "id" messageVersion.
     */
    @RequestMapping(value = "/messageVersions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MessageVersion> getMessageVersion(@PathVariable Long id) {
        log.debug("REST request to get MessageVersion : {}", id);
        return Optional.ofNullable(messageVersionRepository.findOne(id))
            .map(messageVersion -> new ResponseEntity<>(
                messageVersion,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /messageVersions/:id -> delete the "id" messageVersion.
     */
    @RequestMapping(value = "/messageVersions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMessageVersion(@PathVariable Long id) {
        log.debug("REST request to delete MessageVersion : {}", id);
        messageVersionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("messageVersion", id.toString())).build();
    }
}
