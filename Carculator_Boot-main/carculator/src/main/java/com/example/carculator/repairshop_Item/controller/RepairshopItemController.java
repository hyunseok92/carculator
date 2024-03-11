package com.example.carculator.repairshop_Item.controller;

import com.example.carculator.repairshop_Item.entity.RepairshopItem;
import com.example.carculator.repairshop_Item.entity.RepairshopItemDto;
import com.example.carculator.repairshop_Item.service.RepairshopItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RepairshopItemController {

    private final RepairshopItemService repairshopItemService;

    // 정비업체 재고 리스트 조회
    @GetMapping("/shopInven/{repairshop_code}")
    public ResponseEntity<List<RepairshopItemDto>> getInventoryList(@PathVariable Long repairshop_code) throws Exception {
        List<RepairshopItemDto> list = repairshopItemService.getInventoryList(repairshop_code);
//        log.info("\n\n" + list);
        return ResponseEntity.ok(list);
    }

    // 정비업체 재고 추가, 수정하기
    @PostMapping("/shopInven")
    public ResponseEntity<RepairshopItem> insertInventoryProduct(@RequestBody RepairshopItem repairshopItem) throws Exception {
        RepairshopItem dto = repairshopItemService.insertInventoryProduct(repairshopItem);
//        log.info("\n\n" + dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    // 정비업체 재고 삭제
    @DeleteMapping("/shopInven")
    public void deleteInventoryProduct(@RequestBody RepairshopItem repairshopItem) throws Exception {
        repairshopItemService.deleteInventoryProduct(repairshopItem);
    }
}
