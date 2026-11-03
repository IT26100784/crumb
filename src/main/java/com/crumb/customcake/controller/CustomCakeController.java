package com.crumb.customcake.controller;

import com.crumb.common.auth.SessionAuthHelper;
import com.crumb.common.dto.ApiResponse;
import com.crumb.common.exception.ForbiddenException;
import com.crumb.customcake.dto.CustomCakeRequest;
import com.crumb.customcake.dto.CustomCakeResponse;
import com.crumb.customcake.dto.CustomCakeStatusUpdate;
import com.crumb.customcake.service.CustomCakeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/custom-cakes")
@RequiredArgsConstructor
public class CustomCakeController {

    private final CustomCakeService customCakeService;


    @PostMapping("/preview")
    public ResponseEntity<ApiResponse<Map<String, Object>>> previewPrice(@Valid @RequestBody CustomCakeRequest request) {
        double estimatedPrice = customCakeService.calculatePreviewPrice(request);
        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "estimatedPrice", estimatedPrice,
                "cakeType", request.getCakeType()
        )));
    }


    @PostMapping
    public ResponseEntity<ApiResponse<CustomCakeResponse>> createRequest(
            @Valid @RequestBody CustomCakeRequest request,
            HttpSession session) {
        Long customerId = SessionAuthHelper.requireLoggedIn(session);
        CustomCakeResponse response = customCakeService.createRequest(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Custom cake request submitted successfully!", response));
    }


    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<CustomCakeResponse>>> getMyRequests(HttpSession session) {
        Long customerId = SessionAuthHelper.requireLoggedIn(session);
        List<CustomCakeResponse> requests = customCakeService.getCustomerRequests(customerId);
        return ResponseEntity.ok(ApiResponse.success(requests));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomCakeResponse>> getRequestById(
            @PathVariable Long id,
            HttpSession session) {
        Long currentUserId = SessionAuthHelper.requireLoggedIn(session);
        boolean isAdmin = SessionAuthHelper.isAdmin(session);

        CustomCakeResponse cake = customCakeService.getRequestById(id);
        if (!isAdmin && !cake.getCustomerId().equals(currentUserId)) {
            throw new ForbiddenException("You are not authorized to view this custom cake order.");
        }

        return ResponseEntity.ok(ApiResponse.success(cake));
    }


    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<CustomCakeResponse>> cancelRequest(
            @PathVariable Long id,
            HttpSession session) {
        Long currentUserId = SessionAuthHelper.requireLoggedIn(session);
        boolean isAdmin = SessionAuthHelper.isAdmin(session);

        CustomCakeResponse cancelled = customCakeService.cancelRequest(id, currentUserId, isAdmin);
        return ResponseEntity.ok(ApiResponse.success("Custom cake request cancelled.", cancelled));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomCakeResponse>>> getAllRequests(
            @RequestParam(required = false) String status,
            HttpSession session) {
        SessionAuthHelper.requireAdmin(session);
        List<CustomCakeResponse> list = customCakeService.getAllRequests(status);
        return ResponseEntity.ok(ApiResponse.success(list));
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<CustomCakeResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody CustomCakeStatusUpdate update,
            HttpSession session) {
        SessionAuthHelper.requireAdmin(session);
        CustomCakeResponse updated = customCakeService.updateStatus(id, update);
        return ResponseEntity.ok(ApiResponse.success("Status updated to " + updated.getStatus(), updated));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRequest(
            @PathVariable Long id,
            HttpSession session) {
        SessionAuthHelper.requireAdmin(session);
        customCakeService.deleteRequest(id);
        return ResponseEntity.ok(ApiResponse.success("Custom cake request removed.", null));
    }
}