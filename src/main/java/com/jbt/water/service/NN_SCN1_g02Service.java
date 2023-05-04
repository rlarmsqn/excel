package com.jbt.water.service;

import com.jbt.water.mapper.NN_SCN1_g02Mapper;
import com.jbt.water.util.ReadHdf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        int size = (int) result.get("size");
        for(int i=0; i < size; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("leftBank", ((List<Object>) result.get("leftBank")).get(i));
            map.put("rightBank", ((List<Object>) result.get("rightBank")).get(i));
            map.put("contractionCoef", ((List<Object>) result.get("contractionCoef")).get(i));
            map.put("expansionCoef", ((List<Object>) result.get("expansionCoef")).get(i));
            map.put("startingElevation", ((List<Object>) result.get("startingElevation")).get(i));
            map.put("verticalIncrement", ((List<Object>) result.get("verticalIncrement")).get(i));
            map.put("verticalSlices", ((List<Object>) result.get("verticalSlices")).get(i));
            map.put("lobSlices", ((List<Object>) result.get("lobSlices")).get(i));
            map.put("channelSlices", ((List<Object>) result.get("channelSlices")).get(i));
            map.put("robSlices", ((List<Object>) result.get("robSlices")).get(i));
            if(ineffectiveGeom.get(i) != null) {
                map.put("ineffectiveGeom", ineffectiveGeom.get(i));
            }
            map.put("lengthLob", ((List<Object>) result.get("lengthLob")).get(i));
            map.put("lengthRob", ((List<Object>) result.get("lengthRob")).get(i));
            map.put("lengthChan", ((List<Object>) result.get("lengthChan")).get(i));
            map.put("leftLeveeSta", ((List<Object>) result.get("leftLeveeSta")).get(i));
            map.put("leftLeveeElev", ((List<Object>) result.get("leftLeveeElev")).get(i));
            map.put("rightLeveeSta", ((List<Object>) result.get("rightLeveeSta")).get(i));
            map.put("rightLeveeElev", ((List<Object>) result.get("rightLeveeElev")).get(i));
            map.put("mannings", ((List<Object>) result.get("mannings")).get(i));
            map.put("polyLine", ((List<Object>) result.get("polyLine")).get(i));
            map.put("reachNames", ((List<Object>) result.get("reachNames")).get(i));
            map.put("riverNames", ((List<Object>) result.get("riverNames")).get(i));
            map.put("riverStations", ((List<Object>) result.get("riverStations")).get(i));
            map.put("stationElevation", ((List<Object>) result.get("stationElevation")).get(i));

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
}
