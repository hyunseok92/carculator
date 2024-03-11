package com.example.carculator.shop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private String cartCode;
    private Long productCode;
    private String productName;
    private String productRmg;
    private Long billCount;
    private Long productAmount;
    private String memberCode;
    private Long productCount;
}
