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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
public class _PatternResource {

    private final Logger log = LoggerFactory.getLogger(_PatternResource.class);
        
    @Inject
    private PatternRepository patternRepository;


    @RequestMapping(value="/patterns/assortment/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Pattern> findByAssortmentId(@PathVariable Long id)
    {
        log.debug("REST request to get all patterns by assortment id");

        return patternRepository.findAllByAssortmentId(id);
    }


}
