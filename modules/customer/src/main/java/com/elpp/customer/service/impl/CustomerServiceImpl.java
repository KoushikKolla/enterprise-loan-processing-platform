package com.elpp.customer.service.impl;

import com.elpp.common.exception.DuplicateResourceException;
import com.elpp.common.exception.InvalidRequestException;
import com.elpp.common.exception.ResourceNotFoundException;
import com.elpp.common.response.ApiResponse;
import com.elpp.customer.dto.request.*;
import com.elpp.customer.dto.response.CustomerResponse;
import com.elpp.customer.enums.CustomerStatus;
import com.elpp.customer.mapper.CustomerMapper;
import com.elpp.customer.service.CustomerService;
import com.elpp.infrastructure.jooq.generated.tables.records.CustomersRecord;
import com.elpp.infrastructure.repository.CustomerRepository;
import org.jooq.Result;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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

    @Override
    public ApiResponse<CustomerResponse> updateMobile(String customerNumber, UpdateMobileRequest request) {
        CustomersRecord customer= customerRepository.findByCustomerNumber(customerNumber);
        if(customer==null){
            throw new ResourceNotFoundException("CUSTOMER NOT FOUND");
        }
        if (customer.getMobileNumber().equals(request.getMobileNumber())) {
            throw new DuplicateResourceException("NEW MOBILE NUMBER MUST BE DIFFERENT FROM CURRENT MOBILE NUMBER.");
        }
        //Validates the update duration set to 15 days
        validateMobileUpdatePeriod(customer);

        if(customerRepository.existsByMobileNumber(request.getMobileNumber())){
            throw new DuplicateResourceException("MOBILE NUMBER IS ALREADY REGISTERED. ");
        }
        customer.setMobileNumber(request.getMobileNumber());
        customer.setLastMobileUpdatedAt(LocalDateTime.now());
        CustomersRecord updatedCustomer=customerRepository.save(customer);
        CustomerResponse response=customerMapper.toResponse(updatedCustomer);
        return new ApiResponse<>(true,"MOBILE NUMBER UPDATED SUCCESSFULLY.",response);
    }

    @Override
    public ApiResponse<CustomerResponse> updateEmail(String customerNumber, UpdateEmailRequest request) {
        CustomersRecord customer= customerRepository.findByCustomerNumber(customerNumber);
        if(customer==null){
            throw  new ResourceNotFoundException("CUSTOMER NOT FOUND");
        }
        if(customer.getEmail().equalsIgnoreCase(request.getEmail())){
            throw new InvalidRequestException("NEW EMAIL MUST BE DIFFERENT FROM CURRENT EMAIL");
        }
        validateEmailUpdatePeriod(customer);
        //DUPLICATE EMAIL
        if(customerRepository.existsByMobileNumber(request.getEmail())){
            throw new DuplicateResourceException("EMAIL IS ALREADY REGISTERED");
        }
        customer.setEmail(request.getEmail());

        customer.setLastEmailUpdatedAt(LocalDateTime.now());

        CustomersRecord updatedCustomer=customerRepository.save(customer);

        CustomerResponse response=customerMapper.toResponse(updatedCustomer);

        return new ApiResponse<>(true,"EMAIL UPDATED SUCCESSFULLY",response);
    }

    @Override
    public ApiResponse<CustomerResponse> updateEmployment(String customerNumber, UpdateEmploymentRequest request) {
        CustomersRecord customer=customerRepository.findByCustomerNumber(customerNumber);
        if(customer==null){
            throw new ResourceNotFoundException("CUSTOMER NOT FOUND");
        }
        if(customer.getEmploymentType().equals(request.getEmploymentType().name())){
            throw new InvalidRequestException("NEW EMPLOYMENT TYPE MUST BE DIFFERENT FROM EMPLOYMENT TYPE");
        }
        customer.setEmploymentType(request.getEmploymentType().name());

        CustomersRecord updatedCustomer =customerRepository.save(customer);

        CustomerResponse response= customerMapper.toResponse(updatedCustomer);

        return new ApiResponse<>(true,"EMPLOYMENT TYPE UPDATED SUCCESSFULLY",response);
    }

    @Override
    public ApiResponse<CustomerResponse> updateAnnualIncome(String customerNumber, UpdateAnnualIncomeRequest request) {
        CustomersRecord customer =
                customerRepository.findByCustomerNumber(customerNumber);

        if (customer == null) {
            throw new ResourceNotFoundException("CUSTOMER NOT FOUND");
        }

        if (customer.getAnnualIncome().compareTo(request.getAnnualIncome()) == 0) {
            throw new InvalidRequestException("NEW ANNUAL INCOME MUST BE DIFFERENT FROM CURRENT ANNUAL INCOME.");
        }

        customer.setAnnualIncome(request.getAnnualIncome());

        CustomersRecord updatedCustomer = customerRepository.save(customer);

        CustomerResponse response = customerMapper.toResponse(updatedCustomer);

        return new ApiResponse<>(
                true,
                "ANNUAL INCOME UPDATED SUCCESSFULLY.",
                response
        );
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

    private void validateMobileUpdatePeriod(CustomersRecord customer){
        LocalDateTime lastUpdated=customer.getLastMobileUpdatedAt();
        //FIRST UPDATE IS ALWAYS ALLOWED
        if(lastUpdated==null){
            return;
        }
        long days= Duration.between(lastUpdated,LocalDateTime.now()).toDays();
        if(days<15){
            throw new InvalidRequestException("MOBILE NUMBER CAN ONLY BE UPDATED ONCE EVERY 15 DAYS");
        }
    }
    private void validateEmailUpdatePeriod(CustomersRecord customer){
        LocalDateTime lastUpdated= customer.getLastEmailUpdatedAt();
        if(lastUpdated==null){
            return;
        }
        long days= Duration.between(lastUpdated,LocalDateTime.now()).toDays();

        if(days<15){
            throw new InvalidRequestException("EMAIL CAN ONLY BE UPDATED ONCE EVERY 15 DAYS");
        }
    }
}
