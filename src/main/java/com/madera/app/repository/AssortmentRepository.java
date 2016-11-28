package com.madera.app.repository;

import com.madera.app.domain.Assortment;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Spring Data JPA repository for the Assortment entity.
 */
@SuppressWarnings("unused")
public interface AssortmentRepository extends JpaRepository<Assortment,Long> {

	 @Query("select a from Assortment a where a.name LIKE %:critere%")
    public Page<Assortment> searchAssortments(Pageable pageable, @Param("critere") String critere);

}
