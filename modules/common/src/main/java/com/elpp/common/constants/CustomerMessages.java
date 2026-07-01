package com.elpp.common.constants;

public final class CustomerMessages{

    private CustomerMessages() {
    }

    // Success Messages
    public static final String CUSTOMER_REGISTERED =
            "CUSTOMER REGISTERED SUCCESSFULLY.";

    public static final String CUSTOMER_FETCHED =
            "CUSTOMER FETCHED SUCCESSFULLY.";

    public static final String CUSTOMERS_FETCHED =
            "CUSTOMERS FETCHED SUCCESSFULLY.";

    public static final String CUSTOMERS_FOUND =
            "CUSTOMERS FOUND SUCCESSFULLY.";

    public static final String MOBILE_UPDATED =
            "MOBILE NUMBER UPDATED SUCCESSFULLY.";

    public static final String EMAIL_UPDATED =
            "EMAIL UPDATED SUCCESSFULLY.";

    public static final String EMPLOYMENT_UPDATED =
            "EMPLOYMENT TYPE UPDATED SUCCESSFULLY.";

    public static final String ANNUAL_INCOME_UPDATED =
            "ANNUAL INCOME UPDATED SUCCESSFULLY.";

    // Error Messages
    public static final String CUSTOMER_NOT_FOUND =
            "CUSTOMER NOT FOUND.";

    public static final String MOBILE_ALREADY_REGISTERED =
            "MOBILE NUMBER IS ALREADY REGISTERED.";

    public static final String EMAIL_ALREADY_REGISTERED =
            "EMAIL IS ALREADY REGISTERED.";

    public static final String PAN_ALREADY_REGISTERED =
            "PAN NUMBER IS ALREADY REGISTERED.";

    public static final String AADHAAR_ALREADY_REGISTERED =
            "AADHAAR NUMBER IS ALREADY REGISTERED.";

    public static final String SAME_MOBILE =
            "NEW MOBILE NUMBER MUST BE DIFFERENT FROM CURRENT MOBILE NUMBER.";

    public static final String SAME_EMAIL =
            "NEW EMAIL MUST BE DIFFERENT FROM CURRENT EMAIL.";

    public static final String SAME_EMPLOYMENT =
            "NEW EMPLOYMENT TYPE MUST BE DIFFERENT FROM CURRENT EMPLOYMENT TYPE.";

    public static final String SAME_ANNUAL_INCOME =
            "NEW ANNUAL INCOME MUST BE DIFFERENT FROM CURRENT ANNUAL INCOME.";

    public static final String MOBILE_UPDATE_RESTRICTED =
            "MOBILE NUMBER CAN ONLY BE UPDATED ONCE EVERY 15 DAYS.";

    public static final String EMAIL_UPDATE_RESTRICTED =
            "EMAIL CAN ONLY BE UPDATED ONCE EVERY 15 DAYS.";
}