package com.collabera.library_app.service.impl;

import com.collabera.library_app.dto.LoanBookDto;
import com.collabera.library_app.dto.ReturnBookDto;
import com.collabera.library_app.dto.response.LoanBookReceiptDto;
import com.collabera.library_app.dto.response.ReturnBookReceiptDto;
import com.collabera.library_app.exception.ConflictException;
import com.collabera.library_app.exception.ResourceNotFoundException;
import com.collabera.library_app.model.Book;
import com.collabera.library_app.model.BookMetaData;
import com.collabera.library_app.model.Loan;
import com.collabera.library_app.model.Member;
import com.collabera.library_app.repository.BookMetaDataRepository;
import com.collabera.library_app.repository.BookRepository;
import com.collabera.library_app.repository.LoanRepository;
import com.collabera.library_app.service.LoanService;
import com.collabera.library_app.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {

    private final BookRepository bookRepository;

    private final BookMetaDataRepository bookMetaDataRepository;

    private final LoanRepository loanRepository;

    private final MemberService memberService;

    public LoanServiceImpl(BookRepository bookRepository, BookMetaDataRepository bookMetaDataRepository, LoanRepository loanRepository, MemberService memberService) {
        this.bookRepository = bookRepository;
        this.bookMetaDataRepository = bookMetaDataRepository;
        this.loanRepository = loanRepository;
        this.memberService = memberService;
    }

    /**
     * Creates a new loan record for a given book and member.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Validates that the book exists in the metadata table using its ISBN.</li>
     *   <li>Validates that the member exists in the system by card number.</li>
     *   <li>Ensures the member does not already have an active loan for the same ISBN.</li>
     *   <li>Fetches all physical copies of the book and finds the first available (not currently loaned) copy.</li>
     *   <li>If an available copy is found, creates a new {@link Loan} record with the current timestamp.</li>
     *   <li>Persists the loan record and returns a receipt DTO with loan details.</li>
     * </ol>
     *
     * @param loanBookDto request data containing the member card number and ISBN of the book to loan
     * @return a {@link LoanBookReceiptDto} containing details of the successful loan transaction
     * @throws ResourceNotFoundException if the book ISBN does not exist, the member does not exist,
     *                                   or no copies of the book are found
     * @throws ConflictException         if the member already has an active loan for the same ISBN,
     *                                   or if all copies of the book are currently loaned out
     */
    @Override
    @Transactional
    public LoanBookReceiptDto loanBook(LoanBookDto loanBookDto) {
        // 1. Check if the book exists in the metadata table
        BookMetaData bookMetaData = bookMetaDataRepository.findByIsbn(loanBookDto.isbn()).orElse(null);
        if (bookMetaData == null) {
            throw new ResourceNotFoundException("Book with ISBN " + loanBookDto.isbn() + " does not exist");
        }

        // 2. Check if member exists
        Member member = memberService.getMember(loanBookDto.cardNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Member with ID " + loanBookDto.cardNumber() + " does not exist"));

        // 3. Check if member already has an active loan for same ISBN
        if (loanRepository.existsByMemberAndBook_BookMetaDataIsbnAndReturnedAtIsNull(member, loanBookDto.isbn())) {
            throw new ConflictException("Not allowed to book multiple copies with same ISBN. Only 1 copy per unique ISBN.");
        }

        // 4. Check if book copies exist for this ISBN
        List<Book> copies = bookRepository.findByBookMetaData(bookMetaData);
        if (copies.isEmpty()) {
            throw new ResourceNotFoundException("No copies available for book with ISBN " + loanBookDto.isbn());
        }

        // 4. Find the first available (not loaned) copy
        Optional<Book> availableCopy = copies.stream()
                .filter(copy -> !loanRepository.existsByBookAndReturnedAtIsNull(copy))
                .findFirst();

        if (availableCopy.isEmpty()) {
            throw new ConflictException("All copies of book with ISBN " + loanBookDto.isbn() + " are already loaned out");
        }

        Book bookToLoan = availableCopy.get();

        // 5. Create loan record
        Loan loan = Loan.builder()
                .book(bookToLoan)
                .member(member)
                .bookedAt(LocalDateTime.now())
                .returnedAt(null)
                .build();

        Loan savedLoan = loanRepository.save(loan);

        // 6. Build and return receipt DTO
        return LoanBookReceiptDto.fromEntity(savedLoan);
    }

    /**
     * Processes the return of a borrowed book.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Validates that the member exists in the system.</li>
     *   <li>Finds the active loan for the given member and book ISBN.</li>
     *   <li>Updates the loan record with the return timestamp.</li>
     *   <li>Returns a receipt with the loan return details.</li>
     * </ol>
     *
     * @param returnBookDto request data containing the member card number and ISBN of the book to return
     * @return a {@link ReturnBookReceiptDto} containing details of the return transaction
     * @throws ResourceNotFoundException if the member does not exist or no active loan is found
     * @throws ConflictException         if the book was already returned
     */
    @Override
    @Transactional
    public ReturnBookReceiptDto returnBook(ReturnBookDto returnBookDto) {
        // 1. Fetch member by card number
        Member member = memberService.getMember(returnBookDto.cardNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Member with ID " + returnBookDto.cardNumber() + " does not exist"));

        // 2. Find active loan for this member and book
        Loan activeLoan = loanRepository.findByMemberAndBook_BookMetaDataIsbnAndReturnedAtIsNull(member, returnBookDto.isbn())
                .orElseThrow(() -> new ResourceNotFoundException("No active loan found for member " + returnBookDto.cardNumber() + " and ISBN " + returnBookDto.isbn()));

        if (activeLoan.getReturnedAt() != null) {
            throw new ConflictException("Book was already returned at " + activeLoan.getReturnedAt());
        }

        // 3. Update return time
        activeLoan.setReturnedAt(LocalDateTime.now());
        Loan savedLoan = loanRepository.save(activeLoan);

        return ReturnBookReceiptDto.fromEntity(savedLoan);
    }
}

