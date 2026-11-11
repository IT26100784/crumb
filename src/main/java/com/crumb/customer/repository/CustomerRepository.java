
package com.crumb.customer.repository;

import com.crumb.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Customer> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String name, String email
    );

    long countByRole(String role);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
        UPDATE customers
        SET customer_type = 'PREMIUM',
            loyalty_points = COALESCE(loyalty_points, 0) + 100,
            membership_expiry = :expiry,
            vip_concierge_note = 'Upgraded to VIP Tier'
        WHERE id = :id
          AND customer_type = 'REGULAR'
          AND role <> 'ADMIN'
        """, nativeQuery = true)
    int upgradeCustomerToPremium(
            @Param("id") Long id,
            @Param("expiry") LocalDate expiry
    );

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
        UPDATE customers
        SET customer_type = 'REGULAR',
            membership_expiry = NULL,
            vip_concierge_note = NULL
        WHERE id = :id
          AND customer_type = 'PREMIUM'
          AND role <> 'ADMIN'
        """, nativeQuery = true)
    int downgradeCustomerToRegular(@Param("id") Long id);
}
