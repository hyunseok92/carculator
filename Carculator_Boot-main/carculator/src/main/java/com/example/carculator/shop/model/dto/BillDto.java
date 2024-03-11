package com.example.carculator.shop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDto {
    private String billNo;
    private String memberCode;
    private String billName;
    private Long billAmount;
    private String recipient;
    private String payName;
    private String billPhone;
    private String billEmail;
    private String billAddressNo;
    private String billAddress;
    private String billNow;
    private LocalDateTime billDate;
    private String billRequest;
    private String billCancle;
    private String billKind;
    private String invNo;
    private String paymentkey;
}
