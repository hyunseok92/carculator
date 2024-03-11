package com.example.carculator.repairshop_Item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
@Table(name = "REPAIRSHOP_ITEM")
public class RepairshopItem {

    @Column(name = "REPAIRSHOP_CODE")
    private Long repairshop_code;
    @Id
    @Column(name = "NAME_PRODUCT")
    private String name_product;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "PRICE")
    private Long price;
}
