package com.madera.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.madera.app.domain.Module;
import com.madera.app.domain.Module_component;

import com.madera.app.repository.ModuleRepository;
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
public class _ModuleResource {

    private final Logger log = LoggerFactory.getLogger(_ModuleResource.class);
        
    @Inject
    private ModuleRepository moduleRepository;

    @Inject
    private Module_componentRepository module_componentRepository;

    /**
     * GET  /modules/:critere : get the "critere" to search.
     *
     * @param critere the critere of the search
     * @return the ResponseEntity with status 200 (OK) and with body the assortment, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/modules/search/{critere}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Module>> findModule(Pageable pageable,@PathVariable String critere) throws URISyntaxException {
        log.debug("REST request to find Module");
         Page<Module> page = moduleRepository.searchModules(pageable, critere);
          HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/modules/search/{critere}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value="/modules/nature/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Module> findByModuleNatureId(@PathVariable Long id)
    {
        log.debug("REST request to get all modules by nature id");

        return moduleRepository.findByNature(id);
    }





     /**
     * PUT  /modules/duplicate : Duplicate an existing module.
     *
     * @param _module the module to duplicate
     * @return the ResponseEntity with status 200 (OK) and with body the duplicate assortment,
     * or with status 400 (Bad Request) if the assortment is not valid,
     * or with status 500 (Internal Server Error) if the assortment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/modules/duplicate",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Module> duplicateModule(@Valid @RequestBody Module _module) throws URISyntaxException {
        log.debug("REST request to duplicate Module : {}", _module);
        if (_module.getId() == null) {
            return null;
        }
        Module module = moduleRepository.findOne(_module.getId());
    

        //Création du nouveau module
        Module m = new Module();
        m.setName(module.getName());
        m.setCctp(module.getCctp());
        m.setPrice(module.getPrice());
        m.setPrincipal_cross_section(module.getPrincipal_cross_section());
        m.setModule_nature(module.getModule_nature());
        m.setAssortment(module.getAssortment());
        m = moduleRepository.save(m);


        //Duplication des composants
        List<Module_component> module_components = module_componentRepository.findAllByModuleId(module.getId());
        for(Module_component module_component : module_components)
        {
            Module_component mc = new Module_component();
            mc.setQuantity(module_component.getQuantity());
            mc.setComponent(module_component.getComponent());
            mc.setModule(m);
            mc = module_componentRepository.save(mc);
        }




        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("m", m.getId().toString()))
            .body(m);
    }


}
