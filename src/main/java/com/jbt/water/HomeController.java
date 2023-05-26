package com.jbt.water;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.jbt.water.vo.PushVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class HomeController {

    @GetMapping("/hii")
    public String hii() {
        return "hii";
    }

    @GetMapping("/hi")
    public String hi() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        log.info(GoogleDrive.getCredentials(HTTP_TRANSPORT).getAccessToken());
        log.info("hi~~~~~~~~~~~~~~~~");
        GoogleDrive.push();
        return "hi";
    }

    @PostMapping("/notifications")
    public void notifications(HttpServletRequest request, HttpServletResponse response) throws IOException, GeneralSecurityException {
//        log.info("getInputStream : " + request.getInputStream());
        GoogleDrive.driveChangeFileLog(GoogleDrive.fetchStartPageToken());
//        String requestBody = new BufferedReader(new InputStreamReader(request.getInputStream()))
//                .lines()
//                .collect(Collectors.joining(System.lineSeparator()));
//        log.info(request.getHeader("X-Goog-Resource-ID"));
        log.info(request.getHeader("X-Goog-Resource-State"));
//        log.info(request.getHeader("X-Goog-Channel-Expiration"));
        log.info(request.getHeader("X-Goog-Changed"));
//        log.info(request.getHeader("X-Goog-Channel-ID"));
        // requestBody에는 푸시 알림의 본문 내용이 담겨있습니다.
        // 필요한 정보를 추출하고 처리할 수 있습니다.
//        log.info("Received notification body: " + requestBody);
//        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//        log.info("requestBody : "+requestBody);
//        log.info("response.getWriter() : " + response.getWriter());
        // 요청 본문 읽기
//        log.info(request.getServerName() + " / " +
//        request.getServerPort());
//        log.info(String.valueOf(response.getWriter()));

//        BufferedReader reader = request.getReader();
//        StringBuilder requestBody = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            requestBody.append(line);
//        }
//        String jsonData = requestBody.toString();

//        log.info("getQueryString : " + request.getQueryString());
//        log.info("getLocalAddr : " + request.getLocalAddr());
//        log.info("getLocalPort : " + request.getLocalPort());
//        log.info("getHttpServletMapping : " + request.getHttpServletMapping());
//        log.info("getTrailerFields : " + request.getTrailerFields());
//        log.info("getMethod : " + request.getMethod());
//        log.info("getParameter(kind) : " + request.getParameter("kind"));
//        log.info("getParameterValues : " + Arrays.toString(request.getParameterValues("kind")));
//        log.info("getContentLength : " + String.valueOf(request.getContentLength()));
//        log.info("getHeaderNames : " + String.valueOf(request.getHeaderNames()));

        // 변경 사항 처리 작업
        // jsonData를 파싱하여 필요한 정보 추출
        // ...

        // 필요한 작업 수행

        // 응답 반환
    }
}
