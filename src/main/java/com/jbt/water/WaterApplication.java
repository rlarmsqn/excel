package com.jbt.water;

import com.jbt.water.service.NN_SCN1_g02Service;
import com.jbt.water.service.WaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@Slf4j
@SpringBootApplication
public class WaterApplication {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = SpringApplication.run(WaterApplication.class, args);
        WaterService service = context.getBean(WaterService.class);
        NN_SCN1_g02Service NNSCN1g02Service = context.getBean(NN_SCN1_g02Service.class);
        /*log.info("insert start");
        if(service.insertData().length() == 0) {
            log.info("insert end..");
        }*/

//        service.insertRainFall();
//        service.generateRainFallFile();
//        service.insertFacility();
//        service.generateFacilityFile();
//        NNSCN1g02Service.insertConnections();
//        NNSCN1g02Service.insertCrossSectionsInterpolationSurfaces();
//        NNSCN1g02Service.insertCrossSections();
//        NNSCN1g02Service.insertJunctions();
//        NNSCN1g02Service.insertRiverBankLines();
//        NNSCN1g02Service.insertRiverCenterLines();
//        NNSCN1g02Service.insertRiverEdgeLines();
//        NNSCN1g02Service.insertStorageAreas();
        NNSCN1g02Service.insertStructures();
    }
}
