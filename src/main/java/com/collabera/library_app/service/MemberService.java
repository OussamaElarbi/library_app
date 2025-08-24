package com.collabera.library_app.service;

import com.collabera.library_app.dto.MemberDto;
import com.collabera.library_app.dto.response.RegisterMemberDto;
import com.collabera.library_app.model.Member;

import java.util.Optional;

public interface MemberService {
    RegisterMemberDto registerMember(MemberDto memberDto);
    Optional<Member> getMember(String cardNumber);
}
