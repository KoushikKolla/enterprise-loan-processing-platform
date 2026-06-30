package com.elpp.customer.service.impl;

import com.elpp.common.exception.DuplicateResourceException;
import com.elpp.common.exception.ResourceNotFoundException;
import com.elpp.common.response.ApiResponse;
import com.elpp.customer.dto.request.CreateCustomerRequest;
import com.elpp.customer.dto.response.CustomerResponse;
import com.elpp.customer.enums.CustomerStatus;
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

        validateDuplicateCustomer(request);
        // Convert Request DTO -> CustomersRecord
        CustomersRecord customerRecord = customerRepository.newCustomerRecord();

        customerMapper.toRecord(request,customerRecord);

        // Business Logic
        String lastCustomerNumber= customerRepository.getLastCustomerNumber();
        customerRecord.setCustomerNumber(generateCustomerNumber(lastCustomerNumber));
        customerRecord.setStatus(CustomerStatus.PENDING.name());

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
        Result<CustomersRecord> customers=customerRepository.findAll();
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


    private String generateCustomerNumber(String lastCustomerNumber) {
        //FIRST CUSTOMER
        if(lastCustomerNumber==null){
            return "CUST000001";
        }
        //REMOVE "CUST"
        String numericPart=lastCustomerNumber.substring(4);
        //CONVERT INTO INTEGER
        int number=Integer.parseInt(numericPart);
        //INCREMENT
        number++;
        return String.format("CUST%06d",number);
    }

    private void validateDuplicateCustomer(CreateCustomerRequest request) {
        //CHECKS DUPLICATE NUMBERS ARE THERE ARE NOT BEFORE REGISTERING
        if (customerRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new DuplicateResourceException("MOBILE NUMBER IS ALREADY REGISTERED");
        }
        if(customerRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("EMAIL IS ALREADY REGISTERED");
        }
        if(customerRepository.existsByAadhaarNumber(request.getAadhaarNumber())){
            throw new DuplicateResourceException("AADHAAR NUMBER IS ALREADY REGISTERED");
        }
        if(customerRepository.existsByPanNumber(request.getPanNumber())){
            throw new DuplicateResourceException("PAN NUMBER IS ALREADY REGISTERED");
        }
    }
}
