package com.jbt.water;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WaterMapper {
    void insertWaterLevel(List<WaterVO> waterVO);
}
