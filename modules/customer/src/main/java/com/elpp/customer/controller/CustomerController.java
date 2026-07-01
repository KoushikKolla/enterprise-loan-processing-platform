package com.elpp.customer.controller;

import com.elpp.common.response.ApiResponse;
import com.elpp.customer.dto.request.*;
import com.elpp.customer.dto.response.CustomerResponse;
import com.elpp.customer.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@Validated
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ApiResponse<CustomerResponse> registerCustomer(@Valid @RequestBody CreateCustomerRequest request){
        return customerService.registerCustomer(request);
    }

    @GetMapping("/{customerNumber}")
    public ApiResponse<CustomerResponse> getCustomerByCustomerNumber(@PathVariable String customerNumber){
        return customerService.getCustomerByCustomerNumber(customerNumber);
    }

    @GetMapping
    public ApiResponse<List<CustomerResponse>> getAllCustomers(){
        return customerService.getAllCustomers();
    }
    @GetMapping("/page")
    public ApiResponse<List<CustomerResponse>> getCustomers(@RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be 0 or greater") int page,
                                                            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be at least 1") int size){
        return customerService.getCustomers(page, size);
    }
    @GetMapping("/search")
    public ApiResponse<List<CustomerResponse>> searchCustomers(@RequestParam @jakarta.validation.constraints.NotBlank(message = "Keyword cannot be blank") String keyword){
        return customerService.searchCustomers(keyword);
    }
    @PutMapping("/{customerNumber}/mobile")
    public ApiResponse<CustomerResponse> updateMobile(@PathVariable String customerNumber,
                                                      @Valid @RequestBody UpdateMobileRequest request){
        return customerService.updateMobile(customerNumber,request);
    }
    @PutMapping("/{customerNumber}/email")
    public ApiResponse<CustomerResponse> updateEmail(@PathVariable String customerNumber,
                                                      @Valid @RequestBody UpdateEmailRequest request){
        return customerService.updateEmail(customerNumber,request);
    }
    @PutMapping("/{customerNumber}/employment")
    public ApiResponse<CustomerResponse> updateEmployment(@PathVariable String customerNumber,
                                                          @Valid @RequestBody UpdateEmploymentRequest request){
        return customerService.updateEmployment(customerNumber,request);
    }
    @PutMapping("/{customerNumber}/annual-income")
    public ApiResponse<CustomerResponse> updateAnnualIncome(@PathVariable String customerNumber,
                                                            @Valid @RequestBody UpdateAnnualIncomeRequest request) {
        return customerService.updateAnnualIncome(customerNumber, request);
    }
}
