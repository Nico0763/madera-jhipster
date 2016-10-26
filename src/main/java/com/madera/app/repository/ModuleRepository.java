package com.madera.app.repository;

import com.madera.app.domain.Module;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Module entity.
 */
@SuppressWarnings("unused")
public interface ModuleRepository extends JpaRepository<Module,Long> {

}
