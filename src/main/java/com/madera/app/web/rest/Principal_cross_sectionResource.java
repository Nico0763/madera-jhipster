package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Principal_cross_section;

import com.madera.app.repository.Principal_cross_sectionRepository;
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
 * REST controller for managing Principal_cross_section.
 */
@RestController
@RequestMapping("/api")
public class Principal_cross_sectionResource {

    private final Logger log = LoggerFactory.getLogger(Principal_cross_sectionResource.class);
        
    @Inject
    private Principal_cross_sectionRepository principal_cross_sectionRepository;

    /**
     * POST  /principal-cross-sections : Create a new principal_cross_section.
     *
     * @param principal_cross_section the principal_cross_section to create
     * @return the ResponseEntity with status 201 (Created) and with body the new principal_cross_section, or with status 400 (Bad Request) if the principal_cross_section has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/principal-cross-sections",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Principal_cross_section> createPrincipal_cross_section(@Valid @RequestBody Principal_cross_section principal_cross_section) throws URISyntaxException {
        log.debug("REST request to save Principal_cross_section : {}", principal_cross_section);
        if (principal_cross_section.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("principal_cross_section", "idexists", "A new principal_cross_section cannot already have an ID")).body(null);
        }
        Principal_cross_section result = principal_cross_sectionRepository.save(principal_cross_section);
        return ResponseEntity.created(new URI("/api/principal-cross-sections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("principal_cross_section", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /principal-cross-sections : Updates an existing principal_cross_section.
     *
     * @param principal_cross_section the principal_cross_section to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated principal_cross_section,
     * or with status 400 (Bad Request) if the principal_cross_section is not valid,
     * or with status 500 (Internal Server Error) if the principal_cross_section couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/principal-cross-sections",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Principal_cross_section> updatePrincipal_cross_section(@Valid @RequestBody Principal_cross_section principal_cross_section) throws URISyntaxException {
        log.debug("REST request to update Principal_cross_section : {}", principal_cross_section);
        if (principal_cross_section.getId() == null) {
            return createPrincipal_cross_section(principal_cross_section);
        }
        Principal_cross_section result = principal_cross_sectionRepository.save(principal_cross_section);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("principal_cross_section", principal_cross_section.getId().toString()))
            .body(result);
    }

    /**
     * GET  /principal-cross-sections : get all the principal_cross_sections.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of principal_cross_sections in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/principal-cross-sections",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Principal_cross_section>> getAllPrincipal_cross_sections(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Principal_cross_sections");
        Page<Principal_cross_section> page = principal_cross_sectionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/principal-cross-sections");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /principal-cross-sections/:id : get the "id" principal_cross_section.
     *
     * @param id the id of the principal_cross_section to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the principal_cross_section, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/principal-cross-sections/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Principal_cross_section> getPrincipal_cross_section(@PathVariable Long id) {
        log.debug("REST request to get Principal_cross_section : {}", id);
        Principal_cross_section principal_cross_section = principal_cross_sectionRepository.findOne(id);
        return Optional.ofNullable(principal_cross_section)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /principal-cross-sections/:id : delete the "id" principal_cross_section.
     *
     * @param id the id of the principal_cross_section to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/principal-cross-sections/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePrincipal_cross_section(@PathVariable Long id) {
        log.debug("REST request to delete Principal_cross_section : {}", id);
        principal_cross_sectionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("principal_cross_section", id.toString())).build();
    }

}
