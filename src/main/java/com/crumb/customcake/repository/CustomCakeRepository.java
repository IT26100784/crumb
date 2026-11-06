package com.crumb.customcake.repository;

import com.crumb.customcake.entity.CustomCake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomCakeRepository extends JpaRepository<CustomCake, Long> {


    List<CustomCake> findByCustomerIdOrderByCreatedAtDesc(Long customerId);


    List<CustomCake> findByStatusOrderByCreatedAtDesc(String status);


    List<CustomCake> findAllByOrderByCreatedAtDesc();


    long countByStatus(String status);
}