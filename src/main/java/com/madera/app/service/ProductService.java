package com.madera.app.service;

import com.madera.app.domain.Authority;
import com.madera.app.domain.Product;
import com.madera.app.domain.Component;
import com.madera.app.domain.Component_product;
import com.madera.app.domain.Module_component;
import com.madera.app.repository.AuthorityRepository;
import com.madera.app.repository.ProductRepository;
import com.madera.app.repository.ComponentRepository;
import com.madera.app.repository.Component_productRepository;
import com.madera.app.repository.Module_componentRepository;
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
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);
    @Inject
    private ProductRepository productRepository;

    @Inject
    private ComponentRepository componentRepository;
  

    @Inject
    private Component_productRepository component_productRepository;

     @Inject
    private Module_componentRepository module_componentRepository;


    public Product save(Product product) {


        Product result = productRepository.save(product);
        if(product.getModule() != null)
        {
            List<Module_component> module_components = module_componentRepository.findAllByModuleId(product.getModule().getId());
            for(int i=0; i<module_components.size();i++)
            {
                Module_component mc = module_components.get(i);
                for(int j=0;j<mc.getQuantity();j++)
                {
                    Component_product cp = new Component_product();
                    cp.setProduct(result);
                    cp.setComponent(mc.getComponent());
                    component_productRepository.save(cp);
                }
            }
        } 
         return result;
    }


    public void delete(Long id)
    {
        List<Component_product> components_product = component_productRepository.findAllByProductId(id);
        for(int i=0;i<components_product.size(); i++)
        {
            component_productRepository.delete(components_product.get(i).getId());
        }
        
        productRepository.delete(id);
    }

}
