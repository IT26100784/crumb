
package com.crumb.customer.controller;

import com.crumb.common.auth.SessionAuthHelper;
import com.crumb.common.dto.ApiResponse;
import com.crumb.common.exception.ForbiddenException;
import com.crumb.customer.dto.CustomerResponse;
import com.crumb.customer.dto.CustomerUpdateRequest;
import com.crumb.customer.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(
            @PathVariable Long id,
            HttpSession session) {

        Long currentUserId = SessionAuthHelper.requireLoggedIn(session);
        boolean isAdmin = SessionAuthHelper.isAdmin(session);

        if (!isAdmin && !currentUserId.equals(id)) {
            throw new ForbiddenException(
                    "You are not authorized to view another customer's profile."
            );
        }

        CustomerResponse customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(customer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateRequest request,
            HttpSession session) {

        Long currentUserId = SessionAuthHelper.requireLoggedIn(session);
        boolean isAdmin = SessionAuthHelper.isAdmin(session);

        if (!isAdmin && !currentUserId.equals(id)) {
            throw new ForbiddenException(
                    "You are not authorized to edit another customer's profile."
            );
        }

        CustomerResponse updated = customerService.updateProfile(id, request);

        return ResponseEntity.ok(
                ApiResponse.success("Profile updated successfully!", updated)
        );
    }

    @PostMapping("/{id}/upgrade")
    public ResponseEntity<ApiResponse<CustomerResponse>> upgradeToPremium(
            @PathVariable Long id,
            HttpSession session) {

        SessionAuthHelper.requireAdmin(session);

        CustomerResponse upgraded = customerService.upgradeToPremium(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer upgraded to VIP Gold Membership successfully.",
                        upgraded
                )
        );
    }

    @PostMapping("/{id}/downgrade")
    public ResponseEntity<ApiResponse<CustomerResponse>> downgradeToRegular(
            @PathVariable Long id,
            HttpSession session) {

        SessionAuthHelper.requireAdmin(session);

        CustomerResponse downgraded = customerService.downgradeToRegular(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer downgraded to Regular Membership successfully.",
                        downgraded
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers(
            @RequestParam(required = false) String query,
            HttpSession session) {

        SessionAuthHelper.requireAdmin(session);

        List<CustomerResponse> customers = customerService.searchCustomers(query);

        return ResponseEntity.ok(ApiResponse.success(customers));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<CustomerResponse>> toggleStatus(
            @PathVariable Long id,
            HttpSession session) {

        SessionAuthHelper.requireAdmin(session);

        CustomerResponse customer = customerService.toggleCustomerStatus(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        customer.isActive()
                                ? "Account activated."
                                : "Account suspended.",
                        customer
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(
            @PathVariable Long id,
            HttpSession session) {

        SessionAuthHelper.requireAdmin(session);

        customerService.deleteCustomer(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer removed successfully.",
                        null
                )
        );
    }
}
