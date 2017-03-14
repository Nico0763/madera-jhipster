package com.madera.app.repository;

import com.madera.app.domain.Deadline;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Deadline entity.
 */
@SuppressWarnings("unused")
public interface DeadlineRepository extends JpaRepository<Deadline,Long> {
	 @Query("select SUM(d.percentage) from Deadline d, Quotation q where d.quotation = q AND q.id = :id")
    public Float sum(@Param("id") Long id);
    
    List<Deadline> findAllByQuotationId(Long id);
}
