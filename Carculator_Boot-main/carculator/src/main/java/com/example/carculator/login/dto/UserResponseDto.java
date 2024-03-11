package com.example.carculator.login.dto;

import com.example.carculator.member.model.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDto {

    private String membercode;

    private String userName;

    private String userEmail;

    private String role;

    public UserResponseDto(Member member) {
        this.membercode = member.getMembercode();
        this.userName = member.getName();
        this.userEmail = member.getEmail();
        this.role = member.getGrade();
    }
}
