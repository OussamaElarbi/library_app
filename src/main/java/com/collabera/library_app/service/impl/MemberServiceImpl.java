package com.collabera.library_app.service.impl;

import com.collabera.library_app.dto.MemberDto;
import com.collabera.library_app.dto.response.RegisterMemberDto;
import com.collabera.library_app.model.Member;
import com.collabera.library_app.repository.MemberRepository;
import com.collabera.library_app.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Registers a new library member if they do not already exist.
     * <p>
     * This method first checks if a member with the given email already exists:
     * - If yes, the existing member's card number is returned without creating a new record.
     * - If no, a unique membership number is generated, and a new member is saved.
     * <p>
     * The membership number is generated in the format: {@code LIB-{year}-{UUID-part}}
     * and uniqueness is guaranteed by checking against the repository before saving.
     *
     * @param memberDto the DTO containing the new member's details (email and name)
     * @return a {@link RegisterMemberDto} containing the member's unique card number
     */
    @Override
    @Transactional
    public RegisterMemberDto registerMember(MemberDto memberDto) {
        Optional<Member> existing = memberRepository.findByEmail(memberDto.email());

        if (existing.isPresent()) {
            return new RegisterMemberDto(existing.get().getCardNumber());
        }

        String membershipNumber;
        do {
            membershipNumber = generateMembershipNumber();
        } while (memberRepository.existsByCardNumber(membershipNumber));

        Member savedMember = Member.builder()
                .email(memberDto.email())
                .name(memberDto.name())
                .cardNumber(membershipNumber)
                .build();

        memberRepository.save(savedMember);
        return new RegisterMemberDto(membershipNumber);
    }

    /**
     * Retrieves a library member by their unique card number.
     * <p>
     * This method queries the {@link MemberRepository} to find a {@link Member}
     * associated with the provided card number. It returns an {@link Optional}
     * that will contain the member if found, or be empty if no member exists
     * with the given card number.
     * </p>
     *
     * @param cardNumber the unique library card number used to identify the member
     *                   (must not be {@code null} or empty)
     * @return an {@link Optional} containing the {@link Member} if found,
     * or an empty {@link Optional} if no member is registered with the given card number
     */
    @Override
    public Optional<Member> getMember(String cardNumber) {
        return memberRepository.getByCardNumber(cardNumber);
    }

    /**
     * Generates a unique library membership number for a user.
     * <p>
     * Format: LIB-<YEAR>-<RANDOM_STRING>
     * Example: LIB-2025-4F7A1B9C
     * <p>
     * - "LIB" is a fixed prefix to indicate library membership.
     * - The current year is appended for easy tracking (e.g., LIB-2025).
     * - A random 8-character alphanumeric string is added for uniqueness.
     */
    private String generateMembershipNumber() {
        return "LIB-" + java.time.Year.now().getValue() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
