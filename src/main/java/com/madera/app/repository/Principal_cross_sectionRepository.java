package com.madera.app.repository;

import com.madera.app.domain.Principal_cross_section;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Spring Data JPA repository for the Principal_cross_section entity.
 */
@SuppressWarnings("unused")
public interface Principal_cross_sectionRepository extends JpaRepository<Principal_cross_section,Long> {

	@Query("select pcs from Principal_cross_section pcs where pcs.name LIKE %:critere% OR  pcs.description LIKE %:critere%")
    public Page<Principal_cross_section> searchPrincipal_cross_sections(Pageable pageable, @Param("critere") String critere);

}
