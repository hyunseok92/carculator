package com.example.carculator.shop.entity;

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
@Table(name="bill_detail")
public class BillDetail {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bill_detail_seq")
    @SequenceGenerator(name = "bill_detail_seq", sequenceName = "bd_no_seq", allocationSize = 1)
    private Long BD_NO;

    @Column(name="bill_no")
    private String billNo;

    @Column(name="membercode")
    private String memberCode;

    @Column(name="product_amount")
    private Long productAmount;

    @Column(name="product_name")
    private String productName;

    @Column(name="bill_count")
    private Long billCount;

    @Column(name="product_code")
    private Long productCode;

}
