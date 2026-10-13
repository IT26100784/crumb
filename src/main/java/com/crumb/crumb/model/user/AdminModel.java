package com.crumb.crumb.model.user;

/**
 * Concrete subclass representing a Bakery Staff / Administrator.
 * 
 * OOP Concepts Applied:
 * - Inheritance: Inherits core user attributes from UserModel.
 * - Polymorphism: Overrides getRoleTitle(), hasAdminAccess(), and toFileRecord().
 * - Encapsulation: Private specific attributes with getters and setters.
 */
public class AdminModel extends UserModel {

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
        // Delimited record format: ADMIN|id|name|email|password|||department
        return String.join("|",
                "ADMIN",
                id != null ? id : "",
                name != null ? name : "",
                email != null ? email : "",
                password != null ? password : "",
                "", // No customer phone
                "", // No customer address
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
