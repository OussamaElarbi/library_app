package com.collabera.library_app.dto.response;

public interface BookSummaryDto {
    String getIsbn();

    String getTitle();

    String getAuthor();

    int getTotalCopies();
}