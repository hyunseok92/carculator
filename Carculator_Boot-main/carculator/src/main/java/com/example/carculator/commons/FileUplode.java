package com.example.carculator.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
@Slf4j
public class FileUplode {
    public static String uploadFile(String filePath, String originalName, MultipartFile fileData) throws Exception {
        UUID uid = UUID.randomUUID();
        log.info(fileData.toString());
        String fileName = uid.toString() + "_" + originalName;
        Files.createDirectories(Path.of(filePath));
        Path upfilePath = Path.of(filePath, fileName);
        Files.copy(fileData.getInputStream(), upfilePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public static String uploadProFile(String filePath, String fileName, MultipartFile fileData) throws Exception {
        UUID uid = UUID.randomUUID();
        log.info(fileData.toString());
        Files.createDirectories(Path.of(filePath));
        Path upfilePath = Path.of(filePath, fileName);
        Files.copy(fileData.getInputStream(), upfilePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}
