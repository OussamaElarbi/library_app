package com.collabera.library_app.dto;

import com.collabera.library_app.model.Member;

public record MemberDto(
        Long memberId,
        String cardNumber,
        String name,
        String email
) {
    public static MemberDto fromEntity(Member member) {
        if (member == null) return null;
        return new MemberDto(
                member.getId(),
                member.getCardNumber(),
                member.getName(),
                member.getEmail()
        );
    }
}