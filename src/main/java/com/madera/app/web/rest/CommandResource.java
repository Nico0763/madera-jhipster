package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Command;

import com.madera.app.repository.CommandRepository;
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
 * REST controller for managing Command.
 */
@RestController
@RequestMapping("/api")
public class CommandResource {

    private final Logger log = LoggerFactory.getLogger(CommandResource.class);
        
    @Inject
    private CommandRepository commandRepository;

    /**
     * POST  /commands : Create a new command.
     *
     * @param command the command to create
     * @return the ResponseEntity with status 201 (Created) and with body the new command, or with status 400 (Bad Request) if the command has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/commands",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Command> createCommand(@Valid @RequestBody Command command) throws URISyntaxException {
        log.debug("REST request to save Command : {}", command);
        if (command.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("command", "idexists", "A new command cannot already have an ID")).body(null);
        }
        Command result = commandRepository.save(command);
        return ResponseEntity.created(new URI("/api/commands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("command", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commands : Updates an existing command.
     *
     * @param command the command to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated command,
     * or with status 400 (Bad Request) if the command is not valid,
     * or with status 500 (Internal Server Error) if the command couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/commands",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Command> updateCommand(@Valid @RequestBody Command command) throws URISyntaxException {
        log.debug("REST request to update Command : {}", command);
        if (command.getId() == null) {
            return createCommand(command);
        }
        Command result = commandRepository.save(command);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("command", command.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commands : get all the commands.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of commands in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/commands",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Command>> getAllCommands(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Commands");
        Page<Command> page = commandRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commands");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /commands/:id : get the "id" command.
     *
     * @param id the id of the command to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the command, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/commands/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Command> getCommand(@PathVariable Long id) {
        log.debug("REST request to get Command : {}", id);
        Command command = commandRepository.findOne(id);
        return Optional.ofNullable(command)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /commands/:id : delete the "id" command.
     *
     * @param id the id of the command to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/commands/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCommand(@PathVariable Long id) {
        log.debug("REST request to delete Command : {}", id);
        commandRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("command", id.toString())).build();
    }

}
