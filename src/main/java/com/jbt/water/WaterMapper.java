package com.jbt.water;

import com.jbt.water.vo.WaterInfoVO;
import com.jbt.water.vo.WaterVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WaterMapper {
    void insertWaterLevel(List<WaterVO> waterVO);
    void insertWaterInfo(WaterInfoVO waterInfoVO);
    Integer stationNameCheck(String stationName);
}
