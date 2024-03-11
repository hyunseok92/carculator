package com.example.carculator.car.repository;

import com.example.carculator.car.entity.MyCar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<MyCar, Long>, CarRepositoryCustom {

    void deleteByMembercode(String membercode);
}
