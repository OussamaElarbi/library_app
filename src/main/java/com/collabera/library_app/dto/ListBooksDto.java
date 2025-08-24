package com.collabera.library_app.dto;

import lombok.Builder;

@Builder
public record ListBooksDto(
        int page,
        int size
) {
}