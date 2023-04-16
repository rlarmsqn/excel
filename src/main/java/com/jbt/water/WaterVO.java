package com.jbt.water;

import lombok.Data;

@Data
public class WaterVO {
    private String stationName;
    private String ymd;
    private Double level;
    private String lat;
    private String lon;
    private String province;
    private String country;
    private String agency;
    private String river;
    private String altitude;
    private String catchmentArea;
}
