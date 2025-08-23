package com.collabera.library_app.repository;

import com.collabera.library_app.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByBookCopyIsbn(String isbn);

    long countByBookCopyIsbn(String isbn);

    boolean existsByBookId(Long id);
}
