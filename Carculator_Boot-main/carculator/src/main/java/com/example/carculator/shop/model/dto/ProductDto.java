package com.example.carculator.shop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long productCode;
    private String productName;
    private String category1;
    private Long productCount;
    private String productDetail;
    private Long productSum;
    private Long productView;
    private Long productAmount;
    private String productImg;
    private String productRmg;
    private String detailImg;
    private String detailRmg;
}
