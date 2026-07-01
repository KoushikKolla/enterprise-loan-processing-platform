package com.elpp.customer.service.impl;

import com.elpp.common.constants.CustomerMessages;
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

        // Convert CustomersRecord -> Response DTO
        return buildSuccessResponse(
                customerRecord,
                CustomerMessages.CUSTOMER_REGISTERED
        );
    }

    @Override
    public ApiResponse<CustomerResponse> getCustomerByCustomerNumber(String customerNumber) {
        CustomersRecord customers=getCustomerOrThrow(customerNumber);
        CustomerResponse response= customerMapper.toResponse(customers);
        return new ApiResponse<>(true,
                CustomerMessages.CUSTOMER_FETCHED,
                response);
    }

    @Override
    public ApiResponse<List<CustomerResponse>> getAllCustomers() {
        Result<CustomersRecord> customers=customerRepository.findAll();
        List<CustomerResponse> responses= customerMapper.toResponseList(customers);
        return new ApiResponse<>(true,CustomerMessages.CUSTOMERS_FETCHED,responses);
    }

    @Override
    public ApiResponse<List<CustomerResponse>> getCustomers(int page, int size) {
        Result<CustomersRecord> customers=customerRepository.findAll(page, size);
        List<CustomerResponse> responses= customerMapper.toResponseList(customers);
        return new ApiResponse<>(true,CustomerMessages.CUSTOMERS_FETCHED,responses);
    }

    @Override
    public ApiResponse<List<CustomerResponse>> searchCustomers(String keyword) {
        Result<CustomersRecord> customers= customerRepository.findByKeyword(keyword);
        List<CustomerResponse> responses = customerMapper.toResponseList(customers);
        return new ApiResponse<>(true,
                CustomerMessages.CUSTOMERS_FOUND
                    ,responses);
    }

    @Override
    public ApiResponse<CustomerResponse> updateMobile(String customerNumber, UpdateMobileRequest request) {
        CustomersRecord customer=getCustomerOrThrow(customerNumber);
        if (customer.getMobileNumber().equals(request.getMobileNumber())) {
            throw new DuplicateResourceException(CustomerMessages.SAME_MOBILE);
        }
        //Validates the update duration set to 15 days
        validateMobileUpdatePeriod(customer);

        if(customerRepository.existsByMobileNumber(request.getMobileNumber())){
            throw new DuplicateResourceException(CustomerMessages.MOBILE_ALREADY_REGISTERED);
        }
        customer.setMobileNumber(request.getMobileNumber());
        customer.setLastMobileUpdatedAt(LocalDateTime.now());
        return buildSuccessResponse(
                customer,
                CustomerMessages.MOBILE_UPDATED
        );
    }

    @Override
    public ApiResponse<CustomerResponse> updateEmail(String customerNumber, UpdateEmailRequest request) {
        CustomersRecord customer= customerRepository.findByCustomerNumber(customerNumber);
        if(customer==null){
            throw  new ResourceNotFoundException(CustomerMessages.CUSTOMER_NOT_FOUND);
        }
        if(customer.getEmail().equalsIgnoreCase(request.getEmail())){
            throw new InvalidRequestException(CustomerMessages.SAME_EMAIL);
        }
        validateEmailUpdatePeriod(customer);
        //DUPLICATE EMAIL
        if(customerRepository.existsByMobileNumber(request.getEmail())){
            throw new DuplicateResourceException(CustomerMessages.EMAIL_ALREADY_REGISTERED);
        }
        customer.setEmail(request.getEmail());

        customer.setLastEmailUpdatedAt(LocalDateTime.now());

        CustomersRecord updatedCustomer=customerRepository.save(customer);

        return buildSuccessResponse(
                updatedCustomer,
                CustomerMessages.EMAIL_UPDATED
        );
    }

    @Override
    public ApiResponse<CustomerResponse> updateEmployment(String customerNumber, UpdateEmploymentRequest request) {
        CustomersRecord customer=getCustomerOrThrow(customerNumber);
        if(customer.getEmploymentType().equals(request.getEmploymentType().name())){
            throw new InvalidRequestException(CustomerMessages.SAME_EMPLOYMENT);
        }
        customer.setEmploymentType(request.getEmploymentType().name());

        return buildSuccessResponse(
                customer,
                CustomerMessages.EMPLOYMENT_UPDATED
        );
    }

    @Override
    public ApiResponse<CustomerResponse> updateAnnualIncome(String customerNumber, UpdateAnnualIncomeRequest request) {
        CustomersRecord customer=getCustomerOrThrow(customerNumber);

        if (customer.getAnnualIncome().compareTo(request.getAnnualIncome()) == 0) {
            throw new InvalidRequestException(CustomerMessages.SAME_ANNUAL_INCOME);
        }

        customer.setAnnualIncome(request.getAnnualIncome());

        return buildSuccessResponse(
                customer,
                CustomerMessages.ANNUAL_INCOME_UPDATED
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
            throw new DuplicateResourceException(CustomerMessages.MOBILE_ALREADY_REGISTERED);
        }
        if(customerRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException(CustomerMessages.EMAIL_ALREADY_REGISTERED);
        }
        if(customerRepository.existsByAadhaarNumber(request.getAadhaarNumber())){
            throw new DuplicateResourceException(CustomerMessages.AADHAAR_ALREADY_REGISTERED);
        }
        if(customerRepository.existsByPanNumber(request.getPanNumber())){
            throw new DuplicateResourceException(CustomerMessages.PAN_ALREADY_REGISTERED);
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
            throw new InvalidRequestException(CustomerMessages.MOBILE_UPDATE_RESTRICTED);
        }
    }
    private void validateEmailUpdatePeriod(CustomersRecord customer){
        LocalDateTime lastUpdated= customer.getLastEmailUpdatedAt();
        if(lastUpdated==null){
            return;
        }
        long days= Duration.between(lastUpdated,LocalDateTime.now()).toDays();

        if(days<15){
            throw new InvalidRequestException(CustomerMessages.EMAIL_UPDATE_RESTRICTED);
        }
    }

    private CustomersRecord getCustomerOrThrow(String customerNumber) {

        CustomersRecord customer =
                customerRepository.findByCustomerNumber(customerNumber);

        if (customer == null) {
            throw new ResourceNotFoundException(
                    CustomerMessages.CUSTOMER_NOT_FOUND
            );
        }
        return customer;
    }

    private ApiResponse<CustomerResponse> buildSuccessResponse(
            CustomersRecord customer,
            String message) {

        CustomersRecord updatedCustomer =
                customerRepository.save(customer);

        CustomerResponse response =
                customerMapper.toResponse(updatedCustomer);

        return new ApiResponse<>(
                true,
                message,
                response
        );
    }
}
