package com.jbt.water;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Slf4j
@org.springframework.stereotype.Controller
public class Controller {

    @RequestMapping("/")
    public String home() throws GeneralSecurityException, IOException {
//        GoogleDrive.upload();
        return "index";
    }
}