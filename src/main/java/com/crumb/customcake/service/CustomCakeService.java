package com.crumb.customcake.service;

import com.crumb.customcake.dto.CustomCakeRequest;
import com.crumb.customcake.dto.CustomCakeResponse;
import com.crumb.customcake.dto.CustomCakeStatusUpdate;
import com.crumb.customcake.entity.CustomCake;

import java.util.List;


public interface CustomCakeService {


    CustomCakeResponse createRequest(Long customerId, CustomCakeRequest request);


    double calculatePreviewPrice(CustomCakeRequest request);


    List<CustomCakeResponse> getCustomerRequests(Long customerId);


    List<CustomCakeResponse> getAllRequests(String status);


    CustomCakeResponse getRequestById(Long id);


    CustomCake getRequestEntity(Long id);


    CustomCakeResponse updateStatus(Long id, CustomCakeStatusUpdate update);


    CustomCakeResponse cancelRequest(Long id, Long customerId, boolean isAdmin);


    void deleteRequest(Long id);
}