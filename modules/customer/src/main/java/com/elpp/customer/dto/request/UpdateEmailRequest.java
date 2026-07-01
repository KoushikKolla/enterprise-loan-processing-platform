package com.elpp.customer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UpdateEmailRequest {
    @NotBlank(message = "EMAL IS REQUIRED")
    @Email(message = "INVALID EMAIL FORMAT")
    private String email;

    public UpdateEmailRequest() {
    }

    public UpdateEmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
