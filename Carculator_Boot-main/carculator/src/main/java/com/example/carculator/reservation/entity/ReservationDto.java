package com.example.carculator.reservation.entity;

import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReservationDto {
    private Long repairshop_code;
    private String membercode;
    private Long reserv_code;
    private Timestamp reserv_date;
    private String car_number;
    private String repair_item;
    private String reserv_yn;
    private String content;
    private String name;
    private String car_model;
    private String phone;
    private String repairshop_address;
    private String repairshop_name;

//    public Reservation toEntity (ReservationDto r) {
//        Reservation reservation = Reservation.builder()
//                .repairshop_code(r.getRepairshop_code())
//                .membercode(r.getMembercode())
//                .reserv_code(r.getReserv_code())
//                .reserv_date(r.getReserv_date())
//                .car_number(r.getCar_number())
//                .repair_item(r.getRepair_item())
//                .reserv_yn(r.getReserv_yn())
//                .content(r.getContent())
//                .name(r.getName())
//                .car_model(r.getCar_model())
//                .phone(r.getPhone())
//                .repairshop_address(r.getRepairshop_address())
//                .repairshop_name(r.getRepairshop_name())
//                .build();
//
//        return reservation;
//    }
}
