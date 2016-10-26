package com.madera.app.repository;

import com.madera.app.domain.Quotation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Quotation entity.
 */
@SuppressWarnings("unused")
public interface QuotationRepository extends JpaRepository<Quotation,Long> {

}
