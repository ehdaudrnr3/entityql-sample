package com.entityql.store.repository;

import com.entityql.common.repository.BulkRepository;
import com.entityql.store.domain.entity.Store;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.stereotype.Repository;

import static com.entityql.domain.entity.EStore.qStore;

@Repository
public class StoreBulkRepository extends BulkRepository<Store> {

    public StoreBulkRepository(SQLQueryFactory sqlQueryFactory) {
        super(sqlQueryFactory, qStore);
    }
}
