package com.madera.app.repository;

import com.madera.app.domain.Unit_used;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Unit_used entity.
 */
@SuppressWarnings("unused")
public interface Unit_usedRepository extends JpaRepository<Unit_used,Long> {

}
