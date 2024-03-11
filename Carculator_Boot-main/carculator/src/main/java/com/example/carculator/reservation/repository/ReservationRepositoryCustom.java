package com.example.carculator.reservation.repository;

import com.example.carculator.reservation.entity.ReservationDto;

import java.util.List;
import java.util.Optional;

public interface ReservationRepositoryCustom {

    List<ReservationDto> findReservationsByRepairshop_code(Long repairshop_code);

    // 정비소 측 상세조회
    Optional<ReservationDto> findByReservCode(Long reserv_code);

    // 유저 측 자신의 예약리스트 조회하기
    List<ReservationDto> findReservationsByMembercode(String membercode);

    // 유저 자신의 예약 상세조회
    Optional<ReservationDto> findMemberReservByReservCode(Long reserv_code);

    // 유저 정보 조회용
    Optional<ReservationDto> findReservationByMembercode(String membercode);

    // 예약 INSERT 이전 이미 있는 예약 확인
    int countByRepairshop_codeAndReserv_code(Long repairshop_code, String reserv_date);
}
