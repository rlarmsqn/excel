package com.jbt.water.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WaterInfoVO {
   private String stationName;
   private String lat;
   private String lon;
   private String river;
   private String altitude;
   private String catchmentArea;
   private String province;
   private String country;
   private String agency;
}
