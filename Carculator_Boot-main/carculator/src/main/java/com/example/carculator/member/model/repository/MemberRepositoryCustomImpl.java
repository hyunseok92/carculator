package com.example.carculator.member.model.repository;

import com.example.carculator.repairshop.entity.QRepairShop;
import com.example.carculator.member.dto.MemberDto;
import com.example.carculator.member.model.entity.QMember;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<MemberDto> findMemberByMembercode(String membercode) {
        QMember m = QMember.member;
        QRepairShop rs = QRepairShop.repairShop;

        Tuple tupple = queryFactory.select(
                        m.membercode,
                        m.email,
                        m.phone,
                        m.name,
                        m.birth,
                        m.gender,
                        m.grade,
                        rs.repairshop_name,
                        rs.repairshop_address,
                        rs.repairshop_phone)
                .from(m)
                .leftJoin(rs).on(m.membercode.eq(rs.membercode))
                .where(m.membercode.eq(membercode))
                .fetchOne();
        if (tupple != null) {
            return Optional.of(MemberDto.builder()
                            .membercode(tupple.get(m.membercode))
                            .email(tupple.get(m.email))
                            .phone(tupple.get(m.phone))
                            .name(tupple.get(m.name))
                            .birth(tupple.get(m.birth))
                            .gender(tupple.get(m.gender))
                            .shopname(tupple.get(rs.repairshop_name))
                            .shopaddress(tupple.get(rs.repairshop_address))
                            .shopnumber(tupple.get(rs.repairshop_phone))
                            .grade(tupple.get(m.grade))
                            .shopphone(tupple.get(rs.repairshop_phone))
                    .build());
        } else {
            return Optional.empty();
        }
    }
}
