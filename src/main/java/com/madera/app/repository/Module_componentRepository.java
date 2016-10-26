package com.madera.app.repository;

import com.madera.app.domain.Module_component;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Module_component entity.
 */
@SuppressWarnings("unused")
public interface Module_componentRepository extends JpaRepository<Module_component,Long> {

}
