package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Unit_used;

import com.madera.app.repository.Unit_usedRepository;
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
 * REST controller for managing Unit_used.
 */
@RestController
@RequestMapping("/api")
public class Unit_usedResource {

    private final Logger log = LoggerFactory.getLogger(Unit_usedResource.class);
        
    @Inject
    private Unit_usedRepository unit_usedRepository;

    /**
     * POST  /unit-useds : Create a new unit_used.
     *
     * @param unit_used the unit_used to create
     * @return the ResponseEntity with status 201 (Created) and with body the new unit_used, or with status 400 (Bad Request) if the unit_used has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/unit-useds",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Unit_used> createUnit_used(@Valid @RequestBody Unit_used unit_used) throws URISyntaxException {
        log.debug("REST request to save Unit_used : {}", unit_used);
        if (unit_used.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("unit_used", "idexists", "A new unit_used cannot already have an ID")).body(null);
        }
        Unit_used result = unit_usedRepository.save(unit_used);
        return ResponseEntity.created(new URI("/api/unit-useds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("unit_used", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /unit-useds : Updates an existing unit_used.
     *
     * @param unit_used the unit_used to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated unit_used,
     * or with status 400 (Bad Request) if the unit_used is not valid,
     * or with status 500 (Internal Server Error) if the unit_used couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/unit-useds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Unit_used> updateUnit_used(@Valid @RequestBody Unit_used unit_used) throws URISyntaxException {
        log.debug("REST request to update Unit_used : {}", unit_used);
        if (unit_used.getId() == null) {
            return createUnit_used(unit_used);
        }
        Unit_used result = unit_usedRepository.save(unit_used);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("unit_used", unit_used.getId().toString()))
            .body(result);
    }

    /**
     * GET  /unit-useds : get all the unit_useds.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of unit_useds in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/unit-useds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Unit_used>> getAllUnit_useds(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Unit_useds");
        Page<Unit_used> page = unit_usedRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/unit-useds");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /unit-useds/:id : get the "id" unit_used.
     *
     * @param id the id of the unit_used to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the unit_used, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/unit-useds/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Unit_used> getUnit_used(@PathVariable Long id) {
        log.debug("REST request to get Unit_used : {}", id);
        Unit_used unit_used = unit_usedRepository.findOne(id);
        return Optional.ofNullable(unit_used)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /unit-useds/:id : delete the "id" unit_used.
     *
     * @param id the id of the unit_used to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/unit-useds/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUnit_used(@PathVariable Long id) {
        log.debug("REST request to delete Unit_used : {}", id);
        unit_usedRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("unit_used", id.toString())).build();
    }

}
