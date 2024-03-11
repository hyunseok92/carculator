package com.example.carculator.member.model.repository;

import com.example.carculator.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    //jpa Query methods
    Optional<Member> findByEmail(String email);

    boolean existsMemberByEmail(String email);

    boolean existsMemberById(String id);

    @Modifying
    @Query("UPDATE Member m Set m.pwd = :cryptedPwd WHERE m.membercode = :membercode")
    void updatePwd(@Param("membercode") String membercode, @Param("cryptedPwd") String cryptedPwd);

    @Modifying
    @Query("UPDATE Member m Set m.pwd = :pwd WHERE m.email = :email")
    void updatePwdByEmail(@Param("pwd") String pwd, @Param("email") String email);

    @Modifying
    @Query("UPDATE Member m Set m.status = :status WHERE m.membercode = :membercode")
    void updateStatus(@Param("membercode") String membercode, @Param("status") String status);

    List<Member> findByGradeNot(String role);

}
