package com.collabera.library_app.api;

import com.collabera.api.MembersApiDelegate;
import com.collabera.library_app.dto.MemberDto;
import com.collabera.library_app.dto.response.RegisterMemberDto;
import com.collabera.library_app.service.MemberService;
import com.collabera.model.CreateMemberRequest;
import com.collabera.model.Member;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

@AllArgsConstructor
@Service
public class MembersApiImpl implements MembersApiDelegate {

    private final MemberService memberService;

    /**
     * POST /members : Register a new member
     *
     * @param createMemberRequest (required)
     * @return Member created successfully (status code 201)
     * or Invalid request payload (status code 400)
     * or Conflict (duplicate card number or email) (status code 409)
     * or Server error (status code 500)
     * @see MembersApi#registerMember
     */
    @Override
    public ResponseEntity<Member> registerMember(CreateMemberRequest createMemberRequest) {
        MemberDto memberDto = new MemberDto(createMemberRequest.getName(), createMemberRequest.getEmail());

        RegisterMemberDto savedMember = memberService.registerMember(memberDto);

        URI location = URI.create("/members/" + savedMember.cardNumber());
        return ResponseEntity.created(location).body(Member.builder().cardNumber(savedMember.cardNumber()).build());
    }
}
