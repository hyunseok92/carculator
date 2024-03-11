package com.example.carculator.member.model.service;

import com.example.carculator.repairshop.entity.RepairShop;
import com.example.carculator.repairshop.repository.RepairShopRepository;
import com.example.carculator.member.dto.MemberDto;
import com.example.carculator.member.model.entity.Member;
import com.example.carculator.member.model.repository.MemberRepository;
import com.example.carculator.member.model.repository.MemberRepositoryCustomImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRepositoryCustomImpl memberRepositoryCustomImpl;

    private final RepairShopRepository repairShopRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${oauth.kakao.client_id}")
    private String kakaoClientId;

    //로그인
    //Member 전체 조회
    public List<Member> memberList() {
        return memberRepository.findByGradeNot("ROLE_ADMIN");
    }

    //Member 유저 코드로 조회
    public Optional<MemberDto> findMemberByMembercode(String memberCode) {
        return memberRepositoryCustomImpl.findMemberByMembercode(memberCode);
    }

    //Member ID로 조회
    public Member getMemberById(String memberId) {
        Member memberEntity = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        return Member.builder()
                .membercode(memberEntity.getMembercode())
                .pwd(memberEntity.getPwd())
                .phone(memberEntity.getPhone())
                .email(memberEntity.getEmail())
                .name(memberEntity.getName())
                .birth(memberEntity.getBirth())
                .gender(memberEntity.getGender())
                .shopname(memberEntity.getShopname())
                .picture(memberEntity.getPicture())
                .shopaddress(memberEntity.getShopaddress())
                .shopnumber(memberEntity.getShopnumber())
                .grade(memberEntity.getGrade())
                .status(memberEntity.getStatus())
                .oAuthProvider(memberEntity.getOAuthProvider())
                .build();
    }

    //Member 등록
    @Transactional
    public MemberDto insertMember(MemberDto memberDto) {

        boolean existEmail = memberRepository.existsMemberByEmail(memberDto.getEmail());

        if (existEmail) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 등록된 이메일 주소입니다");
        }

        memberDto.setPwd(bCryptPasswordEncoder.encode(memberDto.getPwd()));

        Member memberEntity = Member.builder()
                .membercode(memberDto.getMembercode())
                .pwd(memberDto.getPwd())
                .phone(memberDto.getPhone())
                .email(memberDto.getEmail())
                .name(memberDto.getName())
                .birth(memberDto.getBirth())
                .gender(memberDto.getGender())
                .shopname(memberDto.getShopname())
                .shopphone(memberDto.getShopphone())
                .picture(memberDto.getPicture())
                .shopaddress(memberDto.getShopaddress())
                .shopnumber(memberDto.getShopnumber())
                .shoprate(memberDto.getShoprate())
                .grade(memberDto.getGrade())
                .status(memberDto.getStatus())
                .oAuthProvider(memberDto.getOAuthProvider())
                .build();

        Member savedMember = memberRepository.save(memberEntity);

        return MemberDto.builder()
                .membercode(savedMember.getMembercode())
                .pwd(savedMember.getPwd())
                .phone(savedMember.getPhone())
                .email(savedMember.getEmail())
                .name(savedMember.getName())
                .birth(savedMember.getBirth())
                .gender(savedMember.getGender())
                .shopname(savedMember.getShopname())
                .shopphone(savedMember.getShopphone())
                .shoprate(savedMember.getShoprate())
                .picture(savedMember.getPicture())
                .shopaddress(savedMember.getShopaddress())
                .shopnumber(savedMember.getShopnumber())
                .grade(savedMember.getGrade())
                .status(savedMember.getStatus())
                .oAuthProvider(savedMember.getOAuthProvider())
                .build();
    }

    // 비밀번호 변경
    @Transactional
    public void updatePassword(MemberDto memberDto) {
        String cryptedPwd = bCryptPasswordEncoder.encode(memberDto.getPwd());
        memberDto.setPwd(cryptedPwd);
        try {
            memberRepository.updatePwd(memberDto.getMembercode(), cryptedPwd);
        } catch (Exception e) {
            log.info("실패\n" + e.getMessage());
        }
    }

    @Transactional
    public void updatePasswordByEmail(MemberDto memberDto) {
        String cryptedPwd = bCryptPasswordEncoder.encode(memberDto.getPwd());
        memberDto.setPwd(cryptedPwd);

        log.info("member pwd " + memberDto.getPwd() + " memberEmail " + memberDto.getEmail());
        try {
            memberRepository.updatePwdByEmail(memberDto.getPwd(), memberDto.getEmail());
        } catch (Exception e) {
            log.info("비밀번호 변경 오류");
        }
    }
    // 비밀번호 확인
    @Transactional
    public void checkPwd(MemberDto memberDto) {
        String cryptedPwd = bCryptPasswordEncoder.encode(memberDto.getPwd());
    }

    // 정비소 회원의 정비소 정보 변경
    public void modifyShopInfo(MemberDto memberDto) throws Exception {
        Optional<Member> optionalMember = memberRepository.findById(memberDto.getMembercode());

        if (optionalMember.isPresent()) {
            Member updated = optionalMember.get();
            updated.setShopname(memberDto.getShopname());
            updated.setShopaddress(memberDto.getShopaddress());
            updated.setShopphone(memberDto.getShopphone());
            updated.setStatus("2");
            memberRepository.save(updated);
        } else {
            throw new Exception("Member not found for membercode: " + memberDto.getMembercode());
        }
    }

    // 회원 상태 변경
    @Transactional
    public void updateStatus(String membercode) {

        Member member = memberRepository.findById(membercode).orElseThrow(() -> new RuntimeException("회원 없음"));

        if (member.getStatus().equals("0")) {
            member.setStatus("1");
        } else {
            member.setStatus("0");
        }

        memberRepository.updateStatus(membercode, member.getStatus());
    }

    @Transactional
    public void updateShopStatus(MemberDto memberDto) throws BadRequestException {
        memberRepository.updateStatus(memberDto.getMembercode(), memberDto.getStatus());

        Member member = memberRepository.findById(memberDto.getMembercode()).orElseThrow(() -> new RuntimeException("회원 없음"));

        try {
            if (member.getStatus().equals("0")) {
                Float[] xy = findGeoPoint(member.getShopaddress());

                if (xy != null) {
                    RepairShop repairShop = RepairShop.builder()
                            .membercode(member.getMembercode())
                            .repairshop_name(member.getShopname())
                            .repairshop_address(member.getShopaddress())
                            .repairshop_phone(member.getShopphone())
                            .repairshop_rate(member.getShoprate())
                            .longitude(xy[0].toString())
                            .latitude(xy[1].toString())
                            .build();

                    log.info(repairShop.toString());
                    repairShopRepository.save(repairShop);

                } else {
                    throw new BadRequestException("업체 등록 실패");
                }
            }
        } catch (Exception e) {
            log.info("repairshop에러" + e.getMessage());
        }
    }

    public Float[] findGeoPoint(String address) {

        String apiKey = "KakaoAK " + kakaoClientId;
        String uri = "https://dapi.kakao.com/v2/local/search/address.json";

        RestTemplate restTemplate = new RestTemplate();

        try {
            //요청 헤더에 만들기, Authorization 헤더 설정하기
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", apiKey);
            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

            UriComponents uriComponents = UriComponentsBuilder
                    .fromHttpUrl(uri)
                    .queryParam("query", address)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, entity, String.class);

            // API Response로부터 body 뽑아내기
            String body = response.getBody();
            JSONObject json = new JSONObject(body);
            // body에서 좌표 뽑아내기
            JSONArray documents = json.getJSONArray("documents");
            String x = documents.getJSONObject(0).getString("x");
            String y = documents.getJSONObject(0).getString("y");

            Float longitude = Float.parseFloat(x);
            Float latitude = Float.parseFloat(y);

            return new Float[]{longitude, latitude};

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
