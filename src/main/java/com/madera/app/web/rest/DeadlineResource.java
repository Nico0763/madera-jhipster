package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Deadline;

import com.madera.app.repository.DeadlineRepository;
import com.madera.app.web.rest.util.HeaderUtil;
import com.madera.app.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing Deadline.
 */
@RestController
@RequestMapping("/api")
public class DeadlineResource {

    private final Logger log = LoggerFactory.getLogger(DeadlineResource.class);
        
    @Inject
    private DeadlineRepository deadlineRepository;

    /**
     * POST  /deadlines : Create a new deadline.
     *
     * @param deadline the deadline to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deadline, or with status 400 (Bad Request) if the deadline has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/deadlines",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Deadline> createDeadline(@Valid @RequestBody Deadline deadline) throws URISyntaxException {
        log.debug("REST request to save Deadline : {}", deadline);
        if (deadline.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("deadline", "idexists", "A new deadline cannot already have an ID")).body(null);
        }
        Deadline result = deadlineRepository.save(deadline);
        return ResponseEntity.created(new URI("/api/deadlines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("deadline", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /deadlines : Updates an existing deadline.
     *
     * @param deadline the deadline to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deadline,
     * or with status 400 (Bad Request) if the deadline is not valid,
     * or with status 500 (Internal Server Error) if the deadline couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/deadlines",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Deadline> updateDeadline(@Valid @RequestBody Deadline deadline) throws URISyntaxException {
        log.debug("REST request to update Deadline : {}", deadline);
        if (deadline.getId() == null) {
            return createDeadline(deadline);
        }
        Deadline result = deadlineRepository.save(deadline);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("deadline", deadline.getId().toString()))
            .body(result);
    }

    /**
     * GET  /deadlines : get all the deadlines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of deadlines in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/deadlines",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Deadline>> getAllDeadlines(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Deadlines");
        Page<Deadline> page = deadlineRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/deadlines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /deadlines/:id : get the "id" deadline.
     *
     * @param id the id of the deadline to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deadline, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/deadlines/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Deadline> getDeadline(@PathVariable Long id) {
        log.debug("REST request to get Deadline : {}", id);
        Deadline deadline = deadlineRepository.findOne(id);
        return Optional.ofNullable(deadline)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /deadlines/:id : delete the "id" deadline.
     *
     * @param id the id of the deadline to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/deadlines/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDeadline(@PathVariable Long id) {
        log.debug("REST request to delete Deadline : {}", id);
        deadlineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("deadline", id.toString())).build();
    }

}
