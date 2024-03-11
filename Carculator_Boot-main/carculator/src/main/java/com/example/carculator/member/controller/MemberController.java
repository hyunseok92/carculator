package com.example.carculator.member.controller;

import com.example.carculator.login.model.AccountService;
import com.example.carculator.member.dto.MemberDto;
import com.example.carculator.member.model.entity.Member;
import com.example.carculator.member.model.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RestController
public class MemberController {

    private final MemberService memberService;

    private final AccountService accountService;


    //회원 등록
    @Operation(summary = "회원가입", description = "회원가입 API")
    @PostMapping("/join")
    public ResponseEntity<MemberDto> insertMember(@RequestBody MemberDto memberDto) {

        try {
            return new ResponseEntity<>(memberService.insertMember(memberDto), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 등록된 이메일 주소입니다");
        }

    }

    //회원 리스트 조회
    @GetMapping("/member/adminSelect")
    public ResponseEntity<List<Member>> memberList() throws  Exception{
        return new ResponseEntity<>(memberService.memberList(), HttpStatus.OK);
    }

    //코드로 회원 조회
    @GetMapping("/member/{membercode}")
    public ResponseEntity<Optional<MemberDto>> selectMemberByCode(@PathVariable("membercode") String membercode) throws Exception {
        Optional<MemberDto> member = memberService.findMemberByMembercode(membercode);
        log.info("\n\n\n\nMember에 들어있는 것 : " + member);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    // 코드로 상태 변경
    @PostMapping("/member/status")
    public ResponseEntity<?> updateStatus(@RequestBody String membercode) throws  Exception {
        try {
            memberService.updateStatus(membercode);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //정비소 회원 상태 변경
    @PostMapping("/member/shopStatus")
    public ResponseEntity<?> updateShopStatus(@RequestBody MemberDto memberDto) throws  Exception {
        try {
            memberService.updateShopStatus(memberDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    // 정비소 회원의 정비소 정보 변경
    @PatchMapping("/member/modify-shop")
    public ResponseEntity<?> modifyShopInfo(@RequestBody MemberDto memberDto) throws Exception {
        try {
            memberService.modifyShopInfo(memberDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //비밀번호 변경
    @PostMapping("/changePwd")
    public ResponseEntity<?> changePwd(@RequestBody MemberDto memberDto) throws Exception {
        try {
            memberService.updatePasswordByEmail(memberDto);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
         return new ResponseEntity<>(HttpStatus.OK);
    }

    // 비밀번호 변경
    @PatchMapping("/member")
    public ResponseEntity<?> updatePassword(@RequestBody MemberDto memberDto) throws Exception {
        try {
            // 현재 비밀번호와 같은 비밀번호라면 Exception
            accountService.checkFormerPwd(memberDto);
            // 위 코드를 통과했다면 서비스 호출
            memberService.updatePassword(memberDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // 비밀번호 변경페이지 비밀번호 확인
    @PostMapping("/member/check-pw")
    public ResponseEntity<?> checkPwd(@RequestBody MemberDto memberDto) throws Exception {
        log.info("\n\n\nController here.\nmembercode : " + memberDto.getMembercode() + "\npassword : " + memberDto.getPwd());
        try {
            accountService.checkCurrentPwd(memberDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}