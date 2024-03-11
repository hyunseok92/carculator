package com.example.carculator.repairshop_Item.entity;

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
public class RepairshopItemDto {
    @Id
    private Long repairshop_code;
    private String name_product;
    private Long quantity;
    private Long price;
}
