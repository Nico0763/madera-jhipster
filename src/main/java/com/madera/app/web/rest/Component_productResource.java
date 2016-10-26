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
 * REST controller for managing Component_product.
 */
@RestController
@RequestMapping("/api")
public class Component_productResource {

    private final Logger log = LoggerFactory.getLogger(Component_productResource.class);
        
    @Inject
    private Component_productRepository component_productRepository;

    /**
     * POST  /component-products : Create a new component_product.
     *
     * @param component_product the component_product to create
     * @return the ResponseEntity with status 201 (Created) and with body the new component_product, or with status 400 (Bad Request) if the component_product has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/component-products",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Component_product> createComponent_product(@Valid @RequestBody Component_product component_product) throws URISyntaxException {
        log.debug("REST request to save Component_product : {}", component_product);
        if (component_product.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("component_product", "idexists", "A new component_product cannot already have an ID")).body(null);
        }
        Component_product result = component_productRepository.save(component_product);
        return ResponseEntity.created(new URI("/api/component-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("component_product", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /component-products : Updates an existing component_product.
     *
     * @param component_product the component_product to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated component_product,
     * or with status 400 (Bad Request) if the component_product is not valid,
     * or with status 500 (Internal Server Error) if the component_product couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/component-products",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Component_product> updateComponent_product(@Valid @RequestBody Component_product component_product) throws URISyntaxException {
        log.debug("REST request to update Component_product : {}", component_product);
        if (component_product.getId() == null) {
            return createComponent_product(component_product);
        }
        Component_product result = component_productRepository.save(component_product);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("component_product", component_product.getId().toString()))
            .body(result);
    }

    /**
     * GET  /component-products : get all the component_products.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of component_products in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/component-products",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Component_product>> getAllComponent_products(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Component_products");
        Page<Component_product> page = component_productRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/component-products");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /component-products/:id : get the "id" component_product.
     *
     * @param id the id of the component_product to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the component_product, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/component-products/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Component_product> getComponent_product(@PathVariable Long id) {
        log.debug("REST request to get Component_product : {}", id);
        Component_product component_product = component_productRepository.findOne(id);
        return Optional.ofNullable(component_product)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /component-products/:id : delete the "id" component_product.
     *
     * @param id the id of the component_product to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/component-products/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteComponent_product(@PathVariable Long id) {
        log.debug("REST request to delete Component_product : {}", id);
        component_productRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("component_product", id.toString())).build();
    }

}
