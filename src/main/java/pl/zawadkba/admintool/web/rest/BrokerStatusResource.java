package pl.zawadkba.admintool.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.zawadkba.admintool.domain.BrokerStatus;
import pl.zawadkba.admintool.repository.BrokerStatusRepository;
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
 * REST controller for managing BrokerStatus.
 */
@RestController
@RequestMapping("/api")
public class BrokerStatusResource {

    private final Logger log = LoggerFactory.getLogger(BrokerStatusResource.class);

    @Inject
    private BrokerStatusRepository brokerStatusRepository;

    /**
     * POST  /brokerStatuss -> Create a new brokerStatus.
     */
    @RequestMapping(value = "/brokerStatuss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BrokerStatus> createBrokerStatus(@Valid @RequestBody BrokerStatus brokerStatus) throws URISyntaxException {
        log.debug("REST request to save BrokerStatus : {}", brokerStatus);
        if (brokerStatus.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new brokerStatus cannot already have an ID").body(null);
        }
        BrokerStatus result = brokerStatusRepository.save(brokerStatus);
        return ResponseEntity.created(new URI("/api/brokerStatuss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("brokerStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /brokerStatuss -> Updates an existing brokerStatus.
     */
    @RequestMapping(value = "/brokerStatuss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BrokerStatus> updateBrokerStatus(@Valid @RequestBody BrokerStatus brokerStatus) throws URISyntaxException {
        log.debug("REST request to update BrokerStatus : {}", brokerStatus);
        if (brokerStatus.getId() == null) {
            return createBrokerStatus(brokerStatus);
        }
        BrokerStatus result = brokerStatusRepository.save(brokerStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("brokerStatus", brokerStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /brokerStatuss -> get all the brokerStatuss.
     */
    @RequestMapping(value = "/brokerStatuss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BrokerStatus> getAllBrokerStatuss() {
        log.debug("REST request to get all BrokerStatuss");
        return brokerStatusRepository.findAll();
    }

    /**
     * GET  /brokerStatuss/:id -> get the "id" brokerStatus.
     */
    @RequestMapping(value = "/brokerStatuss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BrokerStatus> getBrokerStatus(@PathVariable Long id) {
        log.debug("REST request to get BrokerStatus : {}", id);
        return Optional.ofNullable(brokerStatusRepository.findOne(id))
            .map(brokerStatus -> new ResponseEntity<>(
                brokerStatus,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /brokerStatuss/:id -> delete the "id" brokerStatus.
     */
    @RequestMapping(value = "/brokerStatuss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBrokerStatus(@PathVariable Long id) {
        log.debug("REST request to delete BrokerStatus : {}", id);
        brokerStatusRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("brokerStatus", id.toString())).build();
    }
}
