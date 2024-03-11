package com.example.carculator.login.model;

import com.example.carculator.auth.PrincipalDetails;
import com.example.carculator.enums.OAuthProvider;
import com.example.carculator.jwt.JwtUtil;
import com.example.carculator.jwt.TokenDto;
import com.example.carculator.login.dto.GoogleOAuthResponseDto;
import com.example.carculator.login.dto.LoginRequestDto;
import com.example.carculator.login.dto.UserResponseDto;
import com.example.carculator.member.dto.MemberDto;
import com.example.carculator.member.model.entity.Member;
import com.example.carculator.member.model.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtUtil jwtUtil;

    //네이버
    @Value("${oauth.naver.client_id}")
    private String naverClientId;

    @Value("${oauth.naver.client_secret}")
    private String naverClientSecret;

    @Value("${oauth.naver.redirect_uri}")
    private String naverRedirectUri;


    //깃
    @Value("${oauth.git.client_id}")
    private String gitClientId;

    @Value("${oauth.git.client_secret}")
    private String gitClientSecret;

    //구글
    @Value("${oauth.google.client_id}")
    private String googleClientId;

    @Value("${oauth.google.client_secret}")
    private String googleClientSecret;

    @Value("${oauth.google.redirect_uri}")
    private String googleRedirectUri;


    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

    // 비밀번호 변경시 기존 비밀번호 확인
    public void checkFormerPwd(MemberDto memberDto) throws Exception {
        //비밀번호 확인
        Member memberEntity = memberRepository.findById(memberDto.getMembercode())
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        if (bCryptPasswordEncoder.matches(memberDto.getPwd(), memberEntity.getPwd())) {
            throw new BadCredentialsException("현재 비밀번호와 다른 비밀번호를 사용해야합니다.");
        }
    }

    public void checkCurrentPwd(MemberDto memberDto) throws Exception {
        //비밀번호 확인
        Member memberEntity = memberRepository.findById(memberDto.getMembercode())
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        if (!bCryptPasswordEncoder.matches(memberDto.getPwd(), memberEntity.getPwd())) {
            throw new BadCredentialsException("비밀번호가 맞지 않습니다.");
        }
    }

    public UserResponseDto defaultLogin(LoginRequestDto loginRequestDto, HttpServletResponse res) {

        try {
            //존재 여부 확인
            Member memberEntity = memberRepository.findByEmail(loginRequestDto.getUserEmail())
                    .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

            //비밀번호 확인
            if (!bCryptPasswordEncoder.matches(loginRequestDto.getUserPassword(), memberEntity.getPwd())) {
                throw new BadCredentialsException("비밀번호가 맞지 않습니다.");
            }

            if (memberEntity.getStatus().equals("1")) {
                throw new BadCredentialsException("정지된 회원입니다");
            } else if (memberEntity.getStatus().equals("2")) {
                throw new BadCredentialsException("승인 대기중인 회원입니다");
            }

            TokenDto tokenDto = jwtUtil.createAllToken(memberEntity.getEmail());
            setHeader(res, tokenDto);

            return new UserResponseDto(memberEntity);

        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    public UserResponseDto kakaoLogin(String accessToken, HttpServletResponse res) throws JsonProcessingException {

        // 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        MemberDto memberDto = getKakaoUserInfo(accessToken);

        // 3. "카카오 사용자 정보"로 필요시 회원가입
        Member member = insertSocialMember(memberDto);

        // 4. 강제 로그인 처리
        forceLogin(member);

        TokenDto tokenDto = jwtUtil.createAllToken(member.getEmail());
        setHeader(res, tokenDto);

        return new UserResponseDto(member);
    }

    public UserResponseDto gitLogin(String code, HttpServletResponse res) throws Exception {

        MemberDto memberDto = getGitUserInfo(code);

        if (memberDto == null) {
            throw new BadRequestException("회원 가입 실패");
        }

        Member member = insertSocialMember(memberDto);

        // 4. 강제 로그인 처리
        forceLogin(member);

        TokenDto tokenDto = jwtUtil.createAllToken(member.getEmail());
        setHeader(res, tokenDto);

        return new UserResponseDto(member);
    }

    private void forceLogin(Member member) {
        PrincipalDetails principalDetails = new PrincipalDetails(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private MemberDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();

        String name = jsonNode.get("kakao_account")
                .get("name").asText();

        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        String gender = jsonNode.get("kakao_account")
                .get("gender").asText();

        if ("male".equals(gender)) {
            gender = "남자";
        } else if ("female".equals(gender)) {
            gender = "여자";
        }

        JsonNode kakaoAccountNode = jsonNode.get("kakao_account");
        String birthdate = kakaoAccountNode.has("birthdate") ? kakaoAccountNode.get("birthdate").asText() : "";

        String[] makePhonenumber = jsonNode.get("kakao_account")
                .get("phone_number").asText().split(" ");
        String phone_number = "0" + makePhonenumber[1];

        String pwd = bCryptPasswordEncoder.encode(UUID.randomUUID().toString());

        return MemberDto.builder()
                .pwd(pwd)
                .id(id.toString())
                .name(name)
                .email(email)
                .gender(gender)
                .birth(birthdate)
                .phone(phone_number)
                .grade("ROLE_USER")
                .oAuthProvider(OAuthProvider.KAKAO)
                .build();
    }

    public Member insertSocialMember(MemberDto memberDto) {

        boolean existEmail = memberRepository.existsMemberByEmail(memberDto.getEmail());
        
        //등록된 이메일이 있으면 그냥 로그인만 회원 정보는 안받음
        if (existEmail) {
            return memberRepository.findByEmail(memberDto.getEmail()).orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        } else {

            Member memberEntity = Member.builder()
                    .membercode(memberDto.getMembercode())
                    .pwd(memberDto.getPwd())
                    .phone(memberDto.getPhone())
                    .email(memberDto.getEmail())
                    .name(memberDto.getName())
                    .birth(memberDto.getBirth())
                    .gender(memberDto.getGender())
                    .shopname(memberDto.getShopname())
                    .picture(memberDto.getPicture())
                    .shopaddress(memberDto.getShopaddress())
                    .shopnumber(memberDto.getShopnumber())
                    .grade(memberDto.getGrade())
                    .status(memberDto.getStatus())
                    .oAuthProvider(memberDto.getOAuthProvider())
                    .id(memberDto.getId())
                    .build();

            memberEntity.setStatus("0");

            Member savedMember = memberRepository.save(memberEntity);

            return savedMember;
        }
    }

    public UserResponseDto naverLogin(String code, String state, HttpServletResponse res) throws IOException {

        MemberDto memberDto = getNaverUserInfo(code, state);

        Member member = insertSocialMember(memberDto);

        // 4. 강제 로그인 처리
        forceLogin(member);

        TokenDto tokenDto = jwtUtil.createAllToken(member.getEmail());
        setHeader(res, tokenDto);

        return new UserResponseDto(member);
    }

    // 네이버에 요청해서 회원정보 받는 메소드
    public MemberDto getNaverUserInfo(String code, String state) throws IOException {

        String codeReqURL = "https://nid.naver.com/oauth2.0/token";
        String tokenReqURL = "https://openapi.naver.com/v1/nid/me";

        // 코드를 네이버에 전달하여 엑세스 토큰 가져옴
        JsonElement tokenElement = jsonElement(codeReqURL, null, code, state);

        String access_Token = tokenElement.getAsJsonObject().get("access_token").getAsString();
        String refresh_token = tokenElement.getAsJsonObject().get("refresh_token").getAsString();

        // 엑세스 토큰을 네이버에 전달하여 유저정보 가져옴
        JsonElement userInfoElement = jsonElement(tokenReqURL, access_Token, null, null);

        String naverId = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("id"));
        String userEmail = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("email"));
        String userName = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("name"));
        String gender = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("gender"));
        String birthyear = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("birthyear"));
        String birthday = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("birthday"));
        String mobile = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("mobile"));


        String pwd = bCryptPasswordEncoder.encode(UUID.randomUUID().toString());

        naverId = naverId.substring(1, naverId.length() - 1);
        userEmail = userEmail.substring(1, userEmail.length() - 1);
        userName = userName.substring(1, userName.length() - 1);
        gender = gender.substring(1, gender.length() - 1).equals("M") ? "남자" : "여자";
        String birthdate = birthyear.substring(1, birthyear.length() - 1) + (birthday.substring(1, birthday.length() - 1)).replaceAll("-", "");
        mobile = (mobile.substring(1, mobile.length() - 1));

        return MemberDto.builder()
                .pwd(pwd)
                .id(naverId)
                .name(userName)
                .email(userEmail)
                .gender(gender)
                .birth(birthdate)
                .phone(mobile)
                .grade("ROLE_USER")
                .oAuthProvider(OAuthProvider.NAVER)
                .build();
    }

    // 네이버에 요청해서 데이터 전달 받는 메소드
    public JsonElement jsonElement(String reqURL, String token, String code, String state) throws IOException {

        // 요청하는 URL 설정
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // POST 요청을 위해 기본값이 false인 setDoOutput을 true로
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        // POST 요청에 필요한 데이터 저장 후 전송
        if (token == null) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + naverClientId +
                    "&client_secret=" + naverClientSecret +
                    "&redirect_uri=" + naverRedirectUri +
                    "&code=" + code +
                    "&state=" + state;
            bw.write(sb);
            bw.flush();
            bw.close();
        } else {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

        // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();

        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        br.close();

        // Gson 라이브러리에 포함된 클래스로 JSON 파싱
        return JsonParser.parseString(result.toString());
    }

    //네이버 로그인
    public String getNaverLoginReqUrl(HttpServletRequest request) {
        String clientId = naverClientId;
        String redirectUri = naverRedirectUri;
        String state = generateState();

        String reqUrl = "https://nid.naver.com/oauth2.0/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&state=" + state;

        request.getSession().setAttribute("state", state);

        return reqUrl;
    }

    public String generateState() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }


    //깃 유저 정보
    public MemberDto getGitUserInfo(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String codeReqURL = "https://github.com/login/oauth/access_token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", gitClientId);
        map.add("client_secret", gitClientSecret);
        map.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            String responseString = restTemplate.postForObject(codeReqURL, request, String.class);
            Map<String, String> responseMap = parseQueryString(responseString);
            String accessToken = responseMap.get("access_token");
            log.info("access_token : " + accessToken);


            return getGitUserByToken(accessToken);

        } catch (Exception e) {
            log.error("error =", e);
        }
        return null;
    }

    private Map<String, String> parseQueryString(String query) {
        Map<String, String> queryPairs = new LinkedHashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            queryPairs.put(URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8), URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
        }
        return queryPairs;
    }

    public MemberDto getGitUserByToken(String accessToken) {

        RestTemplate restTemplate = new RestTemplate();
        String userInfoUrl = "https://api.github.com/user";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken); // Bearer 토큰 설정

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        try {
            // GitHub 사용자 정보 요청
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    userInfoUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            Map<String, Object> userInfo = response.getBody();

            log.info(userInfo.toString());
            String id = userInfo.get("id").toString();
            String name = null;
            Object nameValue = userInfo.get("name");

            if (nameValue == null) {
                name = userInfo.get("login").toString();
            } else {
                name = userInfo.get("name").toString();
            }

            String email = userInfo.get("email").toString();
            String pwd = bCryptPasswordEncoder.encode(UUID.randomUUID().toString());
            String gender = "비공개";

            return MemberDto.builder()
                    .id(id)
                    .pwd(pwd)
                    .name(name)
                    .email(email)
                    .gender(gender)
                    .grade("ROLE_USER")
                    .oAuthProvider(OAuthProvider.GIT)
                    .build();

        } catch (Exception e) {
            log.error("Error fetching GitHub user info: ", e);
            return null;
        }
    }

    public UserResponseDto googleLogin(String code, HttpServletResponse res) throws JsonProcessingException {

        MemberDto memberDto = getGoogleUserInfo(code);

        Member member = insertSocialMember(memberDto);

        forceLogin(member);

        TokenDto tokenDto = jwtUtil.createAllToken(member.getEmail());
        setHeader(res, tokenDto);

        return new UserResponseDto(member);
    }

    public MemberDto getGoogleUserInfo(String code) {

        RestTemplate restTemplate = new RestTemplate();
        String codeReqURL = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", googleClientId);
        map.add("client_secret", googleClientSecret);
        map.add("redirect_uri", googleRedirectUri);
        map.add("grant_type", "authorization_code");
        map.add("code", code);


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<GoogleOAuthResponseDto> response = restTemplate.postForEntity(codeReqURL, request, GoogleOAuthResponseDto.class);
            GoogleOAuthResponseDto oauthResponse = response.getBody();
            String accessToken = oauthResponse.getAccess_token();

            return getGooleUserInfoByToken(accessToken);

        } catch (Exception e) {
            log.error("error =" + e.getMessage());
        }
        return null;
    }

    private MemberDto getGooleUserInfoByToken(String accessToken) {

        RestTemplate restTemplate = new RestTemplate();
        String userInfoUrl = "https://www.googleapis.com/userinfo/v2/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken); // Bearer 토큰 설정

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        try {
            // Google 사용자 정보 요청
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    userInfoUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            Map<String, Object> userInfo = response.getBody();

            String id = userInfo.get("id").toString();
            String name =  userInfo.get("name").toString();
            String email = userInfo.get("email").toString();
            String pwd = bCryptPasswordEncoder.encode(UUID.randomUUID().toString());
            String gender = "비공개";

            return MemberDto.builder()
                    .id(id)
                    .pwd(pwd)
                    .name(name)
                    .email(email)
                    .gender(gender)
                    .grade("ROLE_USER")
                    .oAuthProvider(OAuthProvider.GOOGLE) // 이 부분도 Google에 맞게 수정
                    .build();

        } catch (Exception e) {
            log.error("Error fetching Google user info: ", e.getMessage());
            return null;
        }

    }
}
