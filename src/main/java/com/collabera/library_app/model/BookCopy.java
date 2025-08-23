package com.collabera.library_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "book_copies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCopy {
    @Id
    @Column(name = "isbn", length = 20)
    private String isbn;

    @Column(nullable = false)
    private String title;

    private String author;

    // One ISBN can be referenced by multiple books
    @OneToMany(mappedBy = "bookCopy")
    private Set<Book> books;

}

