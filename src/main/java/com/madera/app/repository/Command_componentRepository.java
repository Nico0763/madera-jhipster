package com.madera.app.repository;

import com.madera.app.domain.Command_component;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Command_component entity.
 */
@SuppressWarnings("unused")
public interface Command_componentRepository extends JpaRepository<Command_component,Long> {

}
