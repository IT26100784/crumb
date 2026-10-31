package com.crumb.customer.service;

import com.crumb.common.exception.BadRequestException;
import com.crumb.common.exception.ResourceNotFoundException;
import com.crumb.customer.dto.CustomerResponse;
import com.crumb.customer.dto.CustomerUpdateRequest;
import com.crumb.customer.dto.LoginRequest;
import com.crumb.customer.dto.RegisterRequest;
import com.crumb.customer.entity.Customer;
import com.crumb.customer.entity.PremiumCustomer;
import com.crumb.customer.entity.RegularCustomer;
import com.crumb.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerResponse register(RegisterRequest request) {
        if (customerRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new BadRequestException("An account with email " + request.getEmail() + " already exists.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Customer customer;

        if ("PREMIUM".equalsIgnoreCase(request.getCustomerType())) {
            PremiumCustomer premium = new PremiumCustomer(
                    request.getFullName().trim(),
                    request.getEmail().trim().toLowerCase(),
                    request.getPhone().trim(),
                    encodedPassword,
                    request.getAddress() != null ? request.getAddress().trim() : ""
            );
            customer = premium;
        } else {
            RegularCustomer regular = new RegularCustomer(
                    request.getFullName().trim(),
                    request.getEmail().trim().toLowerCase(),
                    request.getPhone().trim(),
                    encodedPassword,
                    request.getAddress() != null ? request.getAddress().trim() : ""
            );
            customer = regular;
        }

        Customer saved = customerRepository.save(customer);
        return CustomerResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse authenticate(LoginRequest request) {
        Customer customer = customerRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new BadRequestException("Invalid email or password."));

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new BadRequestException("Invalid email or password.");
        }

        if (!customer.isActive()) {
            throw new BadRequestException("Your account is deactivated. Please contact Crumb bakery support.");
        }

        return CustomerResponse.fromEntity(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = getCustomerEntity(id);
        return CustomerResponse.fromEntity(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerEntity(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    @Override
    public CustomerResponse updateProfile(Long id, CustomerUpdateRequest request) {
        Customer customer = getCustomerEntity(id);

        customer.setFullName(request.getFullName().trim());
        customer.setPhone(request.getPhone().trim());
        if (request.getAddress() != null) {
            customer.setAddress(request.getAddress().trim());
        }

        if (request.getNewPassword() != null && !request.getNewPassword().trim().isEmpty()) {
            if (request.getNewPassword().trim().length() < 6) {
                throw new BadRequestException("New password must be at least 6 characters long.");
            }
            customer.setPassword(passwordEncoder.encode(request.getNewPassword().trim()));
        }

        Customer saved = customerRepository.save(customer);
        return CustomerResponse.fromEntity(saved);
    }

    @Override
    public CustomerResponse upgradeToPremium(Long id) {
        Customer current = getCustomerEntity(id);
        if (current instanceof PremiumCustomer) {
            return CustomerResponse.fromEntity(current);
        }

        PremiumCustomer premium = new PremiumCustomer();
        premium.setId(current.getId());
        premium.setFullName(current.getFullName());
        premium.setEmail(current.getEmail());
        premium.setPhone(current.getPhone());
        premium.setPassword(current.getPassword());
        premium.setAddress(current.getAddress());
        premium.setRole(current.getRole());
        premium.setLoyaltyPoints(current.getLoyaltyPoints() + 100);
        premium.setActive(current.isActive());
        premium.setMembershipExpiry(LocalDate.now().plusYears(1));
        premium.setVipConciergeNote("Upgraded to VIP Tier");

        Customer saved = customerRepository.save(premium);
        return CustomerResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> searchCustomers(String query) {
        List<Customer> customers;
        if (query != null && !query.trim().isEmpty()) {
            customers = customerRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    query.trim(), query.trim()
            );
        } else {
            customers = customerRepository.findAll();
        }

        return customers.stream()
                .map(CustomerResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse toggleCustomerStatus(Long id) {
        Customer customer = getCustomerEntity(id);
        customer.setActive(!customer.isActive());
        Customer saved = customerRepository.save(customer);
        return CustomerResponse.fromEntity(saved);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerEntity(id);
        if ("ADMIN".equalsIgnoreCase(customer.getRole())) {
            throw new BadRequestException("Primary administrator accounts cannot be deleted.");
        }
        customerRepository.delete(customer);
    }

    @Override
    public void initDefaultAdmin() {
        if (!customerRepository.existsByEmail("admin@crumb.com")) {
            PremiumCustomer admin = new PremiumCustomer();
            admin.setFullName("Crumb Head Baker Admin");
            admin.setEmail("admin@crumb.com");
            admin.setPhone("0112345678");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setAddress("Crumb Bakery Headquarters, Colombo");
            admin.setRole("ADMIN");
            admin.setLoyaltyPoints(999);
            admin.setActive(true);
            admin.setMembershipExpiry(LocalDate.now().plusYears(10));
            admin.setVipConciergeNote("System Master Administrator");

            customerRepository.save(admin);
            System.out.println(">>> SEED: Default administrator created -> admin@crumb.com / Admin@123");
        }
    }
}