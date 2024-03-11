package com.example.carculator.car.repository;

import com.example.carculator.car.entity.MyCarDto;
import com.example.carculator.car.entity.QMyCar;
import com.example.carculator.member.model.entity.QMember;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CarRepositoryCustomImpl implements CarRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    // 나의 차량정보 조회
    @Override
    public Optional<MyCarDto> findMyCarByMembercode(String membercode) {
        QMyCar mc = QMyCar.myCar;
        QMember m = QMember.member;

        Tuple tuple = queryFactory.select(
                        mc.car_id,
                        mc.membercode,
                        mc.car_model,
                        mc.car_year,
                        mc.car_number,
                        mc.car_original_img,
                        mc.car_rename_img,
                        m.name)
                .from(mc)
                .leftJoin(m).on(mc.membercode.eq(m.membercode))
                .where(mc.membercode.eq(membercode))
                .fetchOne();
        if(tuple != null) {
            return Optional.of(MyCarDto.builder()
                    .car_id(tuple.get(mc.car_id))
                    .membercode(tuple.get(mc.membercode))
                    .car_model(tuple.get(mc.car_model))
                    .car_year(tuple.get(mc.car_year))
                    .car_number(tuple.get(mc.car_number))
                    .car_original_img(tuple.get(mc.car_original_img))
                    .car_rename_img(tuple.get(mc.car_rename_img))
                    .name(tuple.get(m.name))
                    .build());
        } else {
            return Optional.empty();
        }
    }

    // 내 차 등록 전 차량 수 확인
    @Override
    public int countMyCarByMembercode(String membercode) {

        QMyCar mc = QMyCar.myCar;

        return (int) queryFactory
                .select(Expressions.constant(1L))
                .from(mc)
                .where(mc.membercode.eq(membercode))
                .fetchCount();
    }

}
