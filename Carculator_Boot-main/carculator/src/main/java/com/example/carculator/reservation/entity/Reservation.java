package com.example.carculator.reservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
@SequenceGenerator(
        name = "RESERV_CODE_GEN",
        sequenceName = "RESERV_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Table(name="RESERVATION")
public class Reservation {

    @Column(name = "REPAIRSHOP_CODE")
    private Long repairshop_code;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "RESERV_CODE_GEN"
    )
    @Column(name = "RESERV_CODE")
    private Long reserv_code;

    @Column(name = "MEMBERCODE")
    private String membercode;

    @Column(name = "CAR_NUMBER")
    private String car_number;

    @Column(name = "RESERV_DATE")
    private Timestamp reserv_date;

    @Column(name = "REPAIR_ITEM")
    private String repair_item;

    @Column(name = "RESERV_YN")
    private String reserv_yn;

    @Column(name = "CONTENT")
    private String content;

//    @Column(name = "NAME")
//    private String name;
//
//    @Column(name = "CAR_MODEL")
//    private String car_model;
//
//    @Column(name = "PHONE")
//    private String phone;
//
//    @Column(name = "REPAIRSHOP_ADDRESS")
//    private String repairshop_address;
//
//    @Column(name = "REPAIRSHOP_NAME")
//    private String repairshop_name;

    public ReservationDto toDto(Reservation r) {
        ReservationDto reservationDto = ReservationDto.builder()
                .repairshop_code(r.getRepairshop_code())
                .membercode(r.getMembercode())
                .reserv_code(r.getReserv_code())
                .reserv_date(r.getReserv_date())
                .car_number(r.getCar_number())
                .repair_item(r.getRepair_item())
                .reserv_yn(r.getReserv_yn())
                .content(r.getContent())
//                .name(r.getName())
//                .car_model(r.getCar_model())
//                .phone(r.getPhone())
//                .repairshop_address(r.getRepairshop_address())
//                .repairshop_name(r.getRepairshop_name())
                .build();

        return reservationDto;
    }
}
