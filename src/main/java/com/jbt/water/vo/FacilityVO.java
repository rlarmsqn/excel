package com.jbt.water.vo;

import lombok.Data;


@Data
public class FacilityVO {
    private String id;
    private String name;
    private String ymdHm;
    /**
     * 수위
     */
    private String waterLevel;
    /**
     * 저수량
     */
    private String inflow;
    /**
     * 총방류량
     */
    private String totalDischarge;
    /**
     * 강우량
     */
    private String fall;
    /**
     * 저수율
     */
    private String lowYield;
    /**
     * 저수량
     */
    private String reservoir;
}
