package com.example.carculator.login.controller;

import com.example.carculator.jwt.JwtUtil;
import com.example.carculator.login.dto.LoginRequestDto;
import com.example.carculator.login.dto.UserResponseDto;
import com.example.carculator.login.model.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "로그인", description = "자체 로그인 API")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse res) {
        try {

            UserResponseDto userResponseDto = accountService.defaultLogin(loginRequestDto, res);

            log.info(userResponseDto.toString());

            return ResponseEntity.ok(userResponseDto);

        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 API")
    @GetMapping("/kakaoLogin/{accessToken}")
    public ResponseEntity<?> kakaoLogin(@PathVariable("accessToken") String token, HttpServletResponse res) throws JsonProcessingException {

        UserResponseDto userResponseDto = accountService.kakaoLogin(token, res);

        return ResponseEntity.ok(userResponseDto);

    }

    @Operation(summary = "네이버 로그인", description = "네이버 로그인 API")
    @GetMapping("/naverLogin")
    public ResponseEntity<String> getNaverLoginReqUrl(HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(accountService.getNaverLoginReqUrl(request));
    }

    @Operation(summary = "네이버 로그인 콜백", description = "네이버 로그인 콜백 API")
    @GetMapping("/naverLogin/callbacks")
    public ResponseEntity<?> naverLogin(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletResponse res) throws Exception {

        try {
            UserResponseDto userResponseDto = accountService.naverLogin(code, state, res);
            return ResponseEntity.ok(userResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "깃 로그인 콜백", description = "깃 로그인 콜백 API")
    @GetMapping("/gitLogin/callbacks")
    public ResponseEntity<?> gitLogin(@RequestParam("code") String code, HttpServletResponse res) throws Exception {

        try {
            log.info("code = " + code);
            UserResponseDto userResponseDto = accountService.gitLogin(code, res);
            return ResponseEntity.ok(userResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "구글 로그인 콜백", description = "구글 로그인 콜백 API")
    @GetMapping("/googleLogin/callbacks")
    public ResponseEntity<?> googleLogin(@RequestParam("code") String code, HttpServletResponse res) throws Exception {

        try {
            UserResponseDto userResponseDto = accountService.googleLogin(code, res);
            return ResponseEntity.ok(userResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "토큰 재발급", description = "토큰 재발급 API")
    @GetMapping("/newtoken/{email}") // access token이 만료됐을 경우
    public ResponseEntity<?> issuedToken(@PathVariable("email") String email, HttpServletResponse response) {

        response.addHeader(JwtUtil.ACCESS_TOKEN, JwtUtil.BEARER_PREFIX + " " + jwtUtil.createToken(email, "Access"));

        return ResponseEntity.ok("재발급");
    }
}
