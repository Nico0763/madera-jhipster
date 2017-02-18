package com.madera.app.repository;

import com.madera.app.domain.Module;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Spring Data JPA repository for the Module entity.
 */
@SuppressWarnings("unused")
public interface ModuleRepository extends JpaRepository<Module,Long> {

	@Query("select m from Module m where m.name LIKE %:critere%")
    public Page<Module> searchModules(Pageable pageable, @Param("critere") String critere);

	@Query("select m from Module m, Module_nature n where m.module_nature = n AND n.id =:id")
    public List<Module> findByNature(@Param("id") Long id);
   

   	@Query("select m from Module m, Assortment a where m.assortment = a AND a.id =:id")
    public List<Module> findByAssortment(@Param("id") Long id);
}
