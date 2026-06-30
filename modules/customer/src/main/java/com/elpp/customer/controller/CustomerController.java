package com.elpp.customer.controller;

import com.elpp.common.response.ApiResponse;
import com.elpp.customer.dto.request.CreateCustomerRequest;
import com.elpp.customer.dto.response.CustomerResponse;
import com.elpp.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
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
    public ApiResponse<List<CustomerResponse>> getCustomers(@RequestParam(defaultValue = "0")int page,
                                                            @RequestParam(defaultValue = "10")int size){
        return customerService.getCustomers(page, size);
    }
}
