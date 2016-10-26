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

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Component.
 */
@RestController
@RequestMapping("/api")
public class ComponentResource {

    private final Logger log = LoggerFactory.getLogger(ComponentResource.class);
        
    @Inject
    private ComponentRepository componentRepository;

    /**
     * POST  /components : Create a new component.
     *
     * @param component the component to create
     * @return the ResponseEntity with status 201 (Created) and with body the new component, or with status 400 (Bad Request) if the component has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/components",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Component> createComponent(@Valid @RequestBody Component component) throws URISyntaxException {
        log.debug("REST request to save Component : {}", component);
        if (component.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("component", "idexists", "A new component cannot already have an ID")).body(null);
        }
        Component result = componentRepository.save(component);
        return ResponseEntity.created(new URI("/api/components/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("component", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /components : Updates an existing component.
     *
     * @param component the component to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated component,
     * or with status 400 (Bad Request) if the component is not valid,
     * or with status 500 (Internal Server Error) if the component couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/components",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Component> updateComponent(@Valid @RequestBody Component component) throws URISyntaxException {
        log.debug("REST request to update Component : {}", component);
        if (component.getId() == null) {
            return createComponent(component);
        }
        Component result = componentRepository.save(component);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("component", component.getId().toString()))
            .body(result);
    }

    /**
     * GET  /components : get all the components.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of components in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/components",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Component>> getAllComponents(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Components");
        Page<Component> page = componentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/components");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /components/:id : get the "id" component.
     *
     * @param id the id of the component to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the component, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/components/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Component> getComponent(@PathVariable Long id) {
        log.debug("REST request to get Component : {}", id);
        Component component = componentRepository.findOne(id);
        return Optional.ofNullable(component)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /components/:id : delete the "id" component.
     *
     * @param id the id of the component to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/components/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteComponent(@PathVariable Long id) {
        log.debug("REST request to delete Component : {}", id);
        componentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("component", id.toString())).build();
    }

}
