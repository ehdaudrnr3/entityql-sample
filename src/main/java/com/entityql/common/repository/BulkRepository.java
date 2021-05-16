package com.entityql.common.repository;

import com.entityql.config.EntityMapper;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Path;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
public abstract class BulkRepository<T> {

    private final Integer DEFAULT_CHUNK_SIZE = 1_000;

    private final SQLQueryFactory sqlQueryFactory;
    private final RelationalPath<?> relationalPath;

    public BulkRepository(SQLQueryFactory sqlQueryFactory, RelationalPath<?> relationalPath) {
        this.sqlQueryFactory = sqlQueryFactory;
        this.relationalPath = relationalPath;
    }

    public int[] batchInsert(List<T> entities) {
        return batchInsert(entities, DEFAULT_CHUNK_SIZE);
    }

    public int[] batchInsert(List<T> entities, int chunkSize) {
        List<List<T>> partition = Lists.partition(entities, chunkSize);
        int[] batchCount = new int[partition.size()];

        int index = 1;
        for(List<T> items : partition) {
            insertItems(items);
            batchCount[index-1] = items.size();
            log.info("sequence : {}, batch count : {}", index++, items.size());
        }

        return batchCount;
    }

    public void batchDeleteAll() {
        SQLDeleteClause delete = sqlQueryFactory.delete(relationalPath);
        long execute = delete.addBatch().execute();
        log.info("delete count : {}", execute);
        delete.clear();
    }

    private void insertItems(List<T> items) {
        SQLInsertClause insertClause = sqlQueryFactory.insert(relationalPath);
        for (T item : items) {
            insertClause.populate(item, EntityMapper.DEFAULT).addBatch();
        }

        if(!insertClause.isEmpty()) {
            insertClause.execute();
            insertClause.clear();
        }
    }

    private List<? extends Object> insertItemsWithPath(SQLInsertClause insertClause, List<T> items, Path<?> path) {
        List<?> withKeys = Collections.emptyList();
        for (T item : items) {
            insertClause.populate(item, EntityMapper.DEFAULT).addBatch();
        }

        if(!insertClause.isEmpty()) {
            withKeys = insertClause.executeWithKeys(path);
            insertClause.clear();
        }
        return withKeys;
    }
}
