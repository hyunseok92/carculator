package com.example.carculator.repairshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="REPAIRSHOP")
public class RepairShop {

    @Id
    @Column(name="REPAIRSHOP_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rep_seq")
    @SequenceGenerator(name = "rep_seq", sequenceName = "rep_code", allocationSize = 1)
    private Long repairshop_code;

    @Column(name="MEMBERCODE")
    private String membercode;

    @Column(name="REPAIRSHOP_NAME")
    private String repairshop_name;

    @Column(name="REPAIRSHOP_ADDRESS")
    private String repairshop_address;

    @Column(name="REPAIRSHOP_PHONE")
    private String repairshop_phone;

    @Column(name="REPAIRSHOP_RATE")
    private String repairshop_rate;

    @Column(name="LATITUDE")
    private String latitude;

    @Column(name="LONGITUDE")
    private String longitude;

    @Column(name="REPAIRSHOP_OPEN")
    private String repairshop_open;

    @Column(name="REPAIRSHOP_CLOSE")
    private String repairshop_close;
}