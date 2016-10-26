package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Assortment;

import com.madera.app.repository.AssortmentRepository;
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
 * REST controller for managing Assortment.
 */
@RestController
@RequestMapping("/api")
public class AssortmentResource {

    private final Logger log = LoggerFactory.getLogger(AssortmentResource.class);
        
    @Inject
    private AssortmentRepository assortmentRepository;

    /**
     * POST  /assortments : Create a new assortment.
     *
     * @param assortment the assortment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new assortment, or with status 400 (Bad Request) if the assortment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/assortments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Assortment> createAssortment(@Valid @RequestBody Assortment assortment) throws URISyntaxException {
        log.debug("REST request to save Assortment : {}", assortment);
        if (assortment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("assortment", "idexists", "A new assortment cannot already have an ID")).body(null);
        }
        Assortment result = assortmentRepository.save(assortment);
        return ResponseEntity.created(new URI("/api/assortments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("assortment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /assortments : Updates an existing assortment.
     *
     * @param assortment the assortment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated assortment,
     * or with status 400 (Bad Request) if the assortment is not valid,
     * or with status 500 (Internal Server Error) if the assortment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/assortments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Assortment> updateAssortment(@Valid @RequestBody Assortment assortment) throws URISyntaxException {
        log.debug("REST request to update Assortment : {}", assortment);
        if (assortment.getId() == null) {
            return createAssortment(assortment);
        }
        Assortment result = assortmentRepository.save(assortment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("assortment", assortment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /assortments : get all the assortments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of assortments in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/assortments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Assortment>> getAllAssortments(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Assortments");
        Page<Assortment> page = assortmentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/assortments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /assortments/:id : get the "id" assortment.
     *
     * @param id the id of the assortment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the assortment, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/assortments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Assortment> getAssortment(@PathVariable Long id) {
        log.debug("REST request to get Assortment : {}", id);
        Assortment assortment = assortmentRepository.findOne(id);
        return Optional.ofNullable(assortment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /assortments/:id : delete the "id" assortment.
     *
     * @param id the id of the assortment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/assortments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAssortment(@PathVariable Long id) {
        log.debug("REST request to delete Assortment : {}", id);
        assortmentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("assortment", id.toString())).build();
    }

}
