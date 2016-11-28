package com.madera.app.repository;

import com.madera.app.domain.Customer;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

/**
 * Spring Data JPA repository for the Customer entity.
 */
@SuppressWarnings("unused")
public interface CustomerRepository extends JpaRepository<Customer,Long> {
	@Query("select c from Customer c where c.name LIKE %:critere% OR c.firstname LIKE %:critere%")
    public Page<Customer> searchCustomers(Pageable pageable, @Param("critere") String critere);
}
