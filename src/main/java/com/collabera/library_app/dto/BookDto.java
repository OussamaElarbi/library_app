package com.collabera.library_app.dto;

import lombok.Builder;

@Builder
public record BookDto(
        String isbn,
        String title,
        String author,
        int copies
) {
}