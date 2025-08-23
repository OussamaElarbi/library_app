package com.collabera.library_app.dto;

import com.collabera.library_app.model.Book;
import lombok.Builder;

@Builder
public record BookDto(
        Long bookId,
        String isbn,
        String title,
        String author
) {
    public static BookDto fromEntity(Book book) {
        if (book == null) return null;
        return new BookDto(
                book.getId(),
                book.getBookCopy() != null ? book.getBookCopy().getIsbn() : null,
                book.getBookCopy() != null ? book.getBookCopy().getTitle() : null,
                book.getBookCopy() != null ? book.getBookCopy().getAuthor() : null
        );
    }
}