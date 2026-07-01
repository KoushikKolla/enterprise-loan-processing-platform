package com.elpp.customer.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class UpdateAnnualIncomeRequest {

    @NotNull(message = "Annual income is required")
    @DecimalMin(value = "0.01",
            message = "Annual income must be greater than zero")
    private BigDecimal annualIncome;

    public UpdateAnnualIncomeRequest() {
    }

    public BigDecimal getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(BigDecimal annualIncome) {
        this.annualIncome = annualIncome;
    }
}