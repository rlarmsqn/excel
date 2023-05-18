package com.jbt.water;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@RestController
public class HomeController {

    @GetMapping("/hi")
    public String hi() {
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
