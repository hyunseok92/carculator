package com.example.carculator.repairshop.controller;

import com.example.carculator.repairshop.entity.RepairShop;
import com.example.carculator.repairshop.service.RepairshopService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@CrossOrigin
@RestController
public class RepairshopController {
    @Autowired
    private final RepairshopService repairshopService;

    //회원 리스트 조회
    @GetMapping("/repairshop")
    public ResponseEntity<List<RepairShop>> repairshopList() throws  Exception{
        return new ResponseEntity<>(repairshopService.repairshopList(), HttpStatus.OK);
    }

    //코드로 회원 조회
    @GetMapping("/repairshop/{repairshop_code}")
    public ResponseEntity<RepairShop> selectRepairshopByCode(@PathVariable("repairshop_code") String repairshop_code) throws Exception {

        RepairShop repairshop = repairshopService.getRepairShopByCode(repairshop_code);

        return new ResponseEntity<>(repairshop, HttpStatus.OK);
    }

    @GetMapping("/repairshop/rate/{repairshop_rate}")
    public ResponseEntity<List<RepairShop>> selectFilteredRepairshopByRate(@PathVariable("repairshop_rate") String repairshop_rate) throws Exception {
        List<RepairShop> filteredshop = repairshopService.getFilteredRepairshopByRate(repairshop_rate);
        return new ResponseEntity<>(filteredshop, HttpStatus.OK);
    }

    //회원 등록
    @PostMapping("/repairshop")
    public RepairShop repairshopInsert(@RequestBody RepairShop repairshop) {

        return repairshopService.insertRepairShop(repairshop);
    }

    //회원수정
    @PatchMapping("/repairshop")
    public RepairShop repairshopUpdate(@RequestBody RepairShop repairshop) {

        return repairshopService.updateRepairShop(repairshop);
    }

    //회원삭제
    @DeleteMapping("/repairshop/{memberNo}")
    public void repairshopDelete(@PathVariable String memberNo) {

        repairshopService.deleteRepairShop(memberNo);
    }
}