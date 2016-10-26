package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Module_component;

import com.madera.app.repository.Module_componentRepository;
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
 * REST controller for managing Module_component.
 */
@RestController
@RequestMapping("/api")
public class Module_componentResource {

    private final Logger log = LoggerFactory.getLogger(Module_componentResource.class);
        
    @Inject
    private Module_componentRepository module_componentRepository;

    /**
     * POST  /module-components : Create a new module_component.
     *
     * @param module_component the module_component to create
     * @return the ResponseEntity with status 201 (Created) and with body the new module_component, or with status 400 (Bad Request) if the module_component has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/module-components",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Module_component> createModule_component(@Valid @RequestBody Module_component module_component) throws URISyntaxException {
        log.debug("REST request to save Module_component : {}", module_component);
        if (module_component.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("module_component", "idexists", "A new module_component cannot already have an ID")).body(null);
        }
        Module_component result = module_componentRepository.save(module_component);
        return ResponseEntity.created(new URI("/api/module-components/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("module_component", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /module-components : Updates an existing module_component.
     *
     * @param module_component the module_component to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated module_component,
     * or with status 400 (Bad Request) if the module_component is not valid,
     * or with status 500 (Internal Server Error) if the module_component couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/module-components",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Module_component> updateModule_component(@Valid @RequestBody Module_component module_component) throws URISyntaxException {
        log.debug("REST request to update Module_component : {}", module_component);
        if (module_component.getId() == null) {
            return createModule_component(module_component);
        }
        Module_component result = module_componentRepository.save(module_component);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("module_component", module_component.getId().toString()))
            .body(result);
    }

    /**
     * GET  /module-components : get all the module_components.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of module_components in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/module-components",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Module_component>> getAllModule_components(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Module_components");
        Page<Module_component> page = module_componentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/module-components");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /module-components/:id : get the "id" module_component.
     *
     * @param id the id of the module_component to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the module_component, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/module-components/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Module_component> getModule_component(@PathVariable Long id) {
        log.debug("REST request to get Module_component : {}", id);
        Module_component module_component = module_componentRepository.findOne(id);
        return Optional.ofNullable(module_component)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /module-components/:id : delete the "id" module_component.
     *
     * @param id the id of the module_component to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/module-components/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteModule_component(@PathVariable Long id) {
        log.debug("REST request to delete Module_component : {}", id);
        module_componentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("module_component", id.toString())).build();
    }

}
