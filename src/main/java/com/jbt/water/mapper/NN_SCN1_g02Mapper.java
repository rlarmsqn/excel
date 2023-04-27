package com.jbt.water.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NN_SCN1_g02Mapper {
    void insertConnections(Map<String, String> data);
    void insertCrossSectionsInterpolationSurfaces(Map<String, String> data);
    void insertCrossSections(Map<String, String> data);
}