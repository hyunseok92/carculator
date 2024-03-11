package com.example.carculator.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="bill")
public class Bill {

    @Id
    @Column(name="bill_no")
    private String billNo;

    @Column(name="membercode")
    private String memberCode;

    @Column(name="bill_name")
    private String billName;

    @Column(name="bill_amount")
    private Long billAmount;

    @Column(name="recipient")
    private String recipient;

    @Column(name="pay_name")
    private String payName;

    @Column(name="bill_phone")
    private String billPhone;

    @Email
    @Column(name="bill_email")
    private String billEmail;

    @Column(name="bill_addressno")
    private String billAddressNo;

    @Column(name="bill_address")
    private String billAddress;

    @Column(name="bill_now")
    private String billNow;

    @Column(name="bill_date")
    private LocalDateTime billDate;

    @Column(name="bill_request")
    private String billRequest;

    @Column(name="bill_cancle")
    private String billCancle;

    @Column(name="bill_kind")
    private String billKind;

    @Column(name="inv_no")
    private String invNo;

    @Column(name="paymentkey")
    private String paymentkey;

}
