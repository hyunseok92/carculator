package com.example.carculator.reservation.repository;

import com.example.carculator.car.entity.QMyCar;
import com.example.carculator.repairshop.entity.QRepairShop;
import com.example.carculator.reservation.entity.QReservation;
import com.example.carculator.reservation.entity.ReservationDto;
import com.example.carculator.member.model.entity.QMember;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    // 정비소 측 List 조회
    @Override
    public List<ReservationDto> findReservationsByRepairshop_code(Long repairshop_code) {
        QReservation r = QReservation.reservation;
        QMember m = QMember.member;
        QMyCar mc = QMyCar.myCar;

        JPAQuery<Tuple> query = queryFactory.select(
                r.repairshop_code,
                r.membercode,
                r.reserv_code,
                r.reserv_date,
                r.car_number,
                r.repair_item,
                r.reserv_yn,
                r.content,
                m.name,
                mc.car_model,
                m.phone)
                .from(r)
                .leftJoin(m).on(r.membercode.eq(m.membercode))
                .leftJoin(mc).on(r.membercode.eq(mc.membercode))
                .where(r.repairshop_code.eq(repairshop_code));

        return query.fetchJoin().fetch().stream().map(tupple -> ReservationDto.builder()
                .repairshop_code(tupple.get(r.repairshop_code))
                .membercode(tupple.get(r.membercode))
                .reserv_code(tupple.get(r.reserv_code))
                .reserv_date(tupple.get(r.reserv_date))
                .car_number(tupple.get(r.car_number))
                .repair_item(tupple.get(r.repair_item))
                .reserv_yn(tupple.get(r.reserv_yn))
                .content(tupple.get(r.content))
                .name(tupple.get(m.name))
                .car_model(tupple.get(mc.car_model))
                .phone(tupple.get(m.phone))
                .build()).toList();
    }

    // 정비소 측 상세조회
    @Override
    public Optional<ReservationDto> findByReservCode(Long reserv_code) {
        QReservation r = QReservation.reservation;
        QMember m = QMember.member;
        QMyCar mc = QMyCar.myCar;

        Tuple tupple = queryFactory.select(
                        r.repairshop_code,
                        r.membercode,
                        r.reserv_code,
                        r.reserv_date,
                        r.car_number,
                        r.repair_item,
                        r.reserv_yn,
                        r.content,
                        m.name,
                        mc.car_model,
                        m.phone)
                .from(r)
                .leftJoin(m).on(r.membercode.eq(m.membercode))
                .leftJoin(mc).on(r.membercode.eq(mc.membercode))
                .where(r.reserv_code.eq(reserv_code))
                .fetchOne();
        if (tupple != null) {
            return Optional.of(ReservationDto.builder()
                    .repairshop_code(tupple.get(r.repairshop_code))
                    .membercode(tupple.get(r.membercode))
                    .reserv_code(tupple.get(r.reserv_code))
                    .reserv_date(tupple.get(r.reserv_date))
                    .car_number(tupple.get(r.car_number))
                    .repair_item(tupple.get(r.repair_item))
                    .reserv_yn(tupple.get(r.reserv_yn))
                    .content(tupple.get(r.content))
                    .name(tupple.get(m.name))
                    .car_model(tupple.get(mc.car_model))
                    .phone(tupple.get(m.phone))
                    .build());
        } else {
            return Optional.empty();
        }
    }

    // 유저 측 자신의 예약리스트 조회하기
    @Override
    public List<ReservationDto> findReservationsByMembercode(String membercode) {
        QReservation r = QReservation.reservation;
        QMember m = QMember.member;
        QMyCar mc = QMyCar.myCar;
        QRepairShop rp = QRepairShop.repairShop;

        JPAQuery<Tuple> query = queryFactory.select(
                        r.repairshop_code,
                        r.membercode,
                        r.reserv_code,
                        r.reserv_date,
                        r.car_number,
                        r.repair_item,
                        r.reserv_yn,
                        r.content,
                        m.name,
                        mc.car_model,
                        m.phone,
                        rp.repairshop_address,
                        rp.repairshop_name)
                .from(r)
                .leftJoin(m).on(r.membercode.eq(m.membercode))
                .leftJoin(mc).on(r.membercode.eq(mc.membercode))
                .leftJoin(rp).on(r.repairshop_code.eq(rp.repairshop_code))
                .where(r.membercode.eq(membercode));

        return query.fetchJoin().fetch().stream().map(tupple -> ReservationDto.builder()
                .repairshop_code(tupple.get(r.repairshop_code))
                .membercode(tupple.get(r.membercode))
                .reserv_code(tupple.get(r.reserv_code))
                .reserv_date(tupple.get(r.reserv_date))
                .car_number(tupple.get(r.car_number))
                .repair_item(tupple.get(r.repair_item))
                .reserv_yn(tupple.get(r.reserv_yn))
                .content(tupple.get(r.content))
                .name(tupple.get(m.name))
                .car_model(tupple.get(mc.car_model))
                .phone(tupple.get(m.phone))
                .repairshop_address(tupple.get(rp.repairshop_address))
                .repairshop_name(tupple.get(rp.repairshop_name))
                .build()).toList();
    }

    // 유저 자신의 예약 상세조회
    @Override
    public Optional<ReservationDto> findMemberReservByReservCode(Long reserv_code) {
        QReservation r = QReservation.reservation;
        QMember m = QMember.member;
        QMyCar mc = QMyCar.myCar;
        QRepairShop rp = QRepairShop.repairShop;

        Tuple tuple = queryFactory.select(
                        r.repairshop_code,
                        r.membercode,
                        r.reserv_code,
                        r.reserv_date,
                        r.car_number,
                        r.repair_item,
                        r.reserv_yn,
                        r.content,
                        m.name,
                        mc.car_model,
                        m.phone,
                        rp.repairshop_address,
                        rp.repairshop_name)
                .from(r)
                .leftJoin(m).on(r.membercode.eq(m.membercode))
                .leftJoin(mc).on(r.membercode.eq(mc.membercode))
                .leftJoin(rp).on(r.repairshop_code.eq(rp.repairshop_code))
                .where(r.reserv_code.eq(reserv_code))
                .fetchOne();
        if(tuple != null) {
            return Optional.of(ReservationDto.builder()
                    .repairshop_code(tuple.get(r.repairshop_code))
                    .membercode(tuple.get(r.membercode))
                    .reserv_code(tuple.get(r.reserv_code))
                    .reserv_date(tuple.get(r.reserv_date))
                    .car_number(tuple.get(r.car_number))
                    .repair_item(tuple.get(r.repair_item))
                    .reserv_yn(tuple.get(r.reserv_yn))
                    .content(tuple.get(r.content))
                    .name(tuple.get(m.name))
                    .car_model(tuple.get(mc.car_model))
                    .phone(tuple.get(m.phone))
                    .repairshop_address(tuple.get(rp.repairshop_address))
                    .repairshop_name(tuple.get(rp.repairshop_name))
                    .build());
        } else {
            return Optional.empty();
        }
    }

    // 유저 정보 조회용
    @Override
    public Optional<ReservationDto> findReservationByMembercode(String membercode) {
        QMember m = QMember.member;
        QMyCar mc = QMyCar.myCar;
        QRepairShop r = QRepairShop.repairShop;

        Tuple tuple = queryFactory.select(
                        r.repairshop_code,
                        m.name,
                        mc.car_model,
                        mc.car_number,
                        m.phone)
                .from(m)
                .leftJoin(mc).on(m.membercode.eq(mc.membercode))
                .leftJoin(r).on(r.membercode.eq(m.membercode))
                .where(m.membercode.eq(membercode))
                .fetchOne();
        if(tuple != null) {
            return Optional.of(ReservationDto.builder()
                    .name(tuple.get(m.name))
                    .car_model(tuple.get(mc.car_model))
                    .car_number(tuple.get(mc.car_number))
                    .phone(tuple.get(m.phone))
                    .repairshop_code(tuple.get(r.repairshop_code))
                    .build());
        } else {
            return Optional.empty();
        }
    }

    // 예약 INSERT 이전 이미 있는 예약 확인
    @Override
    public int countByRepairshop_codeAndReserv_code(Long repairshop_code, String reserv_date) {
        QReservation r = QReservation.reservation;
        QMember m = QMember.member;
        QMyCar mc = QMyCar.myCar;
        QRepairShop rp = QRepairShop.repairShop;

        String formattedDate = "YYYY-MM-DD HH24";

        return (int) queryFactory
                .select(Expressions.constant(1L))
                .from(r)
                .where(r.repairshop_code.eq(repairshop_code)
                    .and(Expressions.stringTemplate(
                "TO_CHAR({0}, {1})", r.reserv_date, formattedDate)
                       .like(reserv_date)))
                .fetchCount();
    }
}