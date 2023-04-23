package com.jbt.water.vo;

import lombok.Data;

@Data
public class RainFallVO {
    private String id;
    private String name;
    private String ymdHm;
    private String waterLevel;
    private String inflow;
    private String totalDischarge;
    private String fall;
    private String lowYield;
    private String reservoir;
}
