package com.crumb.crumb.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Concrete JPA Entity representing an Administrator, mapped in MySQL table 'users'.
 * 
 * OOP Concepts Applied:
 * - Inheritance: Subclasses UserModel.
 * - Polymorphism: Overrides getRoleTitle(), hasAdminAccess(), and toFileRecord().
 * - Encapsulation: Private admin-specific fields with getters and setters.
 */
@Entity
@DiscriminatorValue("ADMIN")
public class AdminModel extends UserModel {

    @Column(name = "department", length = 100)
    private String department;

    public AdminModel() {
        super();
        this.role = "ADMIN";
        this.department = "Management";
    }

    public AdminModel(String id, String name, String email, String password, String department) {
        super(id, name, email, password, "ADMIN");
        this.department = department != null ? department : "Management";
    }

    // --- Polymorphic Method Overrides ---

    @Override
    public String getRoleTitle() {
        return "Crumb Bakery Administrator (" + this.department + ")";
    }

    @Override
    public boolean hasAdminAccess() {
        return true;
    }

    @Override
    public String toFileRecord() {
        return String.join("|",
                "ADMIN",
                id != null ? id : "",
                name != null ? name : "",
                email != null ? email : "",
                password != null ? password : "",
                "",
                "",
                department != null ? department : "Management"
        );
    }

    // --- Getters & Setters ---

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department != null ? department.trim() : "Management";
    }
}
