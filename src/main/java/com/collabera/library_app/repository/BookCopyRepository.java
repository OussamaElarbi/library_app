package com.collabera.library_app.repository;

import com.collabera.library_app.model.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookCopyRepository extends JpaRepository<BookCopy, String> {

    Optional<BookCopy> findByIsbn(String isbn);

    List<BookCopy> findByTitleContainingIgnoreCase(String titlePart);

    List<BookCopy> findByAuthorContainingIgnoreCase(String authorPart);

    boolean existsByIsbn(String isbn);
}

