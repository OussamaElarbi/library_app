package com.collabera.library_app.repository;

import com.collabera.library_app.model.BookMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookMetaDataRepository extends JpaRepository<BookMetaData, String> {
    Optional<BookMetaData> findByIsbn(String isbn);
}

