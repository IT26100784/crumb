package com.crumb.crumb.model.user.dto;

/**
 * Data Transfer Object for incoming user registration and profile update requests.
 */
public class UserRequestDTO {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String customerType;
    private String role;

    public UserRequestDTO() {
    }

    public UserRequestDTO(String name, String email, String password, String phone, String address, String customerType, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.customerType = customerType;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
