package com.entityql.bookstore.repository;

import com.entityql.bookstore.domain.entity.Book;
import com.entityql.bookstore.domain.entity.QBook;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookMatcherRepository {

    private final JPAQueryFactory queryFactory;

    public List<Book> findAllByIds(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException("ids is empty");
        }
        return queryFactory.selectFrom(QBook.book)
                .where(QBook.book.id.in(ids))
                .fetch();
    }
}
