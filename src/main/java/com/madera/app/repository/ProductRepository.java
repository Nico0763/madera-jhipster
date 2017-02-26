package com.madera.app.repository;

import com.madera.app.domain.Product;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Product entity.
 */
@SuppressWarnings("unused")
public interface ProductRepository extends JpaRepository<Product,Long> {
	@Query("select p from Product p, Pattern pa where p.pattern = pa AND pa.id =:id")
    public List<Product> findByPattern(@Param("id") Long id);

    public List<Product> findAllByQuotationId(Long id);
}
