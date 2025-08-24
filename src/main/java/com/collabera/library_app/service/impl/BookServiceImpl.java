package com.collabera.library_app.service.impl;

import com.collabera.library_app.dto.BookDto;
import com.collabera.library_app.dto.ListBooksDto;
import com.collabera.library_app.dto.LoanBookDto;
import com.collabera.library_app.dto.response.BookSummaryDto;
import com.collabera.library_app.dto.response.LoanBookReceiptDto;
import com.collabera.library_app.exception.ConflictException;
import com.collabera.library_app.exception.ResourceNotFoundException;
import com.collabera.library_app.model.Book;
import com.collabera.library_app.model.BookMetaData;
import com.collabera.library_app.model.Loan;
import com.collabera.library_app.model.Member;
import com.collabera.library_app.repository.BookMetaDataRepository;
import com.collabera.library_app.repository.BookRepository;
import com.collabera.library_app.repository.LoanRepository;
import com.collabera.library_app.service.BookService;
import com.collabera.library_app.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMetaDataRepository bookMetaDataRepository;

    private final LoanRepository loanRepository;

    private final MemberService memberService;

    public BookServiceImpl(BookRepository bookRepository, BookMetaDataRepository bookMetaDataRepository, LoanRepository loanRepository, MemberService memberService) {
        this.bookRepository = bookRepository;
        this.bookMetaDataRepository = bookMetaDataRepository;
        this.loanRepository = loanRepository;
        this.memberService = memberService;
    }

    /**
     * Registers a new book along with its metadata and physical copies in the system.
     *
     * <p>This method performs the following steps:
     * <ul>
     *   <li>Saves the provided book metadata (ISBN, title, author) into the database.</li>
     *   <li>Creates the requested number of book copies, each linked to the saved metadata.</li>
     *   <li>Persists all book copies in bulk.</li>
     * </ul>
     * The operation is executed within a transaction, ensuring that metadata and copies
     * are saved consistently.</p>
     *
     * @param bookDto the {@link BookDto} containing book details and the number of copies to register
     * @return the same {@link BookDto} passed as input, representing the registered book information
     */
    @Override
    @Transactional
    public BookDto registerBook(BookDto bookDto) {
        BookMetaData bookMetaData = BookMetaData.builder().isbn(bookDto.isbn())
                .title(bookDto.title())
                .author(bookDto.author())
                .build();

        BookMetaData savedMetaData = bookMetaDataRepository.save(bookMetaData);

        List<Book> copies = new ArrayList<>();
        for (int i = 0; i < bookDto.copies(); i++) {
            copies.add(Book.builder()
                    .bookMetaData(savedMetaData)
                    .build());
        }

        bookRepository.saveAll(copies);
        return bookDto;
    }

    /**
     * Retrieves a paginated list of book summaries.
     *
     * <p>This method uses the provided {@link ListBooksDto} to build a {@link Pageable} object
     * (converting the requested page to zero-based index) and queries the repository for
     * a lightweight projection of books.</p>
     *
     * @param listBooksDto contains pagination parameters such as page number and page size
     * @return a {@link Page} of {@link BookSummaryDto} containing summarized book information
     */
    @Override
    public Page<BookSummaryDto> listBooks(ListBooksDto listBooksDto) {
        Pageable pageable = Pageable.ofSize(listBooksDto.size()).withPage(listBooksDto.page() - 1);
        return bookRepository.findBookSummary(pageable);
    }
}

