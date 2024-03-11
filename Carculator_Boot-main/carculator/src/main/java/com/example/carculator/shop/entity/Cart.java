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
@Table(name="cart")
public class Cart {
    @Id
    @Column(name="cartcode")
    private String cartCode;

    @Column(name="product_count")
    private Long productCount;

    @Column(name="product_code")
    private Long productCode;

    @Column(name="product_name")
    private String productName;

    @Column(name="product_rimg")
    private String productRmg;

    @Column(name="bill_count")
    private Long billCount;

    @Column(name="product_amount")
    private Long productAmount;


    @Column(name="membercode")
    private String memberCode;
}
