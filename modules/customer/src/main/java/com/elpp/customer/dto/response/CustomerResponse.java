package com.elpp.customer.dto.response;

import com.elpp.customer.enums.EmploymentType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerResponse {

    private Long id;

    private String customerNumber;

    private String firstName;

    private String lastName;

    private String email;

    private String mobileNumber;

    private String panNumber;

    private LocalDate dateOfBirth;

    private EmploymentType employmentType;

    private BigDecimal annualIncome;

    private String status;

    public CustomerResponse() {
    }

    public CustomerResponse(Long id,
                            String customerNumber,
                            String firstName,
                            String lastName,
                            String email,
                            String mobileNumber,
                            String panNumber,
                            LocalDate dateOfBirth,
                            EmploymentType employmentType,
                            BigDecimal annualIncome,
                            String status) {

        this.id = id;
        this.customerNumber = customerNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.panNumber = panNumber;
        this.dateOfBirth = dateOfBirth;
        this.employmentType = employmentType;
        this.annualIncome = annualIncome;
        this.status = status;
    }

    // Generate Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public BigDecimal getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(BigDecimal annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
