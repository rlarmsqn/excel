package com.jbt.water;

import com.jbt.water.vo.FacilityVO;
import com.jbt.water.vo.RainFallVO;
import com.jbt.water.vo.WaterInfoVO;
import com.jbt.water.vo.WaterVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WaterMapper {
    void insertWaterLevel(List<WaterVO> waterVOList);
    void insertWaterInfo(WaterInfoVO waterInfoVO);
    void insertRainFall(List<RainFallVO> rainFallVOList);
    void insertFacility(List<FacilityVO> facilityVOList);
    List<RainFallVO> selectRainFall();
    List<FacilityVO> selectFacility();
    List<String> selectRainFallId();
    List<Map<String, String>> selectFacilityIdName();
    void deleteRainFall();
    void deleteFacility();
    Integer stationNameCheck(String stationName);
    Integer countRainFall();
    Integer countFacility();
}
