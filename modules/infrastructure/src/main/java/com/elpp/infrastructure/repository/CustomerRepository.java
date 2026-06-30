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

    public Result<CustomersRecord> findAll(int page,int size){
        int offset=page*size;
        return dsl.selectFrom(CUSTOMERS)
                .limit(size)
                .offset(offset)
                .fetch();
    }

    public Result<CustomersRecord> findByKeyword(String keyword){
        String searchKeyword ="%"+ keyword +"%";
        return dsl.selectFrom(CUSTOMERS)
                .where(CUSTOMERS.FIRST_NAME.likeIgnoreCase(searchKeyword))
                .or(CUSTOMERS.LAST_NAME.likeIgnoreCase(searchKeyword))
                .or(CUSTOMERS.EMAIL.likeIgnoreCase(searchKeyword))
                .or(CUSTOMERS.MOBILE_NUMBER.likeIgnoreCase(searchKeyword))
                .or(CUSTOMERS.PAN_NUMBER.likeIgnoreCase(searchKeyword))
                .fetch();
    }
}
