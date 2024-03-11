package com.example.carculator.repairshop_Item.repository;

import com.example.carculator.repairshop_Item.entity.RepairshopItemDto;

import java.util.List;

public interface RepairshopItemRepositoryCustom {
    List<RepairshopItemDto> getInventoryList(Long repairshop_code);
}
