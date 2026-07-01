package com.elpp.customer.dto.request;

import com.elpp.customer.enums.EmploymentType;
import jakarta.validation.constraints.NotNull;

public class UpdateEmploymentRequest {
    @NotNull(message = "EMPLOYMENT TYPE IS REQUIRED")
    private EmploymentType employmentType;

    public UpdateEmploymentRequest() {
    }

    public UpdateEmploymentRequest(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }
}
