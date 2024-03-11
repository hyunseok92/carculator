package com.example.carculator.member.dto;

import com.example.carculator.enums.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private String membercode;
    private String id;
    private String pwd;
    private String email;
    private String phone;
    private String name;
    private String birth;
    private String gender;
    private String shopname;
    private String picture;
    private String shopaddress;
    private String shopnumber;
    private String grade;
    private String status;
    private OAuthProvider oAuthProvider;
    private String shopphone;
    private String shoprate;
}
