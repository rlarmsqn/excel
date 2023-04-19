package com.jbt.water;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@SpringBootApplication
public class WaterApplication {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = SpringApplication.run(WaterApplication.class, args);
        WaterService service = context.getBean(WaterService.class);
        log.info("insert start");
        if(service.test().length() == 0) {
            log.info("insert end..");
        }

//		service.insertWaterLevelByPakkangoung();
//		log.info("insertWaterLevelByPakkangoung start");
//		service.insertWaterLevelByPhiengluang();
//		log.info("insertWaterLevelByPhiengluang start");
//		service.insertWaterLevelByVernkham();
//		log.info("insertWaterLevelByVernkham start");
//		service.insertWaterLevelByHNamNgumPhiengluang();
//		log.info("insertWaterLevelByHNamNgumPhiengluang start");
    }

}
