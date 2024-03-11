package com.example.carculator.shop.repository;

import com.example.carculator.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, String> {
    @Query("SELECT r FROM Cart r WHERE r.memberCode = :memberCode AND r.productCode = :productCode")
    Cart findCartProduct(@Param("memberCode") String memberCode, @Param("productCode") Long productCode);
    @Query("SELECT r FROM Cart r WHERE r.memberCode = :memberCode")
    List<Cart> findCartList(@Param("memberCode") String memberCode);
}

