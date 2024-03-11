package com.example.carculator.reservation.controller;

import com.example.carculator.reservation.entity.*;
import com.example.carculator.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    // ========================================================================================================
    // =============================================   정비소 코드   ===========================================
    // ========================================================================================================

    // 내 정비소에 들어온 예약 전부 조회 : Custom
    @GetMapping("/reservations/{repairshop_code}")
    public ResponseEntity<List<ReservationDto>> getReceivedReservList(@PathVariable(name = "repairshop_code") Long repairshop_code) throws Exception {
        List<ReservationDto> list
                = reservationService.getReceivedReservList(repairshop_code);
        log.info("\n\n" + list);
        return ResponseEntity.ok(list);
    }

//     내 정비소에 들어온 예약 상세조회
    @GetMapping("/reservation/{reserv_code}")
    public ResponseEntity<Optional<ReservationDto>> getReceivedReservDetail(@PathVariable(name = "reserv_code") Long reserv_code) throws Exception {
        log.info("\n\n\n" + reserv_code);
        Optional<ReservationDto> reservation = reservationService.getReceivedReservDetail(reserv_code);
        log.info("\n\n" + reservation);
        return ResponseEntity.ok(reservation);
    }

    // 예약수락 및 반려
    @PatchMapping("/reservation")
    public void updateReceivedReserv(@RequestBody Reservation data) throws Exception {
        reservationService.updateReceivedReserv(data);
    }

//    // ========================================================================================================
//    // ==============================================   회원 코드   ============================================
//    // ========================================================================================================

    // 유저가 예약한 리스트 조회
    @GetMapping("/mReservation/{membercode}")
    public ResponseEntity<List<ReservationDto>> getMemberReservList(@PathVariable(name = "membercode") String membercode) throws Exception {
        List<ReservationDto> list
                = reservationService.getMemberReservList(membercode);
        log.info("\n\n" + list);
        return ResponseEntity.ok(list);
    }

    // 유저의 예약 상세조회
    @GetMapping("/mdReservation/{reserv_code}")
    public ResponseEntity<Optional<ReservationDto>> getMemberReservDetail(@PathVariable(name = "reserv_code") Long reserv_code) throws Exception {
        Optional<ReservationDto> reservation = reservationService.getMemberReservDetail(reserv_code);
        log.info("\n\n" + reservation);
        return ResponseEntity.ok(reservation);
    }

    // 유저 예약 삭제(취소)
    @DeleteMapping("/reservation/{reserv_code}")
    public void deleteMemberReservation(@PathVariable(name = "reserv_code") Long reserv_code) throws Exception {
        log.info("\n\n\n예약코드 : " + reserv_code);
        reservationService.deleteMemberReservation(reserv_code);
        log.info("\n\n\n예약삭제가 완료되었습니다.");
    }

    // 유저 정보 조회용
    @GetMapping("/reservation/uInfo/{membercode}")
    public ResponseEntity<Optional<ReservationDto>> getMemberInfo(@PathVariable(name = "membercode") String membercode) throws Exception {
        log.info("\n\n\n\n\n\n컨트롤러 도착");
        Optional<ReservationDto> memberInfo = reservationService.getMemberInfo(membercode);
        log.info("\n\n" + memberInfo);
        return ResponseEntity.ok(memberInfo);
    }

    // 예약 등록
    @PostMapping("/reservation")
    public ResponseEntity<?> insertReservation(@RequestBody Reservation reservation) {
        try {
            Reservation insertReservation = reservationService.insertReservation(reservation);
            return new ResponseEntity<>(insertReservation, HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }
}