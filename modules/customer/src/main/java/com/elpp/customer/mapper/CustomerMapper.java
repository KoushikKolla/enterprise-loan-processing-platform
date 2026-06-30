package com.elpp.customer.mapper;

import com.elpp.customer.dto.request.CreateCustomerRequest;
import com.elpp.customer.dto.response.CustomerResponse;
import com.elpp.customer.enums.EmploymentType;
import com.elpp.infrastructure.jooq.generated.tables.records.CustomersRecord;
import org.jooq.Result;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerMapper {

    public void toRecord(CreateCustomerRequest request,
                         CustomersRecord customer) {

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setMobileNumber(request.getMobileNumber());
        customer.setPanNumber(request.getPanNumber());
        customer.setAadhaarNumber(request.getAadhaarNumber());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setEmploymentType(request.getEmploymentType().name());
        customer.setAnnualIncome(request.getAnnualIncome());

    }

    public CustomerResponse toResponse(CustomersRecord customer) {

        CustomerResponse response = new CustomerResponse();

        response.setId(customer.getId());
        response.setCustomerNumber(customer.getCustomerNumber());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setMobileNumber(customer.getMobileNumber());
        response.setPanNumber(customer.getPanNumber());
        response.setDateOfBirth(customer.getDateOfBirth());

        if (customer.getEmploymentType() != null) {
            response.setEmploymentType(
                    EmploymentType.valueOf(
                            customer.getEmploymentType()
                    )
            );
        }

        response.setAnnualIncome(customer.getAnnualIncome());
        response.setStatus(customer.getStatus());

        return response;
    }

    public List<CustomerResponse> toResponseList(Result<CustomersRecord> customers){
        List<CustomerResponse> responses= new ArrayList<>();
        for (CustomersRecord customer : customers){
            responses.add(toResponse(customer));
        }
        return responses;
    }
}