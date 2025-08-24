package com.collabera.library_app.api;

import com.collabera.api.BooksApi;
import com.collabera.api.BooksApiDelegate;
import com.collabera.library_app.dto.BookDto;
import com.collabera.library_app.dto.ListBooksDto;
import com.collabera.library_app.dto.response.BookSummaryDto;
import com.collabera.library_app.service.BookService;
import com.collabera.model.Book;
import com.collabera.model.BookListResponse;
import com.collabera.model.BookListResponsePage;
import com.collabera.model.CreateBookRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@Service
public class BooksApiImpl implements BooksApiDelegate {

    private final BookService bookService;

    /**
     * GET /books : Get a list of all books in the library
     *
     * @param page Zero-based page index (optional, default to 0)
     * @param size Page size (optional, default to 20)
     * @return A list of books (status code 200)
     * or Invalid query parameters (status code 400)
     * or Server error (status code 500)
     * @see BooksApi#listBooks
     */
    @Override
    public ResponseEntity<BookListResponse> listBooks(Integer page, Integer size) {
        ListBooksDto listBooksDto = ListBooksDto.builder().page(page).size(size).build();

        Page<BookSummaryDto> bookSummaryPage = bookService.listBooks(listBooksDto);

        List<Book> bookList = bookSummaryPage.stream()
                .map(bookSummaryDto -> {
                    com.collabera.model.Book bookModel = new com.collabera.model.Book();
                    bookModel.setIsbn(bookSummaryDto.getIsbn());
                    bookModel.setTitle(bookSummaryDto.getTitle());
                    bookModel.setAuthor(bookSummaryDto.getAuthor());
                    bookModel.setTotalCopies(bookSummaryDto.getTotalCopies());
                    return bookModel;
                }).toList();

        BookListResponse response = new BookListResponse();
        response.setItems(bookList);

        BookListResponsePage bookListResponsePage = new BookListResponsePage(page, size, Math.toIntExact(bookSummaryPage.getTotalElements()), bookSummaryPage.getTotalPages());
        response.setPage(bookListResponsePage);

        return ResponseEntity.ok(response);
    }

    /**
     * POST /books : Register a new book to the library
     *
     * @param createBookRequest (required)
     * @return Book created successfully (status code 201)
     * or Invalid request payload (status code 400)
     * or Server error (status code 500)
     * @see BooksApi#registerBook
     */
    @Override
    public ResponseEntity<Book> registerBook(CreateBookRequest createBookRequest) {
        BookDto bookDto = BookDto.builder().isbn(createBookRequest.getIsbn())
                .title(createBookRequest.getTitle())
                .author(createBookRequest.getAuthor())
                .copies(createBookRequest.getTotalCopies())
                .build();

        BookDto savedBookDto = bookService.registerBook(bookDto);
        Book book = Book.builder()
                .isbn(savedBookDto.isbn())
                .author(savedBookDto.author())
                .title(savedBookDto.title())
                .totalCopies(savedBookDto.copies())
                .build();

        URI location = URI.create("/books/" + savedBookDto.isbn());
        return ResponseEntity.created(location).body(book);
    }


}
