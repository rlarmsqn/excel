package com.jbt.water.service;

import com.jbt.water.mapper.NN_SCN1_g02Mapper;
import com.jbt.water.util.ReadHdf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        String fileName  = "C:\\Users\\srmsq\\Desktop\\waterdata\\NN_SCN1.g02.hdf";
        String groupName = "Geometry/Cross_Sections";
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
        Map<String, Object> result = readHdf.crossSectionsHdf(fileName, groupName, bank, blockedIneffectiveInfo, blockedIneffectiveValues,
                contrExpanCoef, HTSEIS, HTVHS, ineffectiveBlocks, ineffectiveInfo, lengths, levees, manningInfo, manningValue, polylineInfo, polylinePoints, reachNames,
                riverNames, riverStations, stationElevationInfo, stationElevationValues, stationManningInfo, stationManningValues);


        Map<Integer, String> ineffectiveGeom = (Map<Integer, String>) result.get("ineffectiveGeom");

        List<String> leftBank = (List<String>) result.get("leftBank");
        List<String> rightBank = (List<String>) result.get("rightBank");
        List<String> contractionCoef = (List<String>) result.get("contractionCoef");
        List<String> expansionCoef = (List<String>) result.get("expansionCoef");
        List<String> startingElevation = (List<String>) result.get("startingElevation");
        List<String> verticalIncrement = (List<String>) result.get("verticalIncrement");
        List<String> verticalSlices = (List<String>) result.get("verticalSlices");
        List<String> lobSlices = (List<String>) result.get("lobSlices");
        List<String> channelSlices = (List<String>) result.get("channelSlices");
        List<String> robSlices = (List<String>) result.get("robSlices");
        List<String> lengthLob = (List<String>) result.get("lengthLob");
        List<String> lengthRob = (List<String>) result.get("lengthRob");
        List<String> lengthChan = (List<String>) result.get("lengthChan");
        List<String> leftLeveeSta = (List<String>) result.get("leftLeveeSta");
        List<String> leftLeveeElev = (List<String>) result.get("leftLeveeElev");
        List<String> rightLeveeSta = (List<String>) result.get("rightLeveeSta");
        List<String> rightLeveeElev = (List<String>) result.get("rightLeveeElev");
        List<String> mannings = (List<String>) result.get("mannings");
        List<String> polyLine = (List<String>) result.get("polyLine");
        List<String> reachName = (List<String>) result.get("reachNames");
        List<String> riverName = (List<String>) result.get("riverNames");
        List<String> riverStation = (List<String>) result.get("riverStations");
        List<String> stationElevation = (List<String>) result.get("stationElevation");

        int size = (int) result.get("size");
        for(int i=0; i < size; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", i + 1);
            map.put("leftBank", leftBank.get(i));
            map.put("rightBank", rightBank.get(i));
            map.put("contractionCoef", contractionCoef.get(i));
            map.put("expansionCoef", expansionCoef.get(i));
            map.put("startingElevation", startingElevation.get(i));
            map.put("verticalIncrement", verticalIncrement.get(i));
            map.put("verticalSlices", verticalSlices.get(i));
            map.put("lobSlices", lobSlices.get(i));
            map.put("channelSlices", channelSlices.get(i));
            map.put("robSlices", robSlices.get(i));
//            if(ineffectiveGeom.get(i) != null) {
                map.put("ineffectiveGeom", ineffectiveGeom.get(i));
//            }
            map.put("lengthLob", lengthLob.get(i));
            map.put("lengthRob", lengthRob.get(i));
            map.put("lengthChan", lengthChan.get(i));
            map.put("leftLeveeSta", leftLeveeSta.get(i));
            map.put("leftLeveeElev", leftLeveeElev.get(i));
            map.put("rightLeveeSta", rightLeveeSta.get(i));
            map.put("rightLeveeElev", rightLeveeElev.get(i));
            map.put("mannings", mannings.get(i));
            map.put("polyLine", polyLine.get(i));
            map.put("reachNames", reachName.get(i));
            map.put("riverNames", riverName.get(i));
            map.put("riverStations", riverStation.get(i));
            map.put("stationElevation", stationElevation.get(i));

            NNSCN1g02Mapper.insertCrossSections(map);
        }

    }

    public void insertJunctions() {
        String fileName  = "C:\\Users\\srmsq\\Desktop\\waterdata\\NN_SCN1.g02.hdf";
        String groupName = "Geometry/Junctions";
        String names = "Names";
        String pointsName = "Points";

        ReadHdf readHdf = new ReadHdf();
        Map<String, String> data = readHdf.junctionsReadHdf(fileName, groupName, names, pointsName);

        NNSCN1g02Mapper.insertJunctions(data);

    }

    public void insertRiverBankLines() {
        String fileName  = "C:\\Users\\srmsq\\Desktop\\waterdata\\NN_SCN1.g02.hdf";
        String groupName = "Geometry/River_Bank_Lines";
        String infoName = "Bank_Lines_Info";
        String pointsName = "Bank_Lines_Points";

        ReadHdf readHdf = new ReadHdf();
        List<String> data = readHdf.riverBankLinesReadHdf(fileName, groupName, infoName, pointsName);

        NNSCN1g02Mapper.insertRiverBankLines(data);
    }

    public void insertRiverCenterLines() {
        String fileName  = "C:\\Users\\srmsq\\Desktop\\waterdata\\NN_SCN1.g02.hdf";
        String groupName = "Geometry/River_Centerlines";
        String dsJunction = "DS_Junction";
        String dsSa_2D = "DS_SA-2D";
        String polyLineInfo = "Polyline_Info";
        String polyLinePoints = "Polyline_Points";
        String reachName = "Reach_Names";
        String riverName = "River_Names";
        String usJunction = "US_Junction";
        String usSa_2D = "US_SA-2D";

        ReadHdf readHdf = new ReadHdf();
        List<Map<String, String>> data = readHdf.riverCenterlinesReadHdf(fileName, groupName, dsJunction, dsSa_2D, polyLineInfo, polyLinePoints,
                                                          reachName, riverName, usJunction, usSa_2D);

        NNSCN1g02Mapper.insertRiverCenterLines(data);
    }

    public void insertRiverEdgeLines() {
        String fileName  = "C:\\Users\\srmsq\\Desktop\\waterdata\\NN_SCN1.g02.hdf";
        String groupName = "Geometry/River_Edge_Lines";
        String infoName = "Polyline_Info";
        String pointName = "Polyline_Points";
        ReadHdf readHdf = new ReadHdf();
        List<String> data = readHdf.riverEdgeLinesReadHdf(fileName, groupName, infoName, pointName);

        NNSCN1g02Mapper.insertRiverEdgeLines(data);

    }

    public void insertStorageAreas() {
        String fileName  = "C:\\Users\\srmsq\\Desktop\\waterdata\\NN_SCN1.g02.hdf";
        String groupName = "Geometry/Storage_Areas";
        String names = "Names";
        String polygonAreaName = "Polygon_Area";
        String polygonInfoName = "Polygon_Info";
        String polygonPointsName = "Polygon_Points";
        String volumeElevationInfoName = "Volume_Elevation_Info";
        String volumeElevationValuesName = "Volume_Elevation_Values";

        ReadHdf readHdf = new ReadHdf();
        List<Map<String, Object>> data = readHdf.storageAreasReadHdf(fileName, groupName, names, polygonAreaName, polygonInfoName,
                polygonPointsName, volumeElevationInfoName, volumeElevationValuesName);

        for(int i=0; i < data.size(); i++) {
//            System.out.println(data.get(i).get("polygonArea"));
        }
        NNSCN1g02Mapper.insertStorageAreas(data);

    }

    public void insertStructures() {
        String fileName  = "C:\\Users\\srmsq\\Desktop\\waterdata\\NN_SCN1.g02.hdf";
        String groupName = "Geometry/Structures";

        ReadHdf readHdf = new ReadHdf();
        Map<String, Object> data = readHdf.structuresReadHdf(fileName, groupName);
        Map<String,String> centerLineList = (Map<String,String>) data.get("centerLine");
        List<Map<String,String>> peirDataList = (List<Map<String,String>>) data.get("peirData");
        Map<String, String> profileDataList = (Map<String,String>) data.get("profileData");
        List<Map<String,String>> udwcDataList = (List<Map<String,String>>) data.get("udwcData");
        int size = Integer.parseInt(String.valueOf(data.get("size")));


        // Pier Data
        for(int i=0; i < peirDataList.size(); i++) {
//            NNSCN1g02Mapper.insertPierData(peirDataList.get(i));
        }

        // User Defined Weir Connectivity Data
        for(int i=0; i < udwcDataList.size(); i++) {
//            NNSCN1g02Mapper.insertUserDefinedWeirConnectivity(udwcDataList.get(i));
        }

        for(int i=0; i < size; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(i));
            map.put("centerLineGeom", centerLineList.get(String.valueOf(i)));
            map.put("profileGeom", profileDataList.get(String.valueOf(i)));

//            System.out.println(i + " / " + map);

            NNSCN1g02Mapper.insertStructuresGeom(map);
        }

    }
}
