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
    public Result<CustomersRecord> findAll(){
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

    public String getLastCustomerNumber(){
        return dsl
                .select(CUSTOMERS.CUSTOMER_NUMBER)
                .from(CUSTOMERS)
                .orderBy(CUSTOMERS.ID.desc())
                .limit(1)
                .fetchOne(CUSTOMERS.CUSTOMER_NUMBER);
    }
    public boolean existsByMobileNumber(String mobileNumber){
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(CUSTOMERS)
                        .where(CUSTOMERS.MOBILE_NUMBER.eq(mobileNumber))
        );
    }

    public boolean existsByEmail(String email){
        return dsl.fetchExists(dsl.selectOne()
                .from(CUSTOMERS)
                .where(CUSTOMERS.EMAIL.eq(email))
        );
    }

    public boolean existsByPanNumber(String panNumber){
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(CUSTOMERS)
                        .where(CUSTOMERS.PAN_NUMBER.eq(panNumber))
        );
    }

    public boolean existsByAadhaarNumber(String aadhaarNumber){
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(CUSTOMERS)
                        .where(CUSTOMERS.AADHAAR_NUMBER.eq(aadhaarNumber))
        );
    }
}
