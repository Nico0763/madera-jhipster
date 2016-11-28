package com.madera.app.repository;

import com.madera.app.domain.Pattern;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Pattern entity.
 */
@SuppressWarnings("unused")
public interface PatternRepository extends JpaRepository<Pattern,Long> {

	    List<Pattern> findAllByAssortmentId(Long id);
}
