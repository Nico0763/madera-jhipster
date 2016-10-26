package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Pattern;

import com.madera.app.repository.PatternRepository;
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
 * REST controller for managing Pattern.
 */
@RestController
@RequestMapping("/api")
public class PatternResource {

    private final Logger log = LoggerFactory.getLogger(PatternResource.class);
        
    @Inject
    private PatternRepository patternRepository;

    /**
     * POST  /patterns : Create a new pattern.
     *
     * @param pattern the pattern to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pattern, or with status 400 (Bad Request) if the pattern has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/patterns",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pattern> createPattern(@Valid @RequestBody Pattern pattern) throws URISyntaxException {
        log.debug("REST request to save Pattern : {}", pattern);
        if (pattern.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pattern", "idexists", "A new pattern cannot already have an ID")).body(null);
        }
        Pattern result = patternRepository.save(pattern);
        return ResponseEntity.created(new URI("/api/patterns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pattern", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /patterns : Updates an existing pattern.
     *
     * @param pattern the pattern to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pattern,
     * or with status 400 (Bad Request) if the pattern is not valid,
     * or with status 500 (Internal Server Error) if the pattern couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/patterns",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pattern> updatePattern(@Valid @RequestBody Pattern pattern) throws URISyntaxException {
        log.debug("REST request to update Pattern : {}", pattern);
        if (pattern.getId() == null) {
            return createPattern(pattern);
        }
        Pattern result = patternRepository.save(pattern);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pattern", pattern.getId().toString()))
            .body(result);
    }

    /**
     * GET  /patterns : get all the patterns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of patterns in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/patterns",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Pattern>> getAllPatterns(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Patterns");
        Page<Pattern> page = patternRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/patterns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /patterns/:id : get the "id" pattern.
     *
     * @param id the id of the pattern to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pattern, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/patterns/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pattern> getPattern(@PathVariable Long id) {
        log.debug("REST request to get Pattern : {}", id);
        Pattern pattern = patternRepository.findOne(id);
        return Optional.ofNullable(pattern)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /patterns/:id : delete the "id" pattern.
     *
     * @param id the id of the pattern to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/patterns/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePattern(@PathVariable Long id) {
        log.debug("REST request to delete Pattern : {}", id);
        patternRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pattern", id.toString())).build();
    }

}
