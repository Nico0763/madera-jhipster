package com.madera.app.repository;

import com.madera.app.domain.Component_product;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Component_product entity.
 */
@SuppressWarnings("unused")
public interface Component_productRepository extends JpaRepository<Component_product,Long> {
	List<Component_product> findAllByProductId(Long id);
}
