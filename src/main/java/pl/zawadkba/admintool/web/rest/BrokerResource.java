package pl.zawadkba.admintool.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.zawadkba.admintool.domain.Broker;
import pl.zawadkba.admintool.repository.BrokerRepository;
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
 * REST controller for managing Broker.
 */
@RestController
@RequestMapping("/api")
public class BrokerResource {

    private final Logger log = LoggerFactory.getLogger(BrokerResource.class);

    @Inject
    private BrokerRepository brokerRepository;

    /**
     * POST  /brokers -> Create a new broker.
     */
    @RequestMapping(value = "/brokers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Broker> createBroker(@Valid @RequestBody Broker broker) throws URISyntaxException {
        log.debug("REST request to save Broker : {}", broker);
        if (broker.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new broker cannot already have an ID").body(null);
        }
        Broker result = brokerRepository.save(broker);
        return ResponseEntity.created(new URI("/api/brokers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("broker", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /brokers -> Updates an existing broker.
     */
    @RequestMapping(value = "/brokers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Broker> updateBroker(@Valid @RequestBody Broker broker) throws URISyntaxException {
        log.debug("REST request to update Broker : {}", broker);
        if (broker.getId() == null) {
            return createBroker(broker);
        }
        Broker result = brokerRepository.save(broker);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("broker", broker.getId().toString()))
            .body(result);
    }

    /**
     * GET  /brokers -> get all the brokers.
     */
    @RequestMapping(value = "/brokers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Broker> getAllBrokers() {
        log.debug("REST request to get all Brokers");
        return brokerRepository.findAll();
    }

    /**
     * GET  /brokers/:id -> get the "id" broker.
     */
    @RequestMapping(value = "/brokers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Broker> getBroker(@PathVariable Long id) {
        log.debug("REST request to get Broker : {}", id);
        return Optional.ofNullable(brokerRepository.findOne(id))
            .map(broker -> new ResponseEntity<>(
                broker,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /brokers/:id -> delete the "id" broker.
     */
    @RequestMapping(value = "/brokers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBroker(@PathVariable Long id) {
        log.debug("REST request to delete Broker : {}", id);
        brokerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("broker", id.toString())).build();
    }
}
