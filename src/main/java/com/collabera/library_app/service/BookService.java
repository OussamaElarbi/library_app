package com.collabera.library_app.service;

import com.collabera.library_app.dto.BookDto;
import com.collabera.library_app.dto.ListBooksDto;
import com.collabera.library_app.dto.response.BookSummaryDto;
import org.springframework.data.domain.Page;

public interface BookService {
    BookDto registerBook(BookDto bookDto);
    Page<BookSummaryDto> listBooks(ListBooksDto listBooksDto);
}
