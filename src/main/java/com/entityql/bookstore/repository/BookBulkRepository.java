package com.entityql.bookstore.repository;

import com.entityql.common.repository.BulkRepository;
import com.entityql.domain.entity.EBook;
import com.querydsl.sql.SQLQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Repository
@Transactional
public class BookBulkRepository<Book> extends BulkRepository<Book> {

    private final BookMatcherRepository bookMatcherRepository;

    public BookBulkRepository(SQLQueryFactory sqlQueryFactory, BookMatcherRepository bookMatcherRepository) {
        super(sqlQueryFactory, EBook.qBook);
        this.bookMatcherRepository = bookMatcherRepository;
    }
}
