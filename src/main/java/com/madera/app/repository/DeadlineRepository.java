package com.madera.app.repository;

import com.madera.app.domain.Deadline;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Deadline entity.
 */
@SuppressWarnings("unused")
public interface DeadlineRepository extends JpaRepository<Deadline,Long> {

}
