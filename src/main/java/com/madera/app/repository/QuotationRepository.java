package com.madera.app.repository;

import com.madera.app.domain.Quotation;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Spring Data JPA repository for the Quotation entity.
 */
@SuppressWarnings("unused")
public interface QuotationRepository extends JpaRepository<Quotation,Long> {

	@Query("select q from Quotation q where q.name LIKE %:critere%")
    public Page<Quotation> searchQuotations(Pageable pageable, @Param("critere") String critere);

    List<Quotation> findAllByCustomerId(Long id);

    @Query("select sum(p.module.price) from Quotation q, Product p where p.quotation = q AND q.id = :id")
    public Long costQuotation(@Param("id") Long id);

}
