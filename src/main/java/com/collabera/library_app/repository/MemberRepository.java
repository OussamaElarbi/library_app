package com.collabera.library_app.repository;

import com.collabera.library_app.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByCardNumber(String membershipNumber);

    Optional<Member> getByCardNumber(String cardNumber);
}

