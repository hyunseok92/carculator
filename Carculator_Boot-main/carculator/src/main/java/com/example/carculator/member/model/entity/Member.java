package com.example.carculator.member.model.entity;

import com.example.carculator.enums.OAuthProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="member")
public class Member {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "membercode", updatable = false, nullable = false)
    private String membercode;

    //소셜 아이디
    @Column(name="id")
    private String id;

    @Column(name="pwd")
    private String pwd;

    @Email
    @Column(name="email")
    private String email;

    @Column(name="phone")
    private String phone;

    @Column(name="name")
    private String name;

    @Column(name="birth")
    private String birth;

    @Column(name="gender")
    private String gender;

    @Column(name="shopname")
    private String shopname;

    @Column(name="picture")
    private String picture;

    @Column(name="shopaddress")
    private String shopaddress;

    @Column(name="shopnumber")
    private String shopnumber;

    @Column(name="grade")
    private String grade;

    @Column(name="status")
    private String status;

    @Column(name="logintype")
    private OAuthProvider oAuthProvider;

    @Column(name="shopphone")
    private String shopphone;

    @Column(name="shoprate")
    private String shoprate;

}
