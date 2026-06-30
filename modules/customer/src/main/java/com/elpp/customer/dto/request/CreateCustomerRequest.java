package com.elpp.customer.dto.request;

import com.elpp.customer.enums.EmploymentType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateCustomerRequest {
    @NotBlank(message = "FIRST NAME IS MANDATORY")
    @Size(max=100,message = "FIRST NAME CANNOT EXCEED !)) CHARACTERS")
    private String firstName;

    @NotBlank(message = "LAST NAME MANDATORY")
    @Size(max=100)
    private String lastName;

    @NotBlank(message = "EMAIL IS MANDATORY")
    @Email(message = "INVALID EMAIL FORMAT")
    private String email;

    @NotBlank(message = "MOBILE NUMBER IS MANDATORY")
    @Pattern(regexp = "^[6-9]\\d{9}$",message = "INVALD MOBILE NUMBER ")
    private String mobileNumber;

    @NotBlank(message = "PAN CARD IS MANDATORY")
    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}$")
    private String panNumber;

    @NotBlank(message = "AADHAAR CARD IS MANDATORY")
    @Pattern(regexp = "^\\d{12}$")
    private String aadhaarNumber;

    @NotNull(message = "DATE OF BIRTH IS MANDATORY")
    @Past(message = "DOB MUST BE IN PAST")
    private LocalDate dateOfBirth;

    @NotNull(message = "EMPLOYMENT TYPE IS MANDATORY")
    private EmploymentType employmentType;

    @NotNull(message = "ANNUAL INCOME IS MANDATORY")
    @Positive(message = "INCOME MUST BE GREATER THAN ZERO")
    private BigDecimal annualIncome;

    public CreateCustomerRequest() {
    }

    public CreateCustomerRequest(String firstName,
                                 String lastName,
                                 String email,
                                 String mobileNumber,
                                 String panNumber,
                                 String aadhaarNumber,
                                 LocalDate dateOfBirth,
                                 EmploymentType employmentType,
                                 BigDecimal annualIncome) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.panNumber = panNumber;
        this.aadhaarNumber = aadhaarNumber;
        this.dateOfBirth = dateOfBirth;
        this.employmentType = employmentType;
        this.annualIncome = annualIncome;
    }

    // Generate Getters and Setters


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public BigDecimal getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(BigDecimal annualIncome) {
        this.annualIncome = annualIncome;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}