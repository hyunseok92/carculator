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
@Table(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_code_seq", allocationSize = 1)
    @Column(name="product_code")
    private Long productCode;

    @Column(name="product_name")
    private String productName;

    @Column(name="category_1")
    private String category1;

    @Column(name="product_img")
    private String productImg;

    @Column(name="product_rimg")
    private String productRmg;

    @Column(name="detail_img")
    private String detailImg;

    @Column(name="detail_rimg")
    private String  detailRmg;

    @Column(name="product_count")
    private Long productCount;

    @Column(name="product_detail")
    private String productDetail;

    @Column(name="product_sum")
    private Long productSum;

    @Column(name="product_view")
    private Long productView;

    @Column(name="product_amount")
    private Long productAmount;

}
