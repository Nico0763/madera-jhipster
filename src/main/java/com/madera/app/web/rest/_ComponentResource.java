package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Component;

import com.madera.app.repository.ComponentRepository;
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
public class _ComponentResource {

    private final Logger log = LoggerFactory.getLogger(_ComponentResource.class);
        
    @Inject
    private ComponentRepository componentRepository;

    /**
     * GET  /components/:critere : get the "critere" to search.
     *
     * @param critere the critere of the search
     * @return the ResponseEntity with status 200 (OK) and with body the assortment, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/components/search/{critere}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Component>> findComponent(Pageable pageable,@PathVariable String critere) throws URISyntaxException {
        log.debug("REST request to find Component");
         Page<Component> page = componentRepository.searchComponents(pageable, critere);
          HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/components/search/{critere}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

   

    @RequestMapping(value="/components/nature/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Component> findByComponentNatureId(@PathVariable Long id)
    {
        log.debug("REST request to get all components by nature id");

        return componentRepository.findByNature(id);
    }


}
