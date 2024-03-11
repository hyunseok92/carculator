package com.example.carculator.reservation.service;

import com.example.carculator.reservation.entity.*;
import com.example.carculator.reservation.repository.ReservationRepository;
import com.example.carculator.reservation.repository.ReservationRepositoryCustomImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationRepositoryCustomImpl reservationRepositoryCustomImpl;

    // ========================================================================================================
    // =============================================   정비소 코드   ===========================================
    // ========================================================================================================

//    // 내 정비소에 들어온 예약 전부 조회
    public List<ReservationDto> getReceivedReservList(Long rCode) {
        return reservationRepository.findReservationsByRepairshop_code(rCode);
    }

    // 내 정비소에 들어온 예약 상세조회

    public Optional<ReservationDto> getReceivedReservDetail(Long reserv_code) {
        return reservationRepositoryCustomImpl.findByReservCode(reserv_code);
    }

    // 예약수락 및 반려
    public void updateReceivedReserv(Reservation modified) {
        Optional<Reservation> reservation = reservationRepository.findById(modified.getReserv_code());

        if (reservation.isPresent()) {
            Reservation reserv = reservation.get();
            reserv.setReserv_yn(modified.getReserv_yn());
            reservationRepository.save(reserv);
        } else {
            log.info("예약수락 : 문제 발생");
        }
    }

//    // ========================================================================================================
//    // ==============================================   회원 코드   ============================================
//    // ========================================================================================================

    // 유저 예약 리스트 조회
    public List<ReservationDto> getMemberReservList(String membercode) {
        return reservationRepositoryCustomImpl.findReservationsByMembercode(membercode);
    }

    // 유저 예약 상세조회

    public Optional<ReservationDto> getMemberReservDetail(Long reserv_code) {
        return reservationRepositoryCustomImpl.findMemberReservByReservCode(reserv_code);
    }

    // 유저 예약 삭제
    @Transactional
    public void deleteMemberReservation(Long reserv_code) {
        reservationRepository.deleteById(reserv_code);
    }

    // 유저 정보 조회용
    public Optional<ReservationDto> getMemberInfo(String membercode) {
        return reservationRepositoryCustomImpl.findReservationByMembercode(membercode);
    }

    //Reservation 등록
    @Transactional
    public Reservation insertReservation(Reservation reservation){
        Long repairshop_code = reservation.getRepairshop_code();
        String reserv_date = String.valueOf(reservation.getReserv_date()).substring(0, 13);
        // 등록 전 같은 시간대의 예약이 있는지 확인
        int result = reservationRepositoryCustomImpl.countByRepairshop_codeAndReserv_code(repairshop_code, reserv_date);

        if (result <= 0) {
            Reservation reservationEntity = Reservation.builder()
                    .repairshop_code(reservation.getRepairshop_code())
                    .reserv_code(reservation.getReserv_code())
                    .car_number(reservation.getCar_number())
                    .reserv_date(reservation.getReserv_date())
                    .repair_item(reservation.getRepair_item())
                    .reserv_yn("예약대기")
                    .content(reservation.getContent())
                    .membercode(reservation.getMembercode())
                    .build();
            return reservationRepository.save(reservationEntity);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 이 시간에 예약이 있습니다.\n다른 예약시간을 선택해주세요.");
        }
    }
}
