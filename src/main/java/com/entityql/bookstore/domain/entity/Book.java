package com.entityql.bookstore.domain.entity;

import com.entityql.common.enums.PublicStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "book",
    indexes = {
        @Index(name = "idx_isbn_1", columnList = "isbn")
    }
)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "isbn")
    private Long isbn;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PublicStatus status;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Builder
    public Book(String name, Long isbn, PublicStatus status, LocalDateTime createdDate) {
        this.name = name;
        this.isbn = isbn;
        this.status = status;
        this.createdDate = createdDate;
    }
}
