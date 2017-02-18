package com.madera.app.repository;

import com.madera.app.domain.Component;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Spring Data JPA repository for the Component entity.
 */
@SuppressWarnings("unused")
public interface ComponentRepository extends JpaRepository<Component,Long> {
	@Query("select c from Component c where c.reference LIKE %:critere%")
    public Page<Component> searchComponents(Pageable pageable, @Param("critere") String critere);

    @Query("select c from Component c, Component_nature n where c.component_nature = n AND n.id =:id")
    public List<Component> findByNature(@Param("id") Long id);


}
