package com.collabera.library_app.dto.response;

import com.collabera.library_app.model.Loan;

import java.time.LocalDateTime;
import java.util.Objects;

public record LoanBookReceiptDto(
        String cardNumber,
        String isbn,
        LocalDateTime bookedAt
) {
    public static LoanBookReceiptDto fromEntity(Loan loan) {
        if (loan == null) return null;
        return new LoanBookReceiptDto(
                loan.getMember() != null ? Objects.requireNonNull(loan.getMember()).getCardNumber() : null,
                loan.getMember() != null ? Objects.requireNonNull(loan.getBook()).getBookMetaData().getIsbn() : null,
                loan.getBookedAt()
        );
    }
}