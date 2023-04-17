package com.jbt.water.vo;

import lombok.Data;

import java.util.UUID;

@Data
public class WaterVO {
    private String stationName;
    private String ymd;
    private Double level;
}
