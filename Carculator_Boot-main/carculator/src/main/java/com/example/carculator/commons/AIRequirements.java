package com.example.carculator.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

@Slf4j
public class AIRequirements {

    // Run Python AI
    public String aiPredict(MultipartFile resizedImage) throws Exception {
        // 이미지 저장 위치 : 환경에 따라 수정필요
        String uploadPath = System.getProperty("user.dir") + "\\carculator\\src\\main\\resources\\static\\carImages\\";

        String originalFileName = resizedImage.getOriginalFilename();
        String renameFileName = FileUplode.uploadFile(uploadPath, originalFileName, resizedImage);

        String imagePath = uploadPath + renameFileName;

        // Python AI Project가 있는 경로지정 (환경에 따라 수정 필요)
        String workingDir = "D:\\(1. Java_workspace)\\carculator\\YOLOv8_Project";
//        String workingDir = "..\\..\\..\\..\\..\\..\\..\\..\\..\\YOLOv8_Project";
        // Project 내부 실행코드가 들어있는 파일위치 경로 (환경에 따라 수정 필요)
        String carAiPy = workingDir + File.separator + "run.py";
        // Python Project 내부의 실행기 위치 (수정 불필요)
        String pythonExe = workingDir + File.separator + "venv" + File.separator + "Scripts"
                + File.separator + "python";

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
        String carModel = (pythonResult.substring(13, pythonResult.indexOf(','))).trim();
        log.info("\n\n\ncarModel : " + carModel);
        return carModel;
    }

    // Image Resizing
    public MultipartFile resize(MultipartFile carImg) throws IOException {
        byte[] resizedImageData = null;

        BufferedImage image = ImageIO.read(carImg.getInputStream());

        int targetWidth = 800;
        int targetHeight = 600; // 자동으로 조정됨
        Image scaledImage = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

        BufferedImage resizedImage = new BufferedImage(targetWidth, scaledImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        baos.flush();
        resizedImageData = baos.toByteArray();
        baos.close();

        // MultipartFile 객체 생성
        byte[] finalResizedImageData = resizedImageData;
        MultipartFile resizedFile = new MultipartFile() {
            @Override
            public String getName() {
                return carImg.getName();
            }

            @Override
            public String getOriginalFilename() {
                return carImg.getOriginalFilename();
            }

            @Override
            public String getContentType() {
                return carImg.getContentType();
            }

            @Override
            public boolean isEmpty() {
                return finalResizedImageData == null || finalResizedImageData.length == 0;
            }

            @Override
            public long getSize() {
                return finalResizedImageData.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return finalResizedImageData;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(finalResizedImageData);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                throw new UnsupportedOperationException("transferTo() is not supported for this MultipartFile.");
            }
        };

        return resizedFile;
    }
}