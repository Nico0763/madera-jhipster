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
public class _CommandResource {

    private final Logger log = LoggerFactory.getLogger(_CommandResource.class);
        
    @Inject
    private CommandRepository commandRepository;

     /**
     * GET  /commands/:critere : get the "critere" to search.
     *
     * @param critere the critere of the search
     * @return the ResponseEntity with status 200 (OK) and with body the assortment, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/commands/search/{critere}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Command>> findCommand(Pageable pageable,@PathVariable String critere) throws URISyntaxException {
        log.debug("REST request to find Command");
         Page<Command> page = commandRepository.searchCommands(pageable, critere);
          HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commands/search/{critere}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value="/commands/state/{state}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Command> findByState(@PathVariable Long state)
    {
        log.debug("REST request to get all commands by state");

        return commandRepository.findAllByState(state);
    }

}
