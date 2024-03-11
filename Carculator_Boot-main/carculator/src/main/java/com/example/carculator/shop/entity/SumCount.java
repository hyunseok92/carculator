package com.example.carculator.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="sumcount")
public class SumCount {

    @Column(name="porduct_code")
    private String porductCode;

    @Column(name="product_name")
    private String productName;

    @Column(name="product_viewdate")
    private Date productViewdate;

    @Column(name="user_clickno")
    private int userClickNo;
    @Id
    @Column(name="usercode")
    private String usercode;
}
