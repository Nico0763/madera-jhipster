package com.madera.app.repository;

import com.madera.app.domain.Command;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Spring Data JPA repository for the Command entity.
 */
@SuppressWarnings("unused")
public interface CommandRepository extends JpaRepository<Command,Long> {


	@Query("select c from Command c where c.reference LIKE %:critere%")
    public Page<Command> searchCommands(Pageable pageable, @Param("critere") String critere);

    List<Command> findAllByState(Long state);

}
