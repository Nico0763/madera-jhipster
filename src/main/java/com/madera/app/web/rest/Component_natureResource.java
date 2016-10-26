package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Component_nature;

import com.madera.app.repository.Component_natureRepository;
import com.madera.app.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Component_nature.
 */
@RestController
@RequestMapping("/api")
public class Component_natureResource {

    private final Logger log = LoggerFactory.getLogger(Component_natureResource.class);
        
    @Inject
    private Component_natureRepository component_natureRepository;

    /**
     * POST  /component-natures : Create a new component_nature.
     *
     * @param component_nature the component_nature to create
     * @return the ResponseEntity with status 201 (Created) and with body the new component_nature, or with status 400 (Bad Request) if the component_nature has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/component-natures",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Component_nature> createComponent_nature(@Valid @RequestBody Component_nature component_nature) throws URISyntaxException {
        log.debug("REST request to save Component_nature : {}", component_nature);
        if (component_nature.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("component_nature", "idexists", "A new component_nature cannot already have an ID")).body(null);
        }
        Component_nature result = component_natureRepository.save(component_nature);
        return ResponseEntity.created(new URI("/api/component-natures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("component_nature", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /component-natures : Updates an existing component_nature.
     *
     * @param component_nature the component_nature to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated component_nature,
     * or with status 400 (Bad Request) if the component_nature is not valid,
     * or with status 500 (Internal Server Error) if the component_nature couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/component-natures",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Component_nature> updateComponent_nature(@Valid @RequestBody Component_nature component_nature) throws URISyntaxException {
        log.debug("REST request to update Component_nature : {}", component_nature);
        if (component_nature.getId() == null) {
            return createComponent_nature(component_nature);
        }
        Component_nature result = component_natureRepository.save(component_nature);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("component_nature", component_nature.getId().toString()))
            .body(result);
    }

    /**
     * GET  /component-natures : get all the component_natures.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of component_natures in body
     */
    @RequestMapping(value = "/component-natures",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Component_nature> getAllComponent_natures() {
        log.debug("REST request to get all Component_natures");
        List<Component_nature> component_natures = component_natureRepository.findAll();
        return component_natures;
    }

    /**
     * GET  /component-natures/:id : get the "id" component_nature.
     *
     * @param id the id of the component_nature to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the component_nature, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/component-natures/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Component_nature> getComponent_nature(@PathVariable Long id) {
        log.debug("REST request to get Component_nature : {}", id);
        Component_nature component_nature = component_natureRepository.findOne(id);
        return Optional.ofNullable(component_nature)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /component-natures/:id : delete the "id" component_nature.
     *
     * @param id the id of the component_nature to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/component-natures/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteComponent_nature(@PathVariable Long id) {
        log.debug("REST request to delete Component_nature : {}", id);
        component_natureRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("component_nature", id.toString())).build();
    }

}
