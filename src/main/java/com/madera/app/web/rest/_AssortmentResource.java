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
public class _AssortmentResource {

    private final Logger log = LoggerFactory.getLogger(_AssortmentResource.class);
        
    @Inject
    private AssortmentRepository assortmentRepository;

   /**
     * PUT  /assortments/duplicate : Duplicate an existing assortment.
     *
     * @param _assortment the assortment to duplicate
     * @return the ResponseEntity with status 200 (OK) and with body the duplicate assortment,
     * or with status 400 (Bad Request) if the assortment is not valid,
     * or with status 500 (Internal Server Error) if the assortment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/assortments/duplicate",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Assortment> duplicateAssortment(@Valid @RequestBody Assortment _assortment) throws URISyntaxException {
        log.debug("REST request to duplicate Assortment : {}", _assortment);
        if (_assortment.getId() == null) {
            return null;
        }
        Assortment assortment = assortmentRepository.findOne(_assortment.getId());
    

        //Création du nouveau tour
        Assortment a = new Assortment();
        a.setName(assortment.getName());
        a.setSkeleton_conception_mode(assortment.getSkeleton_conception_mode());
        a.setFinition_ext(assortment.getFinition_ext());
        a.setInsulating_type(assortment.getInsulating_type());
        a.setFrame(assortment.getFrame());
        a.setCover_type(assortment.getCover_type());

        a = assortmentRepository.save(a);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("a", a.getId().toString()))
            .body(a);
    }


    /**
     * GET  /assortments/:critere : get the "critere" to search.
     *
     * @param critere the critere of the search
     * @return the ResponseEntity with status 200 (OK) and with body the assortment, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/assortments/search/{critere}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Assortment>> findAssortment(Pageable pageable,@PathVariable String critere) throws URISyntaxException {
        log.debug("REST request to get all Answers");
         Page<Assortment> page = assortmentRepository.searchAssortments(pageable, critere);
          HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/assortments/search/{critere}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }



}
