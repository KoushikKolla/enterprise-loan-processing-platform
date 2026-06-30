package com.elpp.customer.service;

import com.elpp.common.response.ApiResponse;
import com.elpp.customer.dto.request.CreateCustomerRequest;
import com.elpp.customer.dto.request.UpdateMobileRequest;
import com.elpp.customer.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    ApiResponse<CustomerResponse> registerCustomer(CreateCustomerRequest request);
    ApiResponse<CustomerResponse> getCustomerByCustomerNumber(String customerNumber);
    ApiResponse<List<CustomerResponse>> getAllCustomers();
    ApiResponse<List<CustomerResponse>> getCustomers(int page,int size);
    ApiResponse<List<CustomerResponse>> searchCustomers(String keyword);
    ApiResponse <CustomerResponse> updateMobile(String customerNumber, UpdateMobileRequest request);
}

