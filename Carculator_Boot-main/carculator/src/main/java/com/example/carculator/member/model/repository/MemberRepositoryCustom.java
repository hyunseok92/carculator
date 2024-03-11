package com.example.carculator.member.model.repository;

import com.example.carculator.member.dto.MemberDto;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<MemberDto> findMemberByMembercode(String membercode);
}
