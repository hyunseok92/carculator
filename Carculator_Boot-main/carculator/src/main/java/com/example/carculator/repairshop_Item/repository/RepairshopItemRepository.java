package com.example.carculator.repairshop_Item.repository;

import com.example.carculator.repairshop_Item.entity.RepairshopItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RepairshopItemRepository extends JpaRepository<RepairshopItem, Long>, RepairshopItemRepositoryCustom {
    @Transactional
    @Modifying
    @Query("DELETE FROM RepairshopItem r WHERE r.name_product = :name_product AND r.repairshop_code = :repairshop_code")
    void deleteByName_product(String name_product, Long repairshop_code);
}
