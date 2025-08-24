package com.collabera.library_app.api;

import com.collabera.api.LoansApiDelegate;
import com.collabera.api.ReturnApiDelegate;
import com.collabera.library_app.dto.LoanBookDto;
import com.collabera.library_app.dto.ReturnBookDto;
import com.collabera.library_app.dto.response.LoanBookReceiptDto;
import com.collabera.library_app.dto.response.ReturnBookReceiptDto;
import com.collabera.library_app.service.LoanService;
import com.collabera.model.LoanBookRequest;
import com.collabera.model.LoanReceipt;
import com.collabera.model.ReturnBookRequest;
import com.collabera.model.ReturnReceipt;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.time.ZoneOffset;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LoanApiImpl implements LoansApiDelegate, ReturnApiDelegate {

    private final LoanService loanService;

    /**
     * @return
     */
    @Override
    public Optional<NativeWebRequest> getRequest() {
        return LoansApiDelegate.super.getRequest();
    }

    /**
     * POST /loans : Loan a book to a member
     *
     * @param loanBookRequest (required)
     * @return Book loan created (status code 201)
     * or Invalid request payload (status code 400)
     * or Book or member not found (status code 404)
     * or Conflict (no available copies or already loaned constraints) (status code 409)
     * or Server error (status code 500)
     * @see LoansApi#loanBook
     */
    @Override
    public ResponseEntity<LoanReceipt> loanBook(LoanBookRequest loanBookRequest) {
        LoanBookDto loanBookDto = new LoanBookDto(loanBookRequest.getIsbn(), loanBookRequest.getCardNumber());

        LoanBookReceiptDto loanBookReceiptDto = loanService.loanBook(loanBookDto);

        LoanReceipt loanReceipt = LoanReceipt.builder().isbn(loanBookReceiptDto.isbn())
                .cardNumber(loanBookReceiptDto.cardNumber())
                .bookedAt(loanBookReceiptDto.bookedAt().atOffset(ZoneOffset.ofHours(8)))
                .build();

        URI location = URI.create("/books/" + loanBookReceiptDto.isbn() + "/loans");
        return ResponseEntity.created(location).body(loanReceipt);
    }


    /**
     * PATCH /return : Return a book
     * Returns a borrowed book. The request must include the member card number and the book ISBN.
     *
     * @param returnBookRequest (required)
     * @return Book successfully returned (status code 200)
     * or Invalid request payload (status code 400)
     * or Active loan not found for given cardNumber and isbn (status code 404)
     * or Conflict (already returned) (status code 409)
     * or Server error (status code 500)
     * @see ReturnApi#returnBook
     */
    @Override
    public ResponseEntity<ReturnReceipt> returnBook(ReturnBookRequest returnBookRequest) {
        ReturnBookDto returnBookDto = new ReturnBookDto(
                returnBookRequest.getIsbn(),
                returnBookRequest.getCardNumber()
        );

        ReturnBookReceiptDto returnBookReceiptDto = loanService.returnBook(returnBookDto);

        ReturnReceipt returnReceipt = ReturnReceipt.builder()
                .isbn(returnBookReceiptDto.isbn())
                .cardNumber(returnBookReceiptDto.cardNumber())
                .returnedAt(returnBookReceiptDto.returnedAt().atOffset(ZoneOffset.ofHours(8)))
                .build();

        return ResponseEntity.ok(returnReceipt);
    }
}
