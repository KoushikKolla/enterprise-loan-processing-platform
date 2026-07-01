package com.elpp.customer.service;

import com.elpp.common.response.ApiResponse;
import com.elpp.customer.dto.request.*;
import com.elpp.customer.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    ApiResponse<CustomerResponse> registerCustomer(CreateCustomerRequest request);
    ApiResponse<CustomerResponse> getCustomerByCustomerNumber(String customerNumber);
    ApiResponse<List<CustomerResponse>> getAllCustomers();
    ApiResponse<List<CustomerResponse>> getCustomers(int page,int size);
    ApiResponse<List<CustomerResponse>> searchCustomers(String keyword);
    ApiResponse<CustomerResponse> updateMobile(String customerNumber, UpdateMobileRequest request);
    ApiResponse<CustomerResponse> updateEmail(String customerNumber, UpdateEmailRequest request);
    ApiResponse<CustomerResponse> updateEmployment(String customerNumber, UpdateEmploymentRequest request);
    ApiResponse<CustomerResponse> updateAnnualIncome(String customerNumber, UpdateAnnualIncomeRequest request);
}

