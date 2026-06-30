package com.elpp.customer.service.impl;

import com.elpp.common.exception.ResourceNotFoundException;
import com.elpp.common.response.ApiResponse;
import com.elpp.customer.dto.request.CreateCustomerRequest;
import com.elpp.customer.dto.response.CustomerResponse;
import com.elpp.customer.mapper.CustomerMapper;
import com.elpp.customer.service.CustomerService;
import com.elpp.infrastructure.jooq.generated.tables.records.CustomersRecord;
import com.elpp.infrastructure.repository.CustomerRepository;
import org.jooq.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public ApiResponse<CustomerResponse> registerCustomer(CreateCustomerRequest request) {

        // Convert Request DTO -> CustomersRecord
        CustomersRecord customerRecord = customerRepository.newCustomerRecord();

        customerMapper.toRecord(request,customerRecord);

        // Business Logic
        customerRecord.setCustomerNumber(generateCustomerNumber());
        customerRecord.setStatus("ACTIVE");

        // Save to Database
        CustomersRecord savedCustomer = customerRepository.save(customerRecord);

        // Convert CustomersRecord -> Response DTO
        CustomerResponse response = customerMapper.toResponse(savedCustomer);

        return new ApiResponse<>(true,"CUSTOMER REGISTERED SUCCESSFULLY",response);
    }

    @Override
    public ApiResponse<CustomerResponse> getCustomerByCustomerNumber(String customerNumber) {
        CustomersRecord customersRecord=customerRepository.findByCustomerNumber(customerNumber);
        if(customersRecord==null){
            throw  new ResourceNotFoundException("CUSTOMER NOT FOUND WITH CUSTOMER NUMBER:"+ customerNumber);
        }
        CustomerResponse response= customerMapper.toResponse(customersRecord);
        return new ApiResponse<>(true,
                "CUSTOMER FOUND",
                response);
    }

    @Override
    public ApiResponse<List<CustomerResponse>> getAllCustomers() {
        Result<CustomersRecord> customers=customerRepository.findall();
        List<CustomerResponse> responses= customerMapper.toResponseList(customers);
        return new ApiResponse<>(true,"CUSTOMERS FETCHED SUCCESSFULLY",responses);
    }

    @Override
    public ApiResponse<List<CustomerResponse>> getCustomers(int page, int size) {
        Result<CustomersRecord> customers=customerRepository.findAll(page, size);
        List<CustomerResponse> responses= customerMapper.toResponseList(customers);
        return new ApiResponse<>(true,"CUSTOMERS FETCHED SUCCESSFULLY",responses);
    }

    @Override
    public ApiResponse<List<CustomerResponse>> searchCustomers(String keyword) {
        Result<CustomersRecord> customers= customerRepository.findByKeyword(keyword);
        List<CustomerResponse> responses = customerMapper.toResponseList(customers);
        return new ApiResponse<>(true,
                "CUSTOMERS FOUND SUCCESSFULLY"
                    ,responses);
    }

    /**
     * Temporary implementation.
     * Later we will generate this from the database.
     */
    private String generateCustomerNumber() {
        return "CUST000001";
    }
}
