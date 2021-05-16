package com.entityql.bookstore.repository;

import com.entityql.bookstore.domain.entity.Book;
import com.entityql.common.enums.PublicStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class BookBulkRepositoryTest {

    @Autowired
    private BookBulkRepository bookBulkRepository;

    @Autowired
    private BookRepository bookRepository;

    List<Book> books;
    StopWatch stopWatch;

    @BeforeEach
    void setup() {
        stopWatch = new StopWatch();
       books = IntStream.range(1, 1_000).boxed()
               .map(i-> Book.builder()
                       .name("name" + i)
                       .isbn(Long.valueOf(i*100))
                       .status(PublicStatus.WAIT)
                       .build())
               .collect(Collectors.toList());
    }

    @AfterEach
    void after() {
        bookBulkRepository.batchDeleteAll();
    }

    @Test
    void jpaSaveAllTest() {
        stopWatch.start("jpa save start");
        bookRepository.saveAll(books);
        stopWatch.stop();
        System.out.println("total time seconds : " + stopWatch.getTotalTimeSeconds());
        System.out.println(stopWatch.prettyPrint());
    }

    @Test
    void bulkTest() {
        stopWatch.start("bulk start");
        bookBulkRepository.batchInsert(books);
        stopWatch.stop();
        System.out.println("total time seconds : " + stopWatch.getTotalTimeSeconds());
        System.out.println(stopWatch.prettyPrint());
    }

    @Test
    void noExecuteWithEmpty() {
        bookBulkRepository.batchInsert(new ArrayList<>());
    }
}
