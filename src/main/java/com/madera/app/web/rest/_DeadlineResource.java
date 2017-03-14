package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Deadline;

import com.madera.app.repository.DeadlineRepository;
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
 * REST controller for managing Deadline.
 */
@RestController
@RequestMapping("/api")
public class _DeadlineResource {

    private final Logger log = LoggerFactory.getLogger(_DeadlineResource.class);
        
    @Inject
    private DeadlineRepository deadlineRepository;


    /**
     * GET  /deadline/sum/quotation/:id : get the percentage sum.
     *
     * @param id the id of quotation
     * @return the ResponseEntity with status 200 (OK) and with body the assortment, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/deadline/sum/quotation/{id}",
        method = RequestMethod.GET,
        produces = "application/json")
    @Timed
    public String sum(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to get all Answers");
        Float percent = deadlineRepository.sum(id);
          return "{\"value\":" + String.valueOf(percent) + "}";
    }

    @RequestMapping(value="/deadlines/quotation/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Deadline> findByQuotationId(@PathVariable Long id)
    {
        log.debug("REST request to get all deadlines by quotation id");

        return deadlineRepository.findAllByQuotationId(id);
    }


}
