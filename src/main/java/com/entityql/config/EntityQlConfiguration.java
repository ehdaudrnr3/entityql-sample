package com.entityql.config;

import com.entityql.bookstore.domain.entity.Book;
import com.entityql.common.enums.PublicStatus;
import com.querydsl.sql.H2Templates;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import com.querydsl.sql.types.DateTimeType;
import com.querydsl.sql.types.LocalDateTimeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.exsio.querydsl.entityql.config.EntityQlQueryFactory;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class EntityQlConfiguration {

    @Bean
    @Profile("test")
    public SQLTemplates h2SqlTemplate() {
        return new H2Templates();
    }

    @Bean
    @Profile("local")
    public SQLTemplates mysqlSqlTemplate() {
        return new MySQLTemplates();
    }

    @Bean
    public SQLQueryFactory sqlQueryFactory(DataSource dataSource, SQLTemplates sqlTemplates) {
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(sqlTemplates);
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
        configuration.register(new DateTimeType());
        configuration.register(new LocalDateTimeType());

        return new EntityQlQueryFactory(configuration, dataSource)
                .registerEnumsByName(PublicStatus.class.getPackageName());
    }
}
