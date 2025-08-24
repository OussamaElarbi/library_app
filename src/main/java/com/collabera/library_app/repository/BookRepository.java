package com.collabera.library_app.repository;

import com.collabera.library_app.dto.response.BookSummaryDto;
import com.collabera.library_app.model.Book;
import com.collabera.library_app.model.BookMetaData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("""
            SELECT b.bookMetaData.isbn AS isbn,
                   b.bookMetaData.title AS title,
                   b.bookMetaData.author AS author,
                   COUNT(b) AS totalCopies
            FROM Book b
            GROUP BY b.bookMetaData.isbn, b.bookMetaData.title, b.bookMetaData.author
            ORDER BY b.bookMetaData.title
            """)
    Page<BookSummaryDto> findBookSummary(@NonNull Pageable pageable);

    List<Book> findByBookMetaData(BookMetaData bookMetaData);
}
