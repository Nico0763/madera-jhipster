package com.madera.app.web.rest;


import com.madera.app.complex.*;
import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Quotation;
import com.madera.app.domain.Pattern;

import com.madera.app.repository.QuotationRepository;

import com.madera.app.service.QuotationService;
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
public class _QuotationResource {

    private final Logger log = LoggerFactory.getLogger(_QuotationResource.class);
        
    @Inject
    private QuotationRepository quotationRepository;

    @Inject
    private QuotationService quotationService;

    /**
     * GET  /quotations/:critere : get the "critere" to search.
     *
     * @param critere the critere of the search
     * @return the ResponseEntity with status 200 (OK) and with body the assortment, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/quotations/search/{critere}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Quotation>> findQuotation(Pageable pageable,@PathVariable String critere) throws URISyntaxException {
        log.debug("REST request to find Quotation");
         Page<Quotation> page = quotationRepository.searchQuotations(pageable, critere);
          HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/quotations/search/{critere}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value="/quotations/customer/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Quotation> findByCustomerId(@PathVariable Long id)
    {
        log.debug("REST request to get all quotations by customer id");

        return quotationRepository.findAllByCustomerId(id);
    }

    /**
     * POST  /quotations : Create a new quotation base on pattern.
     *
     * @param quotation the quotation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new quotation, or with status 400 (Bad Request) if the quotation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/quotations/pattern",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Quotation> createQuotation(@RequestBody QuotationPattern quotationPattern) throws URISyntaxException {
        
        Quotation quotation = quotationPattern.quotation;
        Long id = quotationPattern.id;
        log.debug("REST request to save Quotation : {}", quotation);
        if (quotation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("quotation", "idexists", "A new quotation cannot already have an ID")).body(null);
        }
        Quotation result = quotationService.save(quotation, id);
        return ResponseEntity.created(new URI("/api/quotations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("quotation", result.getId().toString()))
            .body(result);
    }

    @RequestMapping(value="/quotation/total/{id}",
        method = RequestMethod.GET,
        produces = "application/json")
    @Timed
    public @ResponseBody String quotationCost(@PathVariable Long id) throws URISyntaxException
    {
        log.debug("REST request to get the cost of quotation");
        Long total =  quotationService.getQuotationCost(id);
        return "{\"value\":" + String.valueOf(total) + "}";
        /*return ResponseEntity.created(new URI("/api/quotation/total/" + id))
            .body(total);*/
    }
}
