package com.crumb.crumb.model.user.dto;

import com.crumb.crumb.model.user.AdminModel;
import com.crumb.crumb.model.user.CustomerModel;
import com.crumb.crumb.model.user.UserModel;

/**
 * Data Transfer Object for safely returning user data without exposing passwords.
 * Demonstrates polymorphic extraction of role title and privileges from UserModel.
 */
public class UserResponseDTO {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String customerType;
    private String department;
    private String role;
    private String roleTitle;
    private boolean adminAccess;

    public UserResponseDTO() {
    }

    public static UserResponseDTO fromEntity(UserModel user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        // Polymorphic invocations:
        dto.setRoleTitle(user.getRoleTitle());
        dto.setAdminAccess(user.hasAdminAccess());

        if (user instanceof CustomerModel customer) {
            dto.setPhone(customer.getPhone());
            dto.setAddress(customer.getAddress());
            dto.setCustomerType(customer.getCustomerType());
        } else if (user instanceof AdminModel admin) {
            dto.setDepartment(admin.getDepartment());
        }

        return dto;
    }

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
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public boolean isAdminAccess() {
        return adminAccess;
    }

    public void setAdminAccess(boolean adminAccess) {
        this.adminAccess = adminAccess;
    }
}
