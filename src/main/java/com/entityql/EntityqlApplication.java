package com.entityql;

import com.entityql.bookstore.repository.BookBulkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EntityqlApplication {
    public static void main(String[] args) {
        SpringApplication.run(EntityqlApplication.class, args);
    }

    @Autowired
    private BookBulkRepository bookBulkRepository;
}
