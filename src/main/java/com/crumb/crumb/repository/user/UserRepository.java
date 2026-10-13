package com.crumb.crumb.repository.user;

import com.crumb.crumb.model.user.UserModel;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User persistence operations.
 * Demonstrates the Abstraction concept by decoupling business logic from storage details.
 */
public interface UserRepository {

    /**
     * Retrieve all users.
     */
    List<UserModel> findAll();

    /**
     * Find a user by their unique ID.
     */
    Optional<UserModel> findById(String id);

    /**
     * Find a user by their unique email address.
     */
    Optional<UserModel> findByEmail(String email);

    /**
     * Search users by name or email keyword.
     */
    List<UserModel> search(String keyword);

    /**
     * Save or update a user.
     */
    UserModel save(UserModel user);

    /**
     * Delete a user by their ID.
     * @return true if deleted, false if not found.
     */
    boolean deleteById(String id);

    /**
     * Generate the next unique identifier for a user type.
     */
    String generateNextId(String role);
}
