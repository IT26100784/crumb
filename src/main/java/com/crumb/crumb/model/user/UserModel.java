package com.crumb.crumb.model.user;

/**
 * Base abstract class representing a User in the Crumb Bakery platform.
 * 
 * OOP Concepts Applied:
 * - Encapsulation: Protected attributes with getters and setters, and internal validation.
 * - Abstraction: Abstract methods (getRoleTitle, hasAdminAccess, toFileRecord) that define
 *   the contract for all derived user types.
 */
public abstract class UserModel {

    protected String id;
    protected String name;
    protected String email;
    protected String password;
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

    /**
     * Polymorphic method to return a descriptive title of the user's role and privileges.
     */
    public abstract String getRoleTitle();

    /**
     * Polymorphic check for administrative capabilities.
     */
    public abstract boolean hasAdminAccess();

    /**
     * Polymorphic method to serialize this user entity into a file-storage text format.
     */
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
