package com.madera.app.service;

import com.madera.app.domain.Authority;
import com.madera.app.domain.Product;
import com.madera.app.domain.Component;
import com.madera.app.domain.Component_product;
import com.madera.app.domain.Module_component;
import com.madera.app.domain.Pattern;
import com.madera.app.domain.Product;
import com.madera.app.domain.Quotation;
import com.madera.app.repository.AuthorityRepository;
import com.madera.app.repository.ProductRepository;
import com.madera.app.repository.ComponentRepository;
import com.madera.app.repository.Component_productRepository;
import com.madera.app.repository.Module_componentRepository;
import com.madera.app.repository.QuotationRepository;
import com.madera.app.security.AuthoritiesConstants;
import com.madera.app.security.SecurityUtils;
import com.madera.app.service.util.RandomUtil;
import com.madera.app.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import javax.inject.Inject;
import java.util.*;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class QuotationService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);
    @Inject
    private QuotationRepository quotationRepository;

    @Inject
    private ProductRepository productRepository;
  

    @Inject
    private Component_productRepository component_productRepository;

     @Inject
    private Module_componentRepository module_componentRepository;

    @Transactional
    public Quotation save(Quotation quotation, Long id) {


        Quotation q = quotationRepository.save(quotation);
            List<Product> products = productRepository.findByPattern(id);
            for(int i=0;i<products.size();i++)
            {
                Product p = products.get(i);
                Product _p = new Product();
                _p.setName(p.getName());
                _p.setModule(p.getModule());
                _p.setQuotation(q);

                _p = productRepository.save(_p);

                List<Component_product> cps = component_productRepository.findAllByProductId(p.getId());
                for(int j=0;j<products.size();j++)
                {

                    Component_product cp = cps.get(j);
                    Component_product _cp = new Component_product();
                    _cp.setAngle(cp.getAngle());
                    _cp.setLength(cp.getLength());
                    _cp.setComponent(cp.getComponent());
                    _cp.setProduct(_p);

                    _cp = component_productRepository.save(_cp);
                }
            }
        

        return q;
    }


   

}
