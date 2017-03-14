package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Component_product;

import com.madera.app.repository.Component_productRepository;
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
public class _Component_productResource {

    private final Logger log = LoggerFactory.getLogger(_Component_productResource.class);
        
    @Inject
    private Component_productRepository component_productRepository;


    @RequestMapping(value="/component_product/product/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Component_product> findByProduct(@PathVariable Long id)
    {
        log.debug("REST request to get all component_products by product id");
        return component_productRepository.findAllByProductId(id);
    }

}
