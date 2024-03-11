package com.example.carculator.car.controller;

import com.example.carculator.car.entity.MyCar;
import com.example.carculator.car.entity.MyCarDto;
import com.example.carculator.car.service.CarService;
import com.example.carculator.commons.AIRequirements;
import com.example.carculator.commons.FileUplode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CarController {
    @Autowired
    private CarService carService;

    // 내 차량 정보보기
    @GetMapping("/car/{membercode}")
    public ResponseEntity<Optional<MyCarDto>> getMyCarInfo(@PathVariable(name = "membercode") String membercode)throws Exception{
        Optional<MyCarDto> myCarDto = carService.getMyCarInfo(membercode);
        log.info("\n" + myCarDto);
        return ResponseEntity.ok(myCarDto);
    }

    // 내 차량 삭제
    @DeleteMapping("/car/{membercode}")
    public ResponseEntity<?> deleteMyCarInfo(@PathVariable(name = "membercode") String membercode)throws Exception{
        try {
            log.info("\n\n\n\n\nmembercode : " + membercode);
            if (carService.deleteMyCarInfo(membercode) == 1) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                log.info("\ncode 1: 차량 삭제 실패입니다.");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (ResponseStatusException e) {
            log.info("\ncode 2: 차량 삭제 실패입니다.");
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    // 내 차량 추가
    @PostMapping(value = "/car", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertMyCarInfo(@ModelAttribute MyCar myCar, @RequestPart("carImg") MultipartFile carImg) throws Exception {
        try {
            String uploadPath = System.getProperty("user.dir") + "\\carculator\\src\\main\\resources\\static\\carImages\\";
            if (carImg != null) {
                String originalFileName = carImg.getOriginalFilename();
                myCar.setCar_original_img(originalFileName);
                String renameFileName = FileUplode.uploadFile(uploadPath, originalFileName, carImg);
                myCar.setCar_rename_img(renameFileName);
            }
            return new ResponseEntity<MyCar>(carService.insertMyCarInfo(myCar), HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    // AI 구동 및 식별값 return
    @PostMapping(value = "/car/img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> insertCarImage(@ModelAttribute MyCar myCar, @RequestPart("carImg") MultipartFile carImg) throws Exception {
        log.info("\n\n\nAI 이미지 분석을 시작합니다.");
        try {
            AIRequirements aiRequirements = new AIRequirements();
            // AI 매개변수 조건충족을 위한 이미지 리사이징
            MultipartFile resizedImage = aiRequirements.resize(carImg);

            String carModel = null;
            if (resizedImage != null) {
                carModel = aiRequirements.aiPredict(resizedImage);
                if (carModel.equals("no detections)")) {
                    log.info("\nAI Predict 결과 : No Detections 입니다.");
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "식별 불가능한 사진입니다. 다시 촬영해주세요.");
                } else {
                    log.info("\n\n" + carModel);
                }
            }
            return new ResponseEntity<String>(carModel, HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }
}
