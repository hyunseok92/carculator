package com.example.carculator.damage.service;

import com.example.carculator.damage.entity.Damage;
import com.example.carculator.damage.repository.DamageRepository;
import com.example.carculator.damage.repository.DamageRepositoryCustom;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class DamageService {
    private DamageRepository damageRepository;
    private DamageRepositoryCustom damageRepositoryCustom;

    public List<Damage> getDamageList(String memberNo) {
        return damageRepository.findAll();
    }
//        Damage damageEntity = damageRepository.findById(memberNo).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
//        return Damage.builder()
//                .memberNo(damageEntity.getMemberNo())
//                .carNo(damageEntity.getCarNo())
//                .damaged(damageEntity.getDamaged())
//                .menteNo(damageEntity.getMenteNo())
//                .menteGrade(damageEntity.getMenteGrade())
//                .menteStartDate(damageEntity.getMenteStartDate())
//                .menteEndDate(damageEntity.getMenteEndDate())
//                .pay(damageEntity.getPay())
//                .build();
//    }
    @Transactional
    public Damage insertDamage(Damage damage) {
        Damage damageEntity = Damage.builder()
                .memberNo(damage.getMemberNo())
                .carNo(damage.getCarNo())
                .damaged(damage.getDamaged())
                .menteNo(damage.getMenteNo())
                .menteGrade(damage.getMenteGrade())
                .menteStartDate(damage.getMenteStartDate())
                .menteEndDate(damage.getMenteEndDate())
                .pay(damage.getPay())
                .build();
        return damageRepository.save(damageEntity);
    }
}
