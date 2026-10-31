package com.crumb.customer.repository;

import com.crumb.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Customer> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);

    long countByRole(String role);
}