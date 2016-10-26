package com.madera.app.repository;

import com.madera.app.domain.Cover_type;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cover_type entity.
 */
@SuppressWarnings("unused")
public interface Cover_typeRepository extends JpaRepository<Cover_type,Long> {

}
