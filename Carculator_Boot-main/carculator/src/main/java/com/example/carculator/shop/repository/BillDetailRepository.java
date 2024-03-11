package com.example.carculator.shop.repository;

import com.example.carculator.shop.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {
    List<BillDetail> findByBillNo(String billNo);
}
