package com.madera.app.service;

import com.madera.app.domain.Authority;
import com.madera.app.domain.Product;
import com.madera.app.domain.Component;
import com.madera.app.domain.Component_product;
import com.madera.app.domain.Module_component;
import com.madera.app.domain.Quotation;
import com.madera.app.domain.Command;
import com.madera.app.domain.Command_component;
import com.madera.app.domain.Provider;
import com.madera.app.domain.Quotation;
import com.madera.app.domain.Customer;
import com.madera.app.repository.AuthorityRepository;
import com.madera.app.repository.ProductRepository;
import com.madera.app.repository.ComponentRepository;
import com.madera.app.repository.Component_productRepository;
import com.madera.app.repository.Command_componentRepository;
import com.madera.app.repository.CommandRepository;
import com.madera.app.repository.QuotationRepository;
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
import java.time.*;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class CommandService {

    private final Logger log = LoggerFactory.getLogger(CommandService.class);
    @Inject
    private CommandRepository commandRepository;

    @Inject
    private ComponentRepository componentRepository;
  

    @Inject
    private Command_componentRepository command_componentRepository;

     @Inject
    private QuotationRepository quotationRepository;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private Component_productRepository component_productRepository;


    public void pushCommand(Quotation quotation) {

        Customer customer = quotation.getCustomer();
        if(customer != null)
        {
                List<Component_product> cps = component_productRepository.findByQuotationGroupByProvider(quotation.getId());

                Long provider = null;
                Command command = null;
                int quantity = 0;
                Component component = null;
                int nb = 0;

                for(int i =0;i<cps.size();i++)
                    {
                        Component_product cp = cps.get(i);

                        Provider p_provider = cp.getComponent().getProvider();
                        if(p_provider!=null)
                        {
                            if(provider != p_provider.getId())
                            {
                                nb ++;
                                command = new Command();
                                Date date = new Date();
                                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                int year  = localDate.getYear();
                                int month = localDate.getMonthValue();
                                String monthStr =Integer.toString(month);
                                if(month < 10)
                                    monthStr = "0" + month;

                                String reference = year + "-" + monthStr + "-";
                                reference += customer.getName().charAt(0) + "-";
                                reference += customer.getFirstname().charAt(0) + "-";
                                reference += nb;
                                log.debug(reference);

                                command.setReference(reference);
                                command.setState(1);
                                command.setProvider(p_provider);
                                command = commandRepository.save(command);
                                provider = p_provider.getId();
                            }
                           



                            if(cp.getComponent() != component && component != null)
                            {
                                Command_component cc = new Command_component();
                                cc.setComponent(component);
                                cc.setCommand(command);
                                cc.setQuantity(quantity);
                                command_componentRepository.save(cc);
                                quantity = 0;
                                
                            }
                            component = cp.getComponent();       
                            quantity ++;
                        }
                    }


                    if(component != null)
                            {
                                Command_component cc = new Command_component();
                                cc.setComponent(component);
                                cc.setCommand(command);
                                cc.setQuantity(quantity);
                                command_componentRepository.save(cc);
                            }
                }
    }

}
