package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Cover_type;

import com.madera.app.repository.Cover_typeRepository;
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
 * REST controller for managing Cover_type.
 */
@RestController
@RequestMapping("/api")
public class Cover_typeResource {

    private final Logger log = LoggerFactory.getLogger(Cover_typeResource.class);
        
    @Inject
    private Cover_typeRepository cover_typeRepository;

    /**
     * POST  /cover-types : Create a new cover_type.
     *
     * @param cover_type the cover_type to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cover_type, or with status 400 (Bad Request) if the cover_type has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cover-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cover_type> createCover_type(@Valid @RequestBody Cover_type cover_type) throws URISyntaxException {
        log.debug("REST request to save Cover_type : {}", cover_type);
        if (cover_type.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cover_type", "idexists", "A new cover_type cannot already have an ID")).body(null);
        }
        Cover_type result = cover_typeRepository.save(cover_type);
        return ResponseEntity.created(new URI("/api/cover-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cover_type", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cover-types : Updates an existing cover_type.
     *
     * @param cover_type the cover_type to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cover_type,
     * or with status 400 (Bad Request) if the cover_type is not valid,
     * or with status 500 (Internal Server Error) if the cover_type couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cover-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cover_type> updateCover_type(@Valid @RequestBody Cover_type cover_type) throws URISyntaxException {
        log.debug("REST request to update Cover_type : {}", cover_type);
        if (cover_type.getId() == null) {
            return createCover_type(cover_type);
        }
        Cover_type result = cover_typeRepository.save(cover_type);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cover_type", cover_type.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cover-types : get all the cover_types.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cover_types in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/cover-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Cover_type>> getAllCover_types(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Cover_types");
        Page<Cover_type> page = cover_typeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cover-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cover-types/:id : get the "id" cover_type.
     *
     * @param id the id of the cover_type to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cover_type, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cover-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cover_type> getCover_type(@PathVariable Long id) {
        log.debug("REST request to get Cover_type : {}", id);
        Cover_type cover_type = cover_typeRepository.findOne(id);
        return Optional.ofNullable(cover_type)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cover-types/:id : delete the "id" cover_type.
     *
     * @param id the id of the cover_type to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cover-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCover_type(@PathVariable Long id) {
        log.debug("REST request to delete Cover_type : {}", id);
        cover_typeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cover_type", id.toString())).build();
    }

}
