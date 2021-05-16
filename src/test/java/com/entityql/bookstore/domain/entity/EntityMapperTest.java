package com.entityql.bookstore.domain.entity;


import com.entityql.config.EntityMapper;
import com.querydsl.core.types.Path;
import com.querydsl.sql.dml.BeanMapper;
import com.querydsl.sql.types.Null;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static com.entityql.domain.entity.EBook.qBook;
import static org.assertj.core.api.Assertions.assertThat;

public class EntityMapperTest {

    @Test
    void notSearchPropertyIfUsingBeanMapper() {
        LocalDateTime createDate = LocalDateTime.of(2021, 5, 15, 3, 4);
        Book book = Book.builder()
                .name("java")
                .createdDate(createDate)
                .build();

        BeanMapper mapper = BeanMapper.DEFAULT;
        Map<Path<?>, Object> map = mapper.createMap(qBook, book);

        assertThat(map.containsValue(createDate)).isFalse();
    }

    @Test
    void searchPropertyIfUsingBeanMapper() {
        LocalDateTime createDate = LocalDateTime.of(2021, 5, 15, 3, 4);
        Book book = Book.builder()
                .name("java")
                .createdDate(createDate)
                .build();

        Map<Path<?>, Object> map = EntityMapper.DEFAULT.createMap(qBook, book);

        assertThat(map).containsValue(createDate);
    }

    @Test
    void nullBindingIfUsingBeanMapper() {
        LocalDateTime createDate = LocalDateTime.of(2021, 5, 15, 3, 4);
        Book book = Book.builder()
                .name("java")
                .createdDate(createDate)
                .build();

        Map<Path<?>, Object> map = EntityMapper.WITH_NULL_BINDINGS.createMap(qBook, book);
        assertThat(map).containsValue(createDate).containsValue(Null.DEFAULT);
    }
}