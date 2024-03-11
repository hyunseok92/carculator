package com.example.carculator.shop.repository;

import com.example.carculator.shop.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, String> {
    List<Bill> findByMemberCode(String memberCode);
}
