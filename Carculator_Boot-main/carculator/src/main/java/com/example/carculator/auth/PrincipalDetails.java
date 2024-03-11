package com.example.carculator.auth;

import com.example.carculator.member.model.entity.Member;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
public class PrincipalDetails implements UserDetails {

    private Member member;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return member.getGrade();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return member.getPwd();
    }

    @Override
    public String getUsername() {
        return member.getName();
    }

    //니 계정 만료됐니
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    //니 계정 잠겼니
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //니 계정 시간이 지났니
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //니 계정이 밴당했니
    @Override
    public boolean isEnabled() {
        return false;
    }
}
