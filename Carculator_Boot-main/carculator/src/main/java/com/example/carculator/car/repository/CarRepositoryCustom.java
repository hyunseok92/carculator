package com.example.carculator.car.repository;

import com.example.carculator.car.entity.MyCarDto;

import java.util.Optional;

public interface CarRepositoryCustom {

    // 내 차량 조회
    Optional<MyCarDto> findMyCarByMembercode(String membercode);

    // 내 차 등록 전 차량 수 확인
    int countMyCarByMembercode(String membercode);
}
