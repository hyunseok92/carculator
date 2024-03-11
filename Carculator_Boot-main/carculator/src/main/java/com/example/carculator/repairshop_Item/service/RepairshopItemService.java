package com.example.carculator.repairshop_Item.service;

import com.example.carculator.repairshop_Item.entity.RepairshopItem;
import com.example.carculator.repairshop_Item.entity.RepairshopItemDto;
import com.example.carculator.repairshop_Item.repository.RepairshopItemRepository;
import com.example.carculator.repairshop_Item.repository.RepairshopItemRepositoryCustomImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class RepairshopItemService {
    private final RepairshopItemRepository repairshopItemRepository;
    private final RepairshopItemRepositoryCustomImpl repairshopItemRepositoryCustomImpl;

    // 정비업체 재고 리스트 조회
    public List<RepairshopItemDto> getInventoryList(Long repairshop_code) {
        return repairshopItemRepositoryCustomImpl.getInventoryList(repairshop_code);
    }

    // 정비업체 재고 추가하기
    @Transactional
    public RepairshopItem insertInventoryProduct(RepairshopItem repairshopItem) {
        log.info("\n\n\nRepository에 save할 객체값 : " + repairshopItem);
        return repairshopItemRepository.save(repairshopItem);
    }

    // 정비업체 재고 삭제
    @Transactional
    public void deleteInventoryProduct(RepairshopItem repairshopItem) {
        log.info("\n\n\nDELETE할 객체값 : " + repairshopItem);
        String name_product = repairshopItem.getName_product();
        Long repairshop_code = repairshopItem.getRepairshop_code();
        repairshopItemRepository.deleteByName_product(name_product, repairshop_code);
    }
}
