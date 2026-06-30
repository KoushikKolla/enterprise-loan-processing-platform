package com.elpp.infrastructure.repository;

import com.elpp.infrastructure.jooq.generated.tables.records.CustomersRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.stereotype.Repository;

import static com.elpp.infrastructure.jooq.generated.Tables.CUSTOMERS;

@Repository
public class CustomerRepository {

    private final DSLContext dsl;

    public CustomerRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public CustomersRecord newCustomerRecord() {
        return dsl.newRecord(CUSTOMERS);
    }

    public CustomersRecord save(CustomersRecord customerRecord) {

        customerRecord.store();

        return customerRecord;
    }
    public CustomersRecord findByCustomerNumber(String customerNumber) {
        return dsl.selectFrom(CUSTOMERS)
                .where(CUSTOMERS.CUSTOMER_NUMBER.eq(customerNumber))
                .fetchOne();

    }
    public Result<CustomersRecord> findall(){
        return dsl.selectFrom(CUSTOMERS)
                .fetch();
    }
}
