package com.madera.app.repository;

import com.madera.app.domain.Insulating_type;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Insulating_type entity.
 */
@SuppressWarnings("unused")
public interface Insulating_typeRepository extends JpaRepository<Insulating_type,Long> {

}
