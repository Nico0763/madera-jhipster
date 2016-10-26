package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Insulating_type;

import com.madera.app.repository.Insulating_typeRepository;
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
 * REST controller for managing Insulating_type.
 */
@RestController
@RequestMapping("/api")
public class Insulating_typeResource {

    private final Logger log = LoggerFactory.getLogger(Insulating_typeResource.class);
        
    @Inject
    private Insulating_typeRepository insulating_typeRepository;

    /**
     * POST  /insulating-types : Create a new insulating_type.
     *
     * @param insulating_type the insulating_type to create
     * @return the ResponseEntity with status 201 (Created) and with body the new insulating_type, or with status 400 (Bad Request) if the insulating_type has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/insulating-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Insulating_type> createInsulating_type(@Valid @RequestBody Insulating_type insulating_type) throws URISyntaxException {
        log.debug("REST request to save Insulating_type : {}", insulating_type);
        if (insulating_type.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("insulating_type", "idexists", "A new insulating_type cannot already have an ID")).body(null);
        }
        Insulating_type result = insulating_typeRepository.save(insulating_type);
        return ResponseEntity.created(new URI("/api/insulating-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("insulating_type", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /insulating-types : Updates an existing insulating_type.
     *
     * @param insulating_type the insulating_type to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated insulating_type,
     * or with status 400 (Bad Request) if the insulating_type is not valid,
     * or with status 500 (Internal Server Error) if the insulating_type couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/insulating-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Insulating_type> updateInsulating_type(@Valid @RequestBody Insulating_type insulating_type) throws URISyntaxException {
        log.debug("REST request to update Insulating_type : {}", insulating_type);
        if (insulating_type.getId() == null) {
            return createInsulating_type(insulating_type);
        }
        Insulating_type result = insulating_typeRepository.save(insulating_type);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("insulating_type", insulating_type.getId().toString()))
            .body(result);
    }

    /**
     * GET  /insulating-types : get all the insulating_types.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of insulating_types in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/insulating-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Insulating_type>> getAllInsulating_types(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Insulating_types");
        Page<Insulating_type> page = insulating_typeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/insulating-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /insulating-types/:id : get the "id" insulating_type.
     *
     * @param id the id of the insulating_type to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the insulating_type, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/insulating-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Insulating_type> getInsulating_type(@PathVariable Long id) {
        log.debug("REST request to get Insulating_type : {}", id);
        Insulating_type insulating_type = insulating_typeRepository.findOne(id);
        return Optional.ofNullable(insulating_type)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /insulating-types/:id : delete the "id" insulating_type.
     *
     * @param id the id of the insulating_type to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/insulating-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInsulating_type(@PathVariable Long id) {
        log.debug("REST request to delete Insulating_type : {}", id);
        insulating_typeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("insulating_type", id.toString())).build();
    }

}
