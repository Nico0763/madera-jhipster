package com.madera.app.repository;

import com.madera.app.domain.Command;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Command entity.
 */
@SuppressWarnings("unused")
public interface CommandRepository extends JpaRepository<Command,Long> {

}
