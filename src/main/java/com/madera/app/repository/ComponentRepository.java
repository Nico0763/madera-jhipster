package com.madera.app.repository;

import com.madera.app.domain.Component;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Component entity.
 */
@SuppressWarnings("unused")
public interface ComponentRepository extends JpaRepository<Component,Long> {

}
