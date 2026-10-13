package com.crumb.crumb.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Concrete JPA Entity representing a Customer, mapped in MySQL table 'users'.
 * 
 * OOP Concepts Applied:
 * - Inheritance: Subclasses UserModel.
 * - Polymorphism: Overrides getRoleTitle(), hasAdminAccess(), and toFileRecord().
 * - Encapsulation: Private customer-specific fields with getters and setters.
 */
@Entity
@DiscriminatorValue("CUSTOMER")
public class CustomerModel extends UserModel {

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "customer_type", length = 30)
    private String customerType; // e.g. "Regular", "Premium"

    public CustomerModel() {
        super();
        this.role = "CUSTOMER";
        this.customerType = "Regular";
    }

    public CustomerModel(String id, String name, String email, String password,
                         String phone, String address, String customerType) {
        super(id, name, email, password, "CUSTOMER");
        this.phone = phone != null ? phone : "";
        this.address = address != null ? address : "";
        this.customerType = customerType != null ? customerType : "Regular";
    }

    // --- Polymorphic Method Overrides ---

    @Override
    public String getRoleTitle() {
        return "Bakery Customer (" + this.customerType + ")";
    }

    @Override
    public boolean hasAdminAccess() {
        return false;
    }

    @Override
    public String toFileRecord() {
        return String.join("|",
                "CUSTOMER",
                id != null ? id : "",
                name != null ? name : "",
                email != null ? email : "",
                password != null ? password : "",
                phone != null ? phone : "",
                address != null ? address : "",
                customerType != null ? customerType : "Regular"
        );
    }

    // --- Getters & Setters ---

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone != null ? phone.trim() : "";
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address != null ? address.trim() : "";
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType != null ? customerType.trim() : "Regular";
    }
}
