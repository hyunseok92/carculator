package com.example.carculator.damage.controller;

import com.example.carculator.damage.entity.Damage;
import com.example.carculator.damage.service.DamageService;
import com.example.carculator.commons.DamageStringCut;
import com.example.carculator.commons.FileUplode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RestController
public class DamageController {
    private DamageService damageService;

    //코드로 기록 조회
    @GetMapping("/damage/{memberNo}")
    public ResponseEntity<List<Damage>> selectMemberByCode(@PathVariable("memberNo") String memberNo) throws Exception {

        List<Damage> damage = damageService.getDamageList(memberNo);

        return new ResponseEntity<>(damage, HttpStatus.OK);
    }

    // AI 구동 및 식별값 return
    @PostMapping(value = "/damage/img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> insertCarImage(@RequestPart("carImg") MultipartFile carImg) throws Exception {
        try {
            // AI 매개변수 조건 충족을 위한 이미지 리사이징
            String uploadPath = System.getProperty("user.dir") + File.separator + "carculator" + File.separator + "src" +
                    File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "carDmgSearch" + File.separator;

            if (carImg != null) {
                String originalFileName = carImg.getOriginalFilename();
                String renameFileName = FileUplode.uploadFile(uploadPath, originalFileName, carImg);
                String imagePath = uploadPath + renameFileName;

//                System.out.println("\n\n\nimagePath : " + imagePath);

                String workingDir = "D:\\(1. Java_workspace)\\carculator\\AIDamaged";
//                log.info("workingDir" + workingDir);
                String carAiPy = workingDir + File.separator + "main.py";
//                log.info("carAiPy" + carAiPy);
                String pythonExe = workingDir + File.separator + "venv" + File.separator + "Scripts"
                        + File.separator + "python";
//                log.info("pythonExe" + pythonExe);

                ProcessBuilder pb = new ProcessBuilder(pythonExe, carAiPy, imagePath)
                        .directory(new File(workingDir));

                Process p = pb.redirectErrorStream(true).start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder pythonOutput = new StringBuilder();

                // 한 줄씩 읽어오면서 StringBuilder에 추가
                String line;
                while ((line = reader.readLine()) != null) {
                    pythonOutput.append(line).append("\n");
                }
                p.waitFor();

                String pythonResult = pythonOutput.toString();
//                log.info("\n\nPython Result: " + pythonResult);
                String testcut = DamageStringCut.processString(pythonResult);
//                log.info("\n\ntestcut : " + testcut + "\ntestcut ended");

                return new ResponseEntity<>(testcut, HttpStatus.CREATED);
            } else {
                // 이미지가 없을 경우에 대한 ResponseEntity 반환
                return new ResponseEntity<>("No image provided", HttpStatus.BAD_REQUEST);
            }
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }


}