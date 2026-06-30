package com.elpp.customer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdateMobileRequest {
    @NotBlank(message="MOBILE NUMBER IS REQUIRED")
    @Pattern(regexp = "^[6-9]\\d{9}$",message = "INVALID MOBILE NUMBER")
    private String mobileNumber;

    public UpdateMobileRequest() {
    }

    public UpdateMobileRequest(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
