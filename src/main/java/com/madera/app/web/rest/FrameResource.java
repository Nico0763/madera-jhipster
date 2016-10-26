package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Frame;

import com.madera.app.repository.FrameRepository;
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
 * REST controller for managing Frame.
 */
@RestController
@RequestMapping("/api")
public class FrameResource {

    private final Logger log = LoggerFactory.getLogger(FrameResource.class);
        
    @Inject
    private FrameRepository frameRepository;

    /**
     * POST  /frames : Create a new frame.
     *
     * @param frame the frame to create
     * @return the ResponseEntity with status 201 (Created) and with body the new frame, or with status 400 (Bad Request) if the frame has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/frames",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Frame> createFrame(@Valid @RequestBody Frame frame) throws URISyntaxException {
        log.debug("REST request to save Frame : {}", frame);
        if (frame.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("frame", "idexists", "A new frame cannot already have an ID")).body(null);
        }
        Frame result = frameRepository.save(frame);
        return ResponseEntity.created(new URI("/api/frames/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("frame", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /frames : Updates an existing frame.
     *
     * @param frame the frame to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated frame,
     * or with status 400 (Bad Request) if the frame is not valid,
     * or with status 500 (Internal Server Error) if the frame couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/frames",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Frame> updateFrame(@Valid @RequestBody Frame frame) throws URISyntaxException {
        log.debug("REST request to update Frame : {}", frame);
        if (frame.getId() == null) {
            return createFrame(frame);
        }
        Frame result = frameRepository.save(frame);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("frame", frame.getId().toString()))
            .body(result);
    }

    /**
     * GET  /frames : get all the frames.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of frames in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/frames",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Frame>> getAllFrames(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Frames");
        Page<Frame> page = frameRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/frames");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /frames/:id : get the "id" frame.
     *
     * @param id the id of the frame to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the frame, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/frames/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Frame> getFrame(@PathVariable Long id) {
        log.debug("REST request to get Frame : {}", id);
        Frame frame = frameRepository.findOne(id);
        return Optional.ofNullable(frame)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /frames/:id : delete the "id" frame.
     *
     * @param id the id of the frame to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/frames/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFrame(@PathVariable Long id) {
        log.debug("REST request to delete Frame : {}", id);
        frameRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("frame", id.toString())).build();
    }

}
