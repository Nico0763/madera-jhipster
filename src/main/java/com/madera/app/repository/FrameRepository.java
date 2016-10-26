package com.madera.app.repository;

import com.madera.app.domain.Frame;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Frame entity.
 */
@SuppressWarnings("unused")
public interface FrameRepository extends JpaRepository<Frame,Long> {

}
