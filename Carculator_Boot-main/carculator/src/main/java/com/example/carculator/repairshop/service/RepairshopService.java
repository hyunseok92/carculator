package com.example.carculator.repairshop.service;

import com.example.carculator.repairshop.entity.RepairShop;
import com.example.carculator.repairshop.repository.RepairShopRepository;
import com.example.carculator.repairshop.repository.RepairShopRepositoryCustom;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class RepairshopService {
    private final RepairShopRepository repairshopRepository;
    private final RepairShopRepositoryCustom repairshopRepositoryCustom;


    public List<RepairShop> repairshopList() {
        return repairshopRepository.findAll();
    }

    public RepairShop getRepairShopByCode(String repairshop_code) {
        RepairShop repairshopEntity = repairshopRepository.findById(repairshop_code).orElseThrow(() -> new RuntimeException("정비업체를 찾을 수 없습니다."));
        return RepairShop.builder()
                .repairshop_code(repairshopEntity.getRepairshop_code())
                .membercode(repairshopEntity.getMembercode())
                .repairshop_name(repairshopEntity.getRepairshop_name())
                .repairshop_address(repairshopEntity.getRepairshop_address())
                .repairshop_phone(repairshopEntity.getRepairshop_phone())
                .repairshop_rate(repairshopEntity.getRepairshop_rate())
                .latitude(repairshopEntity.getLatitude())
                .longitude(repairshopEntity.getLongitude())
                .repairshop_open(repairshopEntity.getRepairshop_open())
                .repairshop_close(repairshopEntity.getRepairshop_close())
                .build();
    }

    public List<RepairShop> getFilteredRepairshopByRate(String repairshop_rate) {
        return repairshopRepository.findByRepairShopRate(repairshop_rate);
    }

    @Transactional
    public RepairShop insertRepairShop(RepairShop repairshop) {
        RepairShop repairshopEntity = RepairShop.builder()
                .repairshop_code(repairshop.getRepairshop_code())
                .membercode(repairshop.getMembercode())
                .repairshop_name(repairshop.getRepairshop_name())
                .repairshop_address(repairshop.getRepairshop_address())
                .repairshop_phone(repairshop.getRepairshop_phone())
                .repairshop_rate(repairshop.getRepairshop_rate())
                .latitude(repairshop.getLatitude())
                .longitude(repairshop.getLongitude())
                .build();
        return repairshopRepository.save(repairshopEntity);
    }

    @Transactional
    public RepairShop updateRepairShop(RepairShop repairshop) {
        RepairShop repairshopEntity = repairshopRepository.findById(repairshop.getMembercode()).orElseThrow(() -> new RuntimeException("없음"));
        repairshopEntity.setRepairshop_code(repairshop.getRepairshop_code());
        repairshopEntity.setMembercode(repairshop.getMembercode());
        repairshopEntity.setRepairshop_name(repairshop.getRepairshop_name());
        repairshopEntity.setRepairshop_address(repairshop.getRepairshop_address());
        repairshopEntity.setRepairshop_phone(repairshop.getRepairshop_phone());
        repairshopEntity.setRepairshop_rate(repairshop.getRepairshop_rate());
        repairshopEntity.setLatitude(repairshop.getLatitude());
        repairshopEntity.setLongitude(repairshop.getLongitude());

        return repairshopRepository.save(repairshopEntity);
    }

    @Transactional
    public void deleteRepairShop(String memberNo) {
        RepairShop repairshopEntity = repairshopRepository.findById(memberNo).orElseThrow(() -> new RuntimeException("없음"));
        repairshopRepository.delete(repairshopEntity);
    }
}