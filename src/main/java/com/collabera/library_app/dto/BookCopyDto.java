package com.collabera.library_app.dto;

import com.collabera.library_app.model.BookCopy;

public record BookCopyDto(
        String isbn,
        String title,
        String author
) {
    public static BookCopyDto fromEntity(BookCopy copy) {
        if (copy == null) return null;
        return new BookCopyDto(copy.getIsbn(), copy.getTitle(), copy.getAuthor());
    }
}