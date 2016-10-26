package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Finition_ext;

import com.madera.app.repository.Finition_extRepository;
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
 * REST controller for managing Finition_ext.
 */
@RestController
@RequestMapping("/api")
public class Finition_extResource {

    private final Logger log = LoggerFactory.getLogger(Finition_extResource.class);
        
    @Inject
    private Finition_extRepository finition_extRepository;

    /**
     * POST  /finition-exts : Create a new finition_ext.
     *
     * @param finition_ext the finition_ext to create
     * @return the ResponseEntity with status 201 (Created) and with body the new finition_ext, or with status 400 (Bad Request) if the finition_ext has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/finition-exts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Finition_ext> createFinition_ext(@Valid @RequestBody Finition_ext finition_ext) throws URISyntaxException {
        log.debug("REST request to save Finition_ext : {}", finition_ext);
        if (finition_ext.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("finition_ext", "idexists", "A new finition_ext cannot already have an ID")).body(null);
        }
        Finition_ext result = finition_extRepository.save(finition_ext);
        return ResponseEntity.created(new URI("/api/finition-exts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("finition_ext", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /finition-exts : Updates an existing finition_ext.
     *
     * @param finition_ext the finition_ext to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated finition_ext,
     * or with status 400 (Bad Request) if the finition_ext is not valid,
     * or with status 500 (Internal Server Error) if the finition_ext couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/finition-exts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Finition_ext> updateFinition_ext(@Valid @RequestBody Finition_ext finition_ext) throws URISyntaxException {
        log.debug("REST request to update Finition_ext : {}", finition_ext);
        if (finition_ext.getId() == null) {
            return createFinition_ext(finition_ext);
        }
        Finition_ext result = finition_extRepository.save(finition_ext);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("finition_ext", finition_ext.getId().toString()))
            .body(result);
    }

    /**
     * GET  /finition-exts : get all the finition_exts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of finition_exts in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/finition-exts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Finition_ext>> getAllFinition_exts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Finition_exts");
        Page<Finition_ext> page = finition_extRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/finition-exts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /finition-exts/:id : get the "id" finition_ext.
     *
     * @param id the id of the finition_ext to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the finition_ext, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/finition-exts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Finition_ext> getFinition_ext(@PathVariable Long id) {
        log.debug("REST request to get Finition_ext : {}", id);
        Finition_ext finition_ext = finition_extRepository.findOne(id);
        return Optional.ofNullable(finition_ext)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /finition-exts/:id : delete the "id" finition_ext.
     *
     * @param id the id of the finition_ext to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/finition-exts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFinition_ext(@PathVariable Long id) {
        log.debug("REST request to delete Finition_ext : {}", id);
        finition_extRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("finition_ext", id.toString())).build();
    }

}
