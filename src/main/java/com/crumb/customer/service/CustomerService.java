package com.crumb.customer.service;

import com.crumb.customer.dto.CustomerResponse;
import com.crumb.customer.dto.CustomerUpdateRequest;
import com.crumb.customer.dto.LoginRequest;
import com.crumb.customer.dto.RegisterRequest;
import com.crumb.customer.entity.Customer;

import java.util.List;

public interface CustomerService {

    CustomerResponse register(RegisterRequest request);

    CustomerResponse authenticate(LoginRequest request);

    CustomerResponse getCustomerById(Long id);

    Customer getCustomerEntity(Long id);

    CustomerResponse updateProfile(Long id, CustomerUpdateRequest request);

    CustomerResponse upgradeToPremium(Long id);

    List<CustomerResponse> searchCustomers(String query);

    CustomerResponse toggleCustomerStatus(Long id);

    void deleteCustomer(Long id);

    void initDefaultAdmin();
}