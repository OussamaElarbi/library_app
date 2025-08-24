package com.collabera.library_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
    @SequenceGenerator(name = "book_seq", sequenceName = "book_seq", allocationSize = 1000)
    @Column(name = "book_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "isbn", nullable = false)
    private BookMetaData bookMetaData;

    // Relationships
    @OneToMany(mappedBy = "book")
    private Set<Loan> loans;
}
