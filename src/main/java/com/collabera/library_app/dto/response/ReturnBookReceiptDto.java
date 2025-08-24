package com.collabera.library_app.dto.response;

import com.collabera.library_app.model.Loan;

import java.time.LocalDateTime;
import java.util.Objects;

public record ReturnBookReceiptDto(String cardNumber,
                                   String isbn,
                                   LocalDateTime returnedAt) {
    public static ReturnBookReceiptDto fromEntity(Loan savedLoan) {
        if (savedLoan == null) return null;
        return new ReturnBookReceiptDto(
                savedLoan.getMember() != null ? Objects.requireNonNull(savedLoan.getMember()).getCardNumber() : null,
                savedLoan.getMember() != null ? Objects.requireNonNull(savedLoan.getBook()).getBookMetaData().getIsbn() : null,
                savedLoan.getBookedAt()
        );
    }
}
