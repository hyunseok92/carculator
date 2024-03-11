package com.example.carculator.commons;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DamageStringCut {
    public static String processString(String aiResult) {
        log.info("\n\n\n" + aiResult);
        // 'ms'가 나오는 위치까지 자르기
        int msIndex = aiResult.indexOf("ms");
        String firstSubstring = (msIndex != -1) ? aiResult.substring(0, msIndex) : aiResult;

        // 12번째 문자부터 마지막 쉼표(,) 문자까지 자르기
        int startIndex = 12;
        int commaIndex = firstSubstring.lastIndexOf(",");
        String finalResult = (startIndex < commaIndex && commaIndex != -1)
                ? firstSubstring.substring(startIndex, commaIndex)
                : "잘라낼 부분이 없습니다.";
        if (finalResult.contains("most")) {
            finalResult = " ";
        }
        if (finalResult.contains("no detections")) {
            finalResult = " ";
        }
        return finalResult;
    }
}
