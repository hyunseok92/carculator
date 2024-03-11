package com.example.carculator.car.service;

import com.example.carculator.car.entity.MyCar;
import com.example.carculator.car.entity.MyCarDto;
import com.example.carculator.car.repository.CarRepository;
import com.example.carculator.car.repository.CarRepositoryCustomImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CarService {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CarRepositoryCustomImpl myCarRepositoryCustomImpl;

// 나의 차량정보 조회
    public Optional<MyCarDto> getMyCarInfo(String membercode) {
        return myCarRepositoryCustomImpl.findMyCarByMembercode(membercode);
    }

    // 나의 차 정보 삭제하기
    @Transactional
    public int deleteMyCarInfo(String membercode) {
        int result = myCarRepositoryCustomImpl.countMyCarByMembercode(membercode);
        log.info("\n\nresult : " + result);
        if (result != 0) {
            carRepository.deleteByMembercode(membercode);
            return 1;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "차량정보가 존재하지 않습니다.");
        }
    }

    // 나의 차 정보 등록
    @Transactional
    public MyCar insertMyCarInfo(MyCar myCar) {
        int result = myCarRepositoryCustomImpl.countMyCarByMembercode(myCar.getMembercode());
        if (result <= 0) {
            MyCar entity = MyCar.builder()
                    .car_id(myCar.getCar_id())
                    .car_model(myCar.getCar_model())
                    .car_year(myCar.getCar_year())
                    .car_number(myCar.getCar_number())
                    .membercode(myCar.getMembercode())
                    .car_original_img(myCar.getCar_original_img())
                    .car_rename_img(myCar.getCar_rename_img())
                    .build();
            return carRepository.save(entity);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "차량은 계정에 한 대만 등록 가능합니다.");
        }
    }
}
