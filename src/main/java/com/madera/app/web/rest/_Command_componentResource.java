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
public class _Command_componentResource {

    private final Logger log = LoggerFactory.getLogger(_Command_componentResource.class);
        
    @Inject
    private Command_componentRepository command_componentRepository;


    @RequestMapping(value="/command_components/command/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Command_component> findByCommandId(@PathVariable Long id)
    {
        log.debug("REST request to get all command_components by command id");

        return command_componentRepository.findAllByCommandId(id);
    }

    @RequestMapping(value="/command_components/component/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Command_component> findByComponentId(@PathVariable Long id)
    {
        log.debug("REST request to get all command_components by component id");

        return command_componentRepository.findAllByComponentId(id);
    }

}
