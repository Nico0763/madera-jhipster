package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Product;
import com.madera.app.domain.Component_product;

import com.madera.app.repository.ProductRepository;
import com.madera.app.service.ProductService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Assortment.
 */
@RestController
@RequestMapping("/api")
public class _ProductResource {

    private final Logger log = LoggerFactory.getLogger(_ProductResource.class);
        
    @Inject
    private ProductRepository productRepository;

      @Inject
    private ProductService productService;

    @Inject
    private Component_productRepository component_productRepository;

    @RequestMapping(value="/components/product/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Component_product> findComponentsByProductId(@PathVariable Long id)
    {
        log.debug("REST request to get all component_product by product id");

        return component_productRepository.findAllByProductId(id);
    }


     @RequestMapping(value="/products/pattern/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Product> findByPatternId(@PathVariable Long id)
    {
        log.debug("REST request to get all products by pattern id");

        return productRepository.findByPattern(id);
    }


    @RequestMapping(value="/products/quotation/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Product> findByQuotationId(@PathVariable Long id)
    {
        log.debug("REST request to get all products by quotation id");

        return productRepository.findAllByQuotationId(id);
    }




    @RequestMapping(value = "/products/dependencies",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) throws URISyntaxException 
    {

         log.debug("REST request to save Product : {}", product);
        if (product.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("product", "idexists", "A new product cannot already have an ID")).body(null);
        }
        Product result = productService.save(product);
        return ResponseEntity.created(new URI("/api/products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("product", result.getId().toString()))
            .body(result);

    }


    @RequestMapping(value = "/products/dependencies/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        productService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("product", id.toString())).build();
    }
}
