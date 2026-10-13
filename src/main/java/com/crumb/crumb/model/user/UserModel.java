package com.crumb.crumb.model.user;

import jakarta.persistence.*;

/**
 * Base abstract JPA Entity representing a User in the Crumb Bakery platform.
 * Persisted in MySQL database table 'users'.
 * 
 * OOP Concepts Applied:
 * - Encapsulation: Protected attributes with getters, setters, and internal validation.
 * - Inheritance: Uses JPA SINGLE_TABLE inheritance for CustomerModel and AdminModel subclasses.
 * - Abstraction: Defines contract and polymorphic methods for all user types.
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role_type", discriminatorType = DiscriminatorType.STRING)
public abstract class UserModel {

    @Id
    @Column(name = "id", length = 50)
    protected String id;

    @Column(name = "name", nullable = false, length = 100)
    protected String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    protected String email;

    @Column(name = "password", nullable = false, length = 255)
    protected String password;

    @Column(name = "role", nullable = false, length = 20)
    protected String role;

    public UserModel() {
    }

    public UserModel(String id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // --- Abstract Methods (Demonstrating Polymorphism) ---

    public abstract String getRoleTitle();

    public abstract boolean hasAdminAccess();

    public abstract String toFileRecord();

    // --- Getters and Setters (Encapsulation) ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name.trim();
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null) {
            this.email = email.trim().toLowerCase();
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
