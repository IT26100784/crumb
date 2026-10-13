package com.crumb.crumb.model.user;

/**
 * Concrete subclass representing a Bakery Customer.
 * 
 * OOP Concepts Applied:
 * - Inheritance: Inherits core user attributes from UserModel.
 * - Polymorphism: Overrides getRoleTitle(), hasAdminAccess(), and toFileRecord().
 * - Encapsulation: Private specific attributes with getters and setters.
 */
public class CustomerModel extends UserModel {

    private String phone;
    private String address;
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
        // Delimited record format: CUSTOMER|id|name|email|password|phone|address|customerType
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
