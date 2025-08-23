package com.collabera.library_app.repository;

import com.collabera.library_app.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByMemberId(Long memberId);

    List<Loan> findByBookId(Long bookId);

    List<Loan> findByMemberIdAndReturnedAtIsNull(Long memberId);

    Optional<Loan> findByBookIdAndReturnedAtIsNull(Long bookId);

    boolean existsByMemberIdAndBookIdAndReturnedAtIsNull(Long memberId, Long bookId);

    long countByMemberIdAndReturnedAtIsNull(Long memberId);
}
