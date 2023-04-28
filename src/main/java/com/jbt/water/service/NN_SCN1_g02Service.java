package com.jbt.water.service;

import com.jbt.water.mapper.NN_SCN1_g02Mapper;
import com.jbt.water.util.ReadHdf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NN_SCN1_g02Service {

    private final NN_SCN1_g02Mapper NNSCN1g02Mapper;

    public void insertConnections() {
        String fileName  = "C:\\Users\\srmsq\\Desktop\\waterdata\\NN_SCN1.g02.hdf";
        String groupName = "Geometry/Connections";
        String connectionName = "Names";
        String infoName = "Polyline_Info";
        String pointName = "Polyline_Points";
        ReadHdf readHdf = new ReadHdf();
        List<Map<String, String>> result = readHdf.connectionReadHdf(fileName, groupName, infoName, pointName, connectionName);

        for(Map<String, String> s : result) {
            NNSCN1g02Mapper.insertConnections(s);
        }
    }

    public void insertCrossSectionsInterpolationSurfaces() {
        String fileName  = "C:\\Users\\srmsq\\Desktop\\waterdata\\NN_SCN1.g02.hdf";
        String groupName = "Geometry/Cross_Section_Interpolation_Surfaces";
        String infoName = "TIN_Info";
        String pointName = "TIN_Points";
        String triangleName = "TIN_Triangles";
        String xsidsName = "XSIDs";
        ReadHdf readHdf = new ReadHdf();
        List<Map<String, String>> result = readHdf.surfacesReadHdf(fileName, groupName, infoName, pointName, triangleName, xsidsName);

        for(Map<String, String> s : result) {
            System.out.println(s.get("usxsId") + " " + s.get("dsxsId"));
            System.out.println(s.get("threed_geom"));
            System.out.println(s.get("triangle_geom"));

            NNSCN1g02Mapper.insertCrossSectionsInterpolationSurfaces(s);
        }
    }

    public void insertCrossSections() {
        String file  = "C:\\Users\\srmsq\\Desktop\\waterdata\\NN_SCN1.g02.hdf";
        String group = "Geometry/Cross_Sections";
        String bank = "Bank_Stations";
        String blockedIneffectiveInfo = "Blocked_Ineffective_Info";
        String blockedIneffectiveValues = "Blocked_Ineffective_Values";
        String contrExpanCoef = "Contr_Expan_Coef";
        String HTSEIS = "Hydraulic_Tables_Starting_Elevation_and_Increment_Size";
        String HTVHS = "Hydraulic_Tables_Vertical_and_Horizontal_Slices";
        String ineffectiveBlocks = "Ineffective_Blocks";
        String ineffectiveInfo = "Ineffective_Info";
        String lengths = "Lengths";
        String levees = "Levees";
        String manningInfo = "Manning's_n_Info";
        String manningValue = "Manning's_n_Values";
        String polylineInfo = "Polyline_Info";
        String polylinePoints = "Polyline_Points";
        String reachNames = "Reach_Names";
        String riverNames = "River_Names";
        String riverStations = "River_Stations";
        String stationElevationInfo = "Station_Elevation_Info";
        String stationElevationValues = "Station_Elevation_Values";
        String stationManningInfo = "Station_Manning's_n_Info";
        String stationManningValues = "Station_Manning's_n_Values";

        ReadHdf readHdf = new ReadHdf();
        readHdf.crossSectionsHdf(file, group, bank, blockedIneffectiveInfo, blockedIneffectiveValues,
                contrExpanCoef, HTSEIS, HTVHS, ineffectiveBlocks, ineffectiveInfo, lengths, levees, manningInfo, manningValue, polylineInfo, polylinePoints, reachNames,
                riverNames, riverStations, stationElevationInfo, stationElevationValues, stationManningInfo, stationManningValues);
//        List<Map<String, String>> result = readHdf.crossSectionsHdf(file, group, bank, blockedIneffectiveInfo, blockedIneffectiveValues,
//                contrExpanCoef, HTSEIS, HTVHS, lengths, levees, manningInfo, manningValue, polylineInfo, polylinePoints, reachNames,
//                riverNames, riverStations, stationElevationInfo, stationElevationValues, stationManningInfo, stationManningValues);

//        for(Map<String, String> data : result) {
//            System.out.println(data);
//        }
    }
}
