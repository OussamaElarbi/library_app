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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "isbn", nullable = false)
    private BookCopy bookCopy;

    // Relationships
    @OneToMany(mappedBy = "book")
    private Set<Loan> loans;
}
