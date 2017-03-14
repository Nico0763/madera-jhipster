package com.madera.app.repository;

import com.madera.app.domain.Component_product;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Component_product entity.
 */
@SuppressWarnings("unused")
public interface Component_productRepository extends JpaRepository<Component_product,Long> {
	List<Component_product> findAllByProductId(Long id);

	 @Query("select cp from Component c, Product p, Component_product cp WHERE cp.product = p AND cp.component = c AND p.quotation.id = :id ORDER BY c.provider.id, c.id")
    public List<Component_product> findByQuotationGroupByProvider(@Param("id") Long id);
}
