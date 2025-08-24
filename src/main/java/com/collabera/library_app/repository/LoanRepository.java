package com.collabera.library_app.repository;

import com.collabera.library_app.model.Book;
import com.collabera.library_app.model.Loan;
import com.collabera.library_app.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    boolean existsByMemberAndBook_BookMetaDataIsbnAndReturnedAtIsNull(Member member, String isbn);

    boolean existsByBookAndReturnedAtIsNull(Book book);

    // Fetch the single active loan for a member and ISBN
    Optional<Loan> findByMemberAndBook_BookMetaDataIsbnAndReturnedAtIsNull(Member member, String isbn);

    // Useful if you want to detect ambiguity and fail when >1 is present
    List<Loan> findAllByMemberAndBook_BookMetaDataIsbnAndReturnedAtIsNull(Member member, String isbn);

}
