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
public class _Module_componentResource {

    private final Logger log = LoggerFactory.getLogger(_Module_componentResource.class);
        
    @Inject
    private Module_componentRepository module_componentRepository;

    @RequestMapping(value="/module_components/module/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Module_component> findByModuleId(@PathVariable Long id)
    {
        log.debug("REST request to get all module_components by module id");

        return module_componentRepository.findAllByModuleId(id);
    }


     @RequestMapping(value="/module_components/component/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Module_component> findByComponentId(@PathVariable Long id)
    {
        log.debug("REST request to get all module_components by component id");

        return module_componentRepository.findAllByComponentId(id);
    }

}
