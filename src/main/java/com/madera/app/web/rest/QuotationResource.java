package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Quotation;

import com.madera.app.repository.QuotationRepository;
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
 * REST controller for managing Quotation.
 */
@RestController
@RequestMapping("/api")
public class QuotationResource {

    private final Logger log = LoggerFactory.getLogger(QuotationResource.class);
        
    @Inject
    private QuotationRepository quotationRepository;

    /**
     * POST  /quotations : Create a new quotation.
     *
     * @param quotation the quotation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new quotation, or with status 400 (Bad Request) if the quotation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/quotations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Quotation> createQuotation(@Valid @RequestBody Quotation quotation) throws URISyntaxException {
        log.debug("REST request to save Quotation : {}", quotation);
        if (quotation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("quotation", "idexists", "A new quotation cannot already have an ID")).body(null);
        }
        Quotation result = quotationRepository.save(quotation);
        return ResponseEntity.created(new URI("/api/quotations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("quotation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /quotations : Updates an existing quotation.
     *
     * @param quotation the quotation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated quotation,
     * or with status 400 (Bad Request) if the quotation is not valid,
     * or with status 500 (Internal Server Error) if the quotation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/quotations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Quotation> updateQuotation(@Valid @RequestBody Quotation quotation) throws URISyntaxException {
        log.debug("REST request to update Quotation : {}", quotation);
        if (quotation.getId() == null) {
            return createQuotation(quotation);
        }
        Quotation result = quotationRepository.save(quotation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("quotation", quotation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /quotations : get all the quotations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of quotations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/quotations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Quotation>> getAllQuotations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Quotations");
        Page<Quotation> page = quotationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/quotations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /quotations/:id : get the "id" quotation.
     *
     * @param id the id of the quotation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the quotation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/quotations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Quotation> getQuotation(@PathVariable Long id) {
        log.debug("REST request to get Quotation : {}", id);
        Quotation quotation = quotationRepository.findOne(id);
        return Optional.ofNullable(quotation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /quotations/:id : delete the "id" quotation.
     *
     * @param id the id of the quotation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/quotations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteQuotation(@PathVariable Long id) {
        log.debug("REST request to delete Quotation : {}", id);
        quotationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("quotation", id.toString())).build();
    }

}
