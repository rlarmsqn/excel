package com.jbt.water;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Slf4j
@RestController
public class HomeController {

    @GetMapping("/hi")
    public String hi() throws GeneralSecurityException, IOException {
        log.info("hi~~~~~~~~~~~~~~~~");
        return "hi";
    }

    @PostMapping("/notifications")
    public ResponseEntity<?> notifications(HttpServletRequest request) throws IOException {
        // 요청 본문 읽기
        BufferedReader reader = request.getReader();
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        String jsonData = requestBody.toString();
        System.out.println(jsonData);
        // 변경 사항 처리 작업
        // jsonData를 파싱하여 필요한 정보 추출
        // ...

        // 필요한 작업 수행

        // 응답 반환
        return ResponseEntity.ok().build();
    }
}
