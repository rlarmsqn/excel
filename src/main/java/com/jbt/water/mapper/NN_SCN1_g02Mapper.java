package com.jbt.water.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NN_SCN1_g02Mapper {
    void insertConnections(Map<String, String> data);
    void insertCrossSectionsInterpolationSurfaces(Map<String, String> data);
    void insertCrossSections(Map<String, Object> data);
    void insertJunctions(Map<String, String> data);
    void insertRiverBankLines(List<String> data);
    void insertRiverCenterLines(List<Map<String, String>> data);
    void insertRiverEdgeLines(List<String> data);
    void insertStorageAreas(List<Map<String, Object>> data);
    void insertStructuresGeom(Map<String, String> data);
    void insertPierData(Map<String, String> data);
    void insertUserDefinedWeirConnectivity(Map<String, String> data);
}
