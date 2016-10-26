package com.madera.app.repository;

import com.madera.app.domain.Assortment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Assortment entity.
 */
@SuppressWarnings("unused")
public interface AssortmentRepository extends JpaRepository<Assortment,Long> {

}
