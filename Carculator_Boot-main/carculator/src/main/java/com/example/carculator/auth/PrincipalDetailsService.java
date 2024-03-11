package com.example.carculator.auth;

import com.example.carculator.member.model.entity.Member;
import com.example.carculator.member.model.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

// 시큐리티 설정에서
@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<Member> memberEntity = memberRepository.findByEmail(username);

        Member member  = memberEntity.orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수가 없습니다"));

        return new PrincipalDetails(member);
    }
}
