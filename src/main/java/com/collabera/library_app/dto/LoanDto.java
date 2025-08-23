package com.collabera.library_app.dto;

import com.collabera.library_app.model.Loan;

import java.time.LocalDateTime;

public record LoanDto(
        Long loanId,
        Long bookId,
        Long memberId,
        LocalDateTime bookedAt,
        LocalDateTime returnedAt
) {
    public static LoanDto fromEntity(Loan loan) {
        if (loan == null) return null;
        return new LoanDto(
                loan.getId(),
                loan.getBook() != null ? loan.getBook().getId() : null,
                loan.getMember() != null ? loan.getMember().getId() : null,
                loan.getBookedAt(),
                loan.getReturnedAt()
        );
    }
}