package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Command_component;

import com.madera.app.repository.Command_componentRepository;
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
 * REST controller for managing Command_component.
 */
@RestController
@RequestMapping("/api")
public class Command_componentResource {

    private final Logger log = LoggerFactory.getLogger(Command_componentResource.class);
        
    @Inject
    private Command_componentRepository command_componentRepository;

    /**
     * POST  /command-components : Create a new command_component.
     *
     * @param command_component the command_component to create
     * @return the ResponseEntity with status 201 (Created) and with body the new command_component, or with status 400 (Bad Request) if the command_component has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/command-components",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Command_component> createCommand_component(@Valid @RequestBody Command_component command_component) throws URISyntaxException {
        log.debug("REST request to save Command_component : {}", command_component);
        if (command_component.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("command_component", "idexists", "A new command_component cannot already have an ID")).body(null);
        }
        Command_component result = command_componentRepository.save(command_component);
        return ResponseEntity.created(new URI("/api/command-components/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("command_component", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /command-components : Updates an existing command_component.
     *
     * @param command_component the command_component to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated command_component,
     * or with status 400 (Bad Request) if the command_component is not valid,
     * or with status 500 (Internal Server Error) if the command_component couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/command-components",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Command_component> updateCommand_component(@Valid @RequestBody Command_component command_component) throws URISyntaxException {
        log.debug("REST request to update Command_component : {}", command_component);
        if (command_component.getId() == null) {
            return createCommand_component(command_component);
        }
        Command_component result = command_componentRepository.save(command_component);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("command_component", command_component.getId().toString()))
            .body(result);
    }

    /**
     * GET  /command-components : get all the command_components.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of command_components in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/command-components",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Command_component>> getAllCommand_components(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Command_components");
        Page<Command_component> page = command_componentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/command-components");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /command-components/:id : get the "id" command_component.
     *
     * @param id the id of the command_component to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the command_component, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/command-components/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Command_component> getCommand_component(@PathVariable Long id) {
        log.debug("REST request to get Command_component : {}", id);
        Command_component command_component = command_componentRepository.findOne(id);
        return Optional.ofNullable(command_component)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /command-components/:id : delete the "id" command_component.
     *
     * @param id the id of the command_component to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/command-components/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCommand_component(@PathVariable Long id) {
        log.debug("REST request to delete Command_component : {}", id);
        command_componentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("command_component", id.toString())).build();
    }

}
