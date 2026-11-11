
package com.crumb.customer.controller;

import com.crumb.common.auth.SessionAuthHelper;
import com.crumb.common.dto.ApiResponse;
import com.crumb.customer.dto.CustomerResponse;
import com.crumb.customer.dto.LoginRequest;
import com.crumb.customer.dto.RegisterRequest;
import com.crumb.customer.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class CustomerAuthController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<CustomerResponse>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpSession session) {
        CustomerResponse customer = customerService.register(request);
        SessionAuthHelper.loginUser(session, customer.getId(), customer.getFullName(), customer.getEmail(), customer.getRole());
        return ResponseEntity.ok(ApiResponse.success("Account registered successfully! Welcome to Crumb.", customer));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<CustomerResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session) {
        CustomerResponse customer = customerService.authenticate(request);
        SessionAuthHelper.loginUser(session, customer.getId(), customer.getFullName(), customer.getEmail(), customer.getRole());
        return ResponseEntity.ok(ApiResponse.success("Welcome back, " + customer.getFullName() + "!", customer));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpSession session) {
        SessionAuthHelper.logoutUser(session);
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully.", null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCurrentUser(HttpSession session) {
        Long userId = SessionAuthHelper.getCurrentUserId(session);
        if (userId == null) {
            return ResponseEntity.ok(ApiResponse.success("Guest session", null));
        }
        CustomerResponse customer = customerService.getCustomerById(userId);
        return ResponseEntity.ok(ApiResponse.success("Active session", customer));
    }
}
