package com.madera.app.repository;

import com.madera.app.domain.Provider;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Spring Data JPA repository for the Provider entity.
 */
@SuppressWarnings("unused")
public interface ProviderRepository extends JpaRepository<Provider,Long> {
	@Query("select p from Provider p where p.name LIKE %:critere%")
    public Page<Provider> searchProviders(Pageable pageable, @Param("critere") String critere);
}
