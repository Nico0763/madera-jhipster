package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Module_nature;

import com.madera.app.repository.Module_natureRepository;
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
 * REST controller for managing Module_nature.
 */
@RestController
@RequestMapping("/api")
public class Module_natureResource {

    private final Logger log = LoggerFactory.getLogger(Module_natureResource.class);
        
    @Inject
    private Module_natureRepository module_natureRepository;

    /**
     * POST  /module-natures : Create a new module_nature.
     *
     * @param module_nature the module_nature to create
     * @return the ResponseEntity with status 201 (Created) and with body the new module_nature, or with status 400 (Bad Request) if the module_nature has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/module-natures",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Module_nature> createModule_nature(@Valid @RequestBody Module_nature module_nature) throws URISyntaxException {
        log.debug("REST request to save Module_nature : {}", module_nature);
        if (module_nature.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("module_nature", "idexists", "A new module_nature cannot already have an ID")).body(null);
        }
        Module_nature result = module_natureRepository.save(module_nature);
        return ResponseEntity.created(new URI("/api/module-natures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("module_nature", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /module-natures : Updates an existing module_nature.
     *
     * @param module_nature the module_nature to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated module_nature,
     * or with status 400 (Bad Request) if the module_nature is not valid,
     * or with status 500 (Internal Server Error) if the module_nature couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/module-natures",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Module_nature> updateModule_nature(@Valid @RequestBody Module_nature module_nature) throws URISyntaxException {
        log.debug("REST request to update Module_nature : {}", module_nature);
        if (module_nature.getId() == null) {
            return createModule_nature(module_nature);
        }
        Module_nature result = module_natureRepository.save(module_nature);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("module_nature", module_nature.getId().toString()))
            .body(result);
    }

    /**
     * GET  /module-natures : get all the module_natures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of module_natures in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/module-natures",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Module_nature>> getAllModule_natures(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Module_natures");
        Page<Module_nature> page = module_natureRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/module-natures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /module-natures/:id : get the "id" module_nature.
     *
     * @param id the id of the module_nature to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the module_nature, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/module-natures/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Module_nature> getModule_nature(@PathVariable Long id) {
        log.debug("REST request to get Module_nature : {}", id);
        Module_nature module_nature = module_natureRepository.findOne(id);
        return Optional.ofNullable(module_nature)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /module-natures/:id : delete the "id" module_nature.
     *
     * @param id the id of the module_nature to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/module-natures/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteModule_nature(@PathVariable Long id) {
        log.debug("REST request to delete Module_nature : {}", id);
        module_natureRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("module_nature", id.toString())).build();
    }

}
