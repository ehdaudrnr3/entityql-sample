package com.entityql.store.repository;

import com.entityql.store.domain.entity.Store;
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
public class StoreBulkRepositoryTest {

    @Autowired
    private StoreBulkRepository storeBulkRepository;

    List<Store> stores;
    StopWatch stopWatch;

    @BeforeEach
    void setup() {
        stopWatch = new StopWatch();
        stores = IntStream.range(1, 100_0000).boxed()
                .map(i-> Store.builder()
                        .name("name" + i)
                        .build())
                .collect(Collectors.toList());
    }

    @AfterEach
    void after() {
        storeBulkRepository.batchDeleteAll();
    }

    @Test
    void bulkTest() {
        stopWatch.start("store bulk start");
        storeBulkRepository.batchInsert(stores);
        stopWatch.stop();
        System.out.println("total time seconds : " + stopWatch.getTotalTimeSeconds());
        System.out.println(stopWatch.prettyPrint());
    }

}
