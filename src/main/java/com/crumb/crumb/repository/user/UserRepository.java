package com.crumb.crumb.repository.user;

import com.crumb.crumb.model.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for MySQL database operations on UserModel entity.
 * Provides standard CRUD and custom SQL/JPQL queries.
 */
@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {

    /**
     * Find a user in MySQL by their unique email.
     */
    Optional<UserModel> findByEmail(String email);

    /**
     * Search users in MySQL across name, email, or ID.
     */
    @Query("SELECT u FROM UserModel u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :kw, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :kw, '%')) OR LOWER(u.id) LIKE LOWER(CONCAT('%', :kw, '%'))")
    List<UserModel> search(@Param("kw") String keyword);
}
