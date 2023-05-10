package com.jbt.water.util;

import jdk.swing.interop.SwingInterOpUtils;
import org.apache.commons.lang3.StringUtils;
import ucar.ma2.Array;
import ucar.ma2.Index;
import ucar.nc2.Group;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadHdf {
    public List<Map<String, String>> connectionReadHdf(String fileName, String groupName, String infoName, String pointName, String names) {

        NetcdfFile dataFile = null;

        List<Map<String, String>> result = new ArrayList<>();

        try {
            dataFile = NetcdfFile.open(fileName);

            Group group = dataFile.findGroup(groupName);

            //추가
            Variable polyNames = dataFile.findVariable(group, names);
            Array polyNamesArr = polyNames.read();
            String[] s = StringUtils.split(String.valueOf(polyNamesArr),",");
            //추가

            Variable polylineInfo = dataFile.findVariable(group, infoName);
            Array polylineInfoArr = polylineInfo.read();

            Variable polylinePoints = dataFile.findVariable(group, pointName);
            Array polylinePointsArr = polylinePoints.read();
            Index polylinePointsIdx = polylinePointsArr.getIndex();


            int[] polylineInfoShape = polylineInfoArr.getShape();
            Index polylineInfoIdx = polylineInfoArr.getIndex();


            for(int i = 0, iCount = polylineInfoShape[0]; i < iCount; i++) {
                for(int j = 0, jCount = polylineInfoShape[1]; j < jCount; j=j+4) {

                    int pointStartIdx = polylineInfoArr.getInt(polylineInfoIdx.set(i, j));
                    int pointCount = polylineInfoArr.getInt(polylineInfoIdx.set(i, j+1));
                    int partStartIdx = polylineInfoArr.getInt(polylineInfoIdx.set(i, j+2));
                    int partCount = polylineInfoArr.getInt(polylineInfoIdx.set(i, j+3));

//                    System.out.println(i + " => " + pointStartIdx + ", " + pointCount + ", " + partStartIdx + ", " + partCount);

                    StringBuffer sbGeom = new StringBuffer("LINESTRING(");
                    Map<String, String> map = new HashMap<>();

                    for(int idx = 0; idx < pointCount; idx++) {

                        sbGeom.append(polylinePointsArr.getDouble(polylinePointsIdx.set(pointStartIdx+idx, 0)));
                        sbGeom.append(" ");
                        sbGeom.append(polylinePointsArr.getDouble(polylinePointsIdx.set(pointStartIdx+idx, 1)));

                        if(idx < (pointCount-1)) {
                            sbGeom.append(", ");
                        }
                    }
                    sbGeom.append(")");
//                    System.out.println(sbGeom.toString());
                    map.put("id",s[i].trim());
                    map.put("geom", sbGeom.toString());
                    result.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (dataFile != null) {
                try {
                    dataFile.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }

        return result;
    }

    public List<Map<String, String>> surfacesReadHdf(String fileName, String groupName, String infoName, String pointName, String trianglesName, String xsidsName) {

        NetcdfFile dataFile = null;

        List<Map<String, String>> result = new ArrayList<>();

        try {
            dataFile = NetcdfFile.open(fileName);

            Group group = dataFile.findGroup(groupName);

            Variable tinInfo = dataFile.findVariable(group, infoName);
            Array tinInfoArr = tinInfo.read();

            Variable tinPoints = dataFile.findVariable(group, pointName);
            Array tinPointsArr = tinPoints.read();
            Index tinPointsIdx = tinPointsArr.getIndex();


            Variable tinTriangles = dataFile.findVariable(group, trianglesName);
            Array tinTriangleArr = tinTriangles.read();
            Index tinTriangleIdx = tinTriangleArr.getIndex();


            Variable xsidsVariable = dataFile.findVariable(group, xsidsName);
            Array xsidsArr = xsidsVariable.read();
            String[] xsidsValue = StringUtils.split(String.valueOf(xsidsArr)," ");
            List<String> usxsid = new ArrayList<>();
            List<String> dsxsid = new ArrayList<>();
            for(int i=0; i < xsidsValue.length; i++) {
                if(i%2 == 0) {
                    usxsid.add(xsidsValue[i]);
                } else {
                    dsxsid.add(xsidsValue[i]);
                }
            }

            int[] tinInfoShape = tinInfoArr.getShape();
            Index tinInfoIdx = tinInfoArr.getIndex();


            for(int i = 0, iCount = tinInfoShape[0]; i < iCount; i++) {
                for(int j = 0, jCount = tinInfoShape[1]; j < jCount; j=j+4) {
                    int pointStartIdx = tinInfoArr.getInt(tinInfoIdx.set(i, j));
                    int pointCount = tinInfoArr.getInt(tinInfoIdx.set(i, j+1));
                    int triangleStartIdx = tinInfoArr.getInt(tinInfoIdx.set(i, j+2));
                    int triangleCount = tinInfoArr.getInt(tinInfoIdx.set(i, j+3));

//                    System.out.println(i + " => " + pointStartIdx + ", " + pointCount + ", " + triangleStartIdx + ", " + triangleCount);

                    StringBuffer sbGeom = new StringBuffer("MULTIPOINT ZM(");
                    StringBuffer triangleGeom = new StringBuffer("MULTIPOINT Z(");
                    Map<String, String> map = new HashMap<>();

                    for(int idx = 0; idx < pointCount; idx++) {

                        sbGeom.append(tinPointsArr.getDouble(tinPointsIdx.set(pointStartIdx+idx, 0)));
                        sbGeom.append(" ");
                        sbGeom.append(tinPointsArr.getDouble(tinPointsIdx.set(pointStartIdx+idx, 1)));
                        sbGeom.append(" ");
                        sbGeom.append(tinPointsArr.getDouble(tinPointsIdx.set(pointStartIdx+idx, 2)));
                        sbGeom.append(" ");
                        sbGeom.append(tinPointsArr.getDouble(tinPointsIdx.set(pointStartIdx+idx, 3)));

                        if(idx < (pointCount-1)) {
                            sbGeom.append(", ");
                        }
                    }

                    for(int idx = 0; idx < triangleCount; idx++) {
                        triangleGeom.append(tinTriangleArr.getInt(tinTriangleIdx.set(triangleStartIdx+idx, 0)));
                        triangleGeom.append(" ");
                        triangleGeom.append(tinTriangleArr.getInt(tinTriangleIdx.set(triangleStartIdx+idx, 1)));
                        triangleGeom.append(" ");
                        triangleGeom.append(tinTriangleArr.getInt(tinTriangleIdx.set(triangleStartIdx+idx, 2)));

                        if(idx < (triangleCount-1)) {
                            triangleGeom.append(", ");
                        }

                    }

                    sbGeom.append(")");
                    triangleGeom.append(")");
//                    System.out.println(sbGeom);
//                    System.out.println(triangleGeom);
//                    System.out.println(usxsid.get(i) + " " + dsxsid.get(i));
                    map.put("threed_geom", sbGeom.toString());
                    map.put("usxsId", usxsid.get(i));
                    map.put("dsxsId", dsxsid.get(i));
                    map.put("triangle_geom", triangleGeom.toString());

                    result.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (dataFile != null) {
                try {
                    dataFile.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }

        return result;
    }

    public Map<String, Object> crossSectionsHdf(String fileName, String groupName, String bankName, String biiName, String bivName, String cecName,
                                                      String htseisName, String htvhsName, String ineffectiveBlocksName, String ineffectiveInfoName, String lengthsName, String leveesName, String miName, String mvName,
                                                      String piName, String ppName, String reachName, String riverName, String riverSName, String seiName, String sevName,
                                                      String smiName, String smvName) {

        NetcdfFile dataFile = null;

        Map<String, Object> result = new HashMap<>();
        
        try {
            dataFile = NetcdfFile.open(fileName);

            Group group = dataFile.findGroup(groupName);

            // Bank Stations
            Variable bank = dataFile.findVariable(group, bankName);
            Array bankArr = bank.read();
            String[] bankValue = StringUtils.split(String.valueOf(bankArr)," ");
            List<String> leftBankList = new ArrayList<>();
            List<String> rightBankList = new ArrayList<>();
            for(int i=0; i < bankValue.length; i++) {
                if(i%2 == 0) {
                    leftBankList.add(bankValue[i]);
                } else {
                    rightBankList.add(bankValue[i]);
                }
            }

            // Blocked Ineffective = ineffective Blocks
            /*Variable blInfo = dataFile.findVariable(group, biiName);
            Array blInfoArr = blInfo.read();

            Variable bivValues = dataFile.findVariable(group, bivName);
            Array bivArr = bivValues.read();
            Index bivArrIdx = bivArr.getIndex();

            int[] blInfoShape = blInfoArr.getShape();
            Index blInfoIdx =  blInfoArr.getIndex();

            List<Map<String, String>> bivValueList = new ArrayList<>();
            List<Map<String, String>> ineffectiveLeft = new ArrayList<>();
            List<Map<String, String>> ineffectiveRight = new ArrayList<>();
            List<Map<String, String>> ineffectiveElevation = new ArrayList<>();
            for(int i = 0, iCount = blInfoShape[0]; i < iCount; i++) {
                for (int j = 0, jCount = blInfoShape[1]; j < jCount; j = j + 2) {
                    int bivIdx = blInfoArr.getInt(blInfoIdx.set(i, j));
                    int bivCount = blInfoArr.getInt(blInfoIdx.set(i, j + 1));

                    for (int idx = 0; idx < bivCount; idx++) {
                        Map<String, String> map1 = new HashMap<>();
                        Map<String, String> map2 = new HashMap<>();
                        Map<String, String> map3 = new HashMap<>();
                        map1.put(String.valueOf(i), String.valueOf(bivArr.getDouble(bivArrIdx.set(bivIdx + idx, 0))));
                        map2.put(String.valueOf(i), String.valueOf(bivArr.getDouble(bivArrIdx.set(bivIdx + idx, 1))));
                        map3.put(String.valueOf(i), String.valueOf(bivArr.getDouble(bivArrIdx.set(bivIdx + idx, 2))));
                        ineffectiveLeft.add(map1);
                        ineffectiveRight.add(map2);
                        ineffectiveElevation.add(map3);
                        bivValueList.add(map);
                    }
                }
            }*/

            // Contr Expan Coef
            Variable cec = dataFile.findVariable(group, cecName);
            Array cecArr = cec.read();
            String[] cecValue = StringUtils.split(String.valueOf(cecArr)," ");
            List<String> contractionCoefList = new ArrayList<>();
            List<String> expansionCoefList = new ArrayList<>();
            for(int i=0; i < cecValue.length; i++) {
                if(i%2 == 0) {
                    contractionCoefList.add(cecValue[i]);
                } else {
                    expansionCoefList.add(cecValue[i]);
                }
            }

            // Hydraulic Tables Starting Elevation and Increment Size
            Variable htseis = dataFile.findVariable(group, htseisName);
            Array htseisArr = htseis.read();
            String[] htseisValue = StringUtils.split(String.valueOf(htseisArr)," ");
            List<String> startingElevationList = new ArrayList<>();
            List<String> verticalIncrementList = new ArrayList<>();
            for(int i=0; i < htseisValue.length; i++) {
                if(i%2 == 0) {
                    startingElevationList.add(htseisValue[i]);
                } else {
                    verticalIncrementList.add(htseisValue[i]);
                }
            }

            // Hydraulic Tables Vertical and Horizontal Slices
            Variable htvhs = dataFile.findVariable(group, htvhsName);
            Array htvhsArr = htvhs.read();
            String[] htvhsValue = StringUtils.split(String.valueOf(htvhsArr)," ");
            List<String> verticalSlicesList = new ArrayList<>();
            List<String> lobSlicesList = new ArrayList<>();
            List<String> channelSlicesList = new ArrayList<>();
            List<String> robSlicesList = new ArrayList<>();
            int lobSlicesIdx = 1;
            int channelSlicesIdx = 2;
            for(int i=0; i < htvhsValue.length; i++) {
                if(i%4 == 0) {
                    verticalSlicesList.add(htvhsValue[i]);
                } else if(lobSlicesIdx == i) {
                    lobSlicesList.add(htvhsValue[i]);
                    lobSlicesIdx += 4;
                } else if(channelSlicesIdx == i) {
                    channelSlicesList.add(htvhsValue[i]);
                    channelSlicesIdx += 4;
                } else {
                    robSlicesList.add(htvhsValue[i]);
                }
            }

            // Ineffective Blocks
            Variable ineffectivInfo = dataFile.findVariable(group, ineffectiveInfoName);
            Array ineffectivInfoArr = ineffectivInfo.read();

            Variable ineffectiveBlocks = dataFile.findVariable(group, ineffectiveBlocksName);
            Array ineffectiveBlocksArr = ineffectiveBlocks.read();
            Index ineffectiveBlocksIdx = ineffectiveBlocksArr.getIndex();

            List<String> ineffectiveBlocksValues = new ArrayList<>();
            int cnt = 0;
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < ineffectiveBlocksArr.getSize(); k++) {
                ucar.ma2.StructureData sd = (ucar.ma2.StructureData) ineffectiveBlocksArr.getObject(k);

                for (ucar.ma2.StructureMembers.Member m : sd.getStructureMembers().getMembers()) {
                    sb.append(sd.getScalarObject(m) + " ");
                    cnt++;
                    if(cnt%4 == 0) {
                        ineffectiveBlocksValues.add(sb.toString().trim());
                        sb.setLength(0);
                    }
                }
            }

            int[] ineffectivInfoShape = ineffectivInfoArr.getShape();
            Index ineffectivInfoIdx =  ineffectivInfoArr.getIndex();

//            List<Map<String, String>> ineffectiveList = new ArrayList<>();
//            List<Map<String, String>> ineffectiveLeftList = new ArrayList<>();
//            List<Map<String, String>> ineffectiveRightList = new ArrayList<>();
//            List<Map<String, String>> ineffectiveElevationList = new ArrayList<>();
//            List<Map<String, String>> ineffectivePermanentList = new ArrayList<>();
            int cnt2 = 0;
            Map<Integer, String> ineffectiveBlocksMap = new HashMap<>();
            for(int i = 0, iCount = ineffectivInfoShape[0]; i < iCount; i++) {
                for (int j = 0, jCount = ineffectivInfoShape[1]; j < jCount; j = j + 4) {
                    int ineffectivCount = ineffectivInfoArr.getInt(ineffectivInfoIdx.set(i, j + 1));
                    StringBuilder sbGeom = new StringBuilder("MULTIPOINT ZM(");

                    Map<Integer, String> map = new HashMap<>();

                    for (int idx = 0; idx < ineffectivCount; idx++) {

                        sbGeom.append(ineffectiveBlocksValues.get(cnt2));
//                        sbGeom.append(" ");
//                        sbGeom.append(ineffectiveBlocksArr.getDouble(ineffectiveBlocksIdx.set(ineffectivStartIdx+idx, 1)));
//                        sbGeom.append(" ");
//                        sbGeom.append(ineffectiveBlocksArr.getDouble(ineffectiveBlocksIdx.set(ineffectivStartIdx+idx, 2)));
//                        sbGeom.append(" ");
//                        sbGeom.append(ineffectiveBlocksArr.getDouble(ineffectiveBlocksIdx.set(ineffectivStartIdx+idx, 3)));
                        if(idx > (ineffectivCount-1)) {
                            sbGeom.append(" ");
                        }

                        if(idx < (ineffectivCount-1)) {
                            sbGeom.append(", ");
                        }
//                        Map<String, String> map = new HashMap<>();

//                        map.put(String.valueOf(i), ineffectiveBlocksValues.get(cnt2));
//                        ineffectiveList.add(map);
                      /*  Map<String, String> map1 = new HashMap<>();
                        Map<String, String> map2 = new HashMap<>();
                        Map<String, String> map3 = new HashMap<>();
                        Map<String, String> map4 = new HashMap<>();
                        map1.put(String.valueOf(i), ineffectiveBlocksValues.get(cnt2));
                        map2.put(String.valueOf(i), ineffectiveBlocksValues.get(cnt2));
                        map3.put(String.valueOf(i), ineffectiveBlocksValues.get(cnt2));
                        ineffectiveLeftList.add(map1);
                        ineffectiveRightList.add(map2);
                        ineffectiveElevationList.add(map3);
                        ineffectivePermanentList.add(map4);*/
                        cnt2++;
                    }
                    sbGeom.append(")");
                    if(!sbGeom.toString().equals("MULTIPOINT ZM()")) {
                        ineffectiveBlocksMap.put(i, sbGeom.toString());
//                        ineffectiveBlocksList.add(map);
                    }
                }
            }


            // Lengths
            Variable lengths = dataFile.findVariable(group, lengthsName);
            Array lengthsArr = lengths.read();
            String[] lengthsValue = StringUtils.split(String.valueOf(lengthsArr)," ");
            List<String> lengthLobList = new ArrayList<>();
            List<String> lengthChanList = new ArrayList<>();
            List<String> lengthRobList = new ArrayList<>();
            int lengthChanIdx = 1;
            for(int i=0; i < lengthsValue.length; i++) {
                if(i%3 == 0) {
                    lengthLobList.add(lengthsValue[i]);
                } else if(lengthChanIdx == i){
                    lengthChanList.add(lengthsValue[i]);
                    lengthChanIdx+=3;
                } else {
                    lengthRobList.add(lengthsValue[i]);
                }
            }

            // Levees
            Variable levees = dataFile.findVariable(group, leveesName);
            Array leveesArr = levees.read();
            String[] leveesValue = StringUtils.split(String.valueOf(leveesArr)," ");
            List<String> leftLeveeStaList = new ArrayList<>();
            List<String> leftLeveeElevList = new ArrayList<>();
            List<String> rightLeveeStaList = new ArrayList<>();
            List<String> rightLeveeElevList = new ArrayList<>();
            int leftLeveeElevIdx = 1;
            int rightLeveeIdx = 2;
            for(int i=0; i < htvhsValue.length; i++) {
                if(i%4 == 0) {
                    leftLeveeStaList.add(leveesValue[i]);
                } else if(leftLeveeElevIdx == i) {
                    leftLeveeElevList.add(leveesValue[i]);
                    leftLeveeElevIdx += 4;
                } else if(rightLeveeIdx == i) {
                    rightLeveeStaList.add(leveesValue[i]);
                    rightLeveeIdx += 4;
                } else {
                    rightLeveeElevList.add(leveesValue[i]);
                }
            }

            // Manning's
            Variable ManningInfo = dataFile.findVariable(group, miName);
            Array ManningInfoArr = ManningInfo.read();

            Variable ManningValues = dataFile.findVariable(group, mvName);
            Array ManningArr = ManningValues.read();
            Index ManningArrIdx = ManningArr.getIndex();

            int[] ManningInfoShape = ManningInfoArr.getShape();
            Index ManningInfoIdx =  ManningInfoArr.getIndex();

            List<String> manningsList = new ArrayList<>();
            for(int i = 0, iCount = ManningInfoShape[0]; i < iCount; i++) {
                for(int j = 0, jCount = ManningInfoShape[1]; j < jCount; j=j+2) {

                    int ManningIdx = ManningInfoArr.getInt(ManningInfoIdx.set(i, j));
                    int ManningCount = ManningInfoArr.getInt(ManningInfoIdx.set(i, j + 1));

                    StringBuffer sbGeom = new StringBuffer("LINESTRING(");

                    for(int idx = 0; idx < ManningCount; idx++) {

                        sbGeom.append(ManningArr.getDouble(ManningArrIdx.set(ManningIdx+idx, 0)));
                        sbGeom.append(" ");
                        sbGeom.append(ManningArr.getDouble(ManningArrIdx.set(ManningIdx+idx, 1)));

                        if(idx < (ManningCount-1)) {
                            sbGeom.append(", ");
                        }
                    }
                    sbGeom.append(")");
                    manningsList.add(sbGeom.toString());
                }
            }

            // Polyline
            Variable polylineInfo = dataFile.findVariable(group, piName);
            Array polylineInfoArr = polylineInfo.read();

            Variable polylineValues = dataFile.findVariable(group, ppName);
            Array polylineArr = polylineValues.read();
            Index polylineArrIdx = polylineArr.getIndex();

            int[] polylineInfoShape = polylineInfoArr.getShape();
            Index polylineInfoIdx =  polylineInfoArr.getIndex();

            List<String> polylineList = new ArrayList<>();
            for(int i = 0, iCount = polylineInfoShape[0]; i < iCount; i++) {
                for(int j = 0, jCount = polylineInfoShape[1]; j < jCount; j=j+4) {

                    int polyStartIdx = polylineInfoArr.getInt(polylineInfoIdx.set(i, j));
                    int polyCount = polylineInfoArr.getInt(polylineInfoIdx.set(i, j+1));

                    StringBuilder sbGeom = new StringBuilder("LINESTRING(");

                    for(int idx = 0; idx < polyCount; idx++) {

                        sbGeom.append(polylineArr.getDouble(polylineArrIdx.set(polyStartIdx+idx, 0)));
                        sbGeom.append(" ");
                        sbGeom.append(polylineArr.getDouble(polylineArrIdx.set(polyStartIdx+idx, 1)));

                        if(idx < (polyCount-1)) {
                            sbGeom.append(", ");
                        }
                    }
                    sbGeom.append(")");
                    polylineList.add(sbGeom.toString());

                }
            }

            // Reach Names, River Names, River Stations
            Variable reach = dataFile.findVariable(group, reachName);
            Array reachArr = reach.read();
            String[] reachValue = StringUtils.split(String.valueOf(reachArr),",");

            Variable riverNm = dataFile.findVariable(group, riverName);
            Array riverNmArr = riverNm.read();
            String[] riverNmValue = StringUtils.split(String.valueOf(riverNmArr),",");

            Variable riverSt = dataFile.findVariable(group, riverSName);
            Array riverStArr = riverSt.read();
            String[] riverStValue = StringUtils.split(String.valueOf(riverStArr),",");

            List<String> reachNamesList = new ArrayList<>();
            List<String> riverNamesList = new ArrayList<>();
            List<String> riverStationsList = new ArrayList<>();
            for (String s : reachValue) {
                reachNamesList.add(s.trim());
            }
            for (String s : riverNmValue) {
                riverNamesList.add(s.trim());
            }
            for (String s : riverStValue) {
                riverStationsList.add(s.trim());
            }

            // Station Elevation
            Variable seInfo = dataFile.findVariable(group, seiName);
            Array seInfoArr = seInfo.read();

            Variable sePoints = dataFile.findVariable(group, sevName);
            Array sePointsArr = sePoints.read();
            Index sePointsIdx = sePointsArr.getIndex();

            int[] seInfoShape = seInfoArr.getShape();
            Index seInfoIdx = seInfoArr.getIndex();

            List<String> stationElevationList = new ArrayList<>();
            for(int i = 0, iCount = seInfoShape[0]; i < iCount; i++) {
                for(int j = 0, jCount = seInfoShape[1]; j < jCount; j=j+2) {

                    int pointStartIdx = seInfoArr.getInt(seInfoIdx.set(i, j));
                    int pointCount = seInfoArr.getInt(seInfoIdx.set(i, j+1));

                    StringBuilder sbGeom = new StringBuilder("LINESTRING(");

                    for(int idx = 0; idx < pointCount; idx++) {

                        sbGeom.append(sePointsArr.getDouble(sePointsIdx.set(pointStartIdx+idx, 0)));
                        sbGeom.append(" ");
                        sbGeom.append(sePointsArr.getDouble(sePointsIdx.set(pointStartIdx+idx, 1)));

                        if(idx < (pointCount-1)) {
                            sbGeom.append(", ");
                        }
                    }
                    sbGeom.append(")");
                    stationElevationList.add(sbGeom.toString());
                }
            }


            // Station Manning's
            /*Variable smInfo = dataFile.findVariable(group, smiName);
            Array smInfoArr = smInfo.read();

            Variable smPoints = dataFile.findVariable(group, smvName);
            Array smPointsArr = smPoints.read();
            Index smPointsIdx = smPointsArr.getIndex();

            int[] smInfoShape = smInfoArr.getShape();
            Index smInfoIdx = smInfoArr.getIndex();

            List<String> stationManningList = new ArrayList<>();
            for(int i = 0, iCount = smInfoShape[0]; i < iCount; i++) {
                for(int j = 0, jCount = smInfoShape[1]; j < jCount; j=j+2) {

                    int pointStartIdx = smInfoArr.getInt(smInfoIdx.set(i, j));
                    int pointCount = smInfoArr.getInt(smInfoIdx.set(i, j+1));

                    StringBuilder sbGeom = new StringBuilder("LINESTRING(");

                    for(int idx = 0; idx < pointCount; idx++) {

                        sbGeom.append(smPointsArr.getDouble(smPointsIdx.set(pointStartIdx+idx, 0)));
                        sbGeom.append(" ");
                        sbGeom.append(smPointsArr.getDouble(smPointsIdx.set(pointStartIdx+idx, 1)));

                        if(idx < (pointCount-1)) {
                            sbGeom.append(", ");
                        }
                    }
                    sbGeom.append(")");
                    stationManningList.add(sbGeom.toString());
                }
            }*/

            // Attributes
            Variable attributes = dataFile.findVariable(group, "Attributes");
            Array attributesArr = attributes.read();
            int attributesSize = (int) attributesArr.getSize();

            result.put("leftBank", leftBankList);
            result.put("rightBank", rightBankList);
//            result.put("ineffectiveLeft", ineffectiveLeftList);
//            result.put("ineffectiveRight", ineffectiveRightList);
//            result.put("ineffectiveElevation", ineffectiveElevationList);
            result.put("contractionCoef", contractionCoefList);
            result.put("expansionCoef", expansionCoefList);
            result.put("startingElevation", startingElevationList);
            result.put("verticalIncrement", verticalIncrementList);
            result.put("verticalSlices", verticalSlicesList);
            result.put("lobSlices", lobSlicesList);
            result.put("channelSlices", channelSlicesList);
            result.put("robSlices", robSlicesList);
            result.put("ineffectiveGeom", ineffectiveBlocksMap);
//            result.put("ineffectiveLeft", ineffectiveLeftList);
//            result.put("ineffectiveRight", ineffectiveRightList);
//            result.put("ineffectiveElevation", ineffectiveElevationList);
//            result.put("ineffectivePermanent", ineffectivePermanentList);
            result.put("lengthLob", lengthLobList);
            result.put("lengthRob", lengthRobList);
            result.put("lengthChan", lengthChanList);
            result.put("leftLeveeSta", leftLeveeStaList);
            result.put("leftLeveeElev", leftLeveeElevList);
            result.put("rightLeveeSta", rightLeveeStaList);
            result.put("rightLeveeElev", rightLeveeElevList);
            result.put("mannings", manningsList);
            result.put("polyLine", polylineList);
            result.put("reachNames", reachNamesList);
            result.put("riverNames", riverNamesList);
            result.put("riverStations", riverStationsList);
            result.put("stationElevation", stationElevationList);
            result.put("size", attributesSize);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (dataFile != null) {
                try {
                    dataFile.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
        
        return result;

    }

    public Map<String, String> junctionsReadHdf(String fileName, String groupName, String names, String pointsName) {
        NetcdfFile dataFile = null;

        Map<String, String> result = new HashMap<>();

        try {
            dataFile = NetcdfFile.open(fileName);

            Group group = dataFile.findGroup(groupName);

            Variable junctionsNames = dataFile.findVariable(group, names);
            Array junctionsNamesArr = junctionsNames.read();

            Variable junctionsPoints = dataFile.findVariable(group, pointsName);
            Array junctionsPointsArr = junctionsPoints.read();

            StringBuilder geom = new StringBuilder("POINT(");
            for(int i=0; i < junctionsPointsArr.getSize(); i++) {
                geom.append(junctionsPointsArr.getDouble(i));
                if(i + 1 < junctionsPointsArr.getSize()) {
                    geom.append(" ");
                }
            }
            geom.append(")");

            result.put("name", junctionsNamesArr.toString());
            result.put("geom", geom.toString());

        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (dataFile != null) {
                try {
                    dataFile.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }

        return result;
    }

    public List<String> riverBankLinesReadHdf(String fileName, String groupName, String infoName, String pointsName) {
        NetcdfFile dataFile = null;

        List<String> result = new ArrayList<>();

        try {
            dataFile = NetcdfFile.open(fileName);

            Group group = dataFile.findGroup(groupName);

            Variable riverBankLineInfo = dataFile.findVariable(group, infoName);
            Array riverBankLineInfoArr = riverBankLineInfo.read();

            Variable riverBankLinePoints = dataFile.findVariable(group, pointsName);
            Array riverBankLinePointsArr = riverBankLinePoints.read();
            Index riverBankLinePointsIdx = riverBankLinePointsArr.getIndex();


            int[] riverBankLineInfoShape = riverBankLineInfoArr.getShape();
            Index riverBankLineInfoIdx = riverBankLineInfoArr.getIndex();

            for(int i = 0, iCount = riverBankLineInfoShape[0]; i < iCount; i++) {
                for(int j = 0, jCount = riverBankLineInfoShape[1]; j < jCount; j=j+4) {

                    int pointStartIdx = riverBankLineInfoArr.getInt(riverBankLineInfoIdx.set(i, j));
                    int pointCount = riverBankLineInfoArr.getInt(riverBankLineInfoIdx.set(i, j+1));

                    StringBuilder geom = new StringBuilder("LINESTRING(");
                    Map<String, String> map = new HashMap<>();

                    for(int idx = 0; idx < pointCount; idx++) {

                        geom.append(riverBankLinePointsArr.getDouble(riverBankLinePointsIdx.set(pointStartIdx+idx, 0)));
                        geom.append(" ");
                        geom.append(riverBankLinePointsArr.getDouble(riverBankLinePointsIdx.set(pointStartIdx+idx, 1)));

                        if(idx < (pointCount-1)) {
                            geom.append(", ");
                        }
                    }
                    geom.append(")");

                    result.add(geom.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (dataFile != null) {
                try {
                    dataFile.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }

        return result;
    }

    public List<Map<String, String>> riverCenterlinesReadHdf(String fileName, String groupName, String dsJunctionName, String dsSa_2DName, String polyLineInfoName,
                                                String polyLinePointsName, String reachName, String riverName, String usJunctionName, String usSa_2DName) {
        NetcdfFile dataFile = null;

        List<Map<String, String>> result = new ArrayList<>();

        try {
            dataFile = NetcdfFile.open(fileName);

            Group group = dataFile.findGroup(groupName);

            Variable dsJunction = dataFile.findVariable(group, dsJunctionName);
            Array dsJunctionArr = dsJunction.read();
            String[] dsJunctionValues = StringUtils.split(dsJunctionArr.toString(), ",");
            List<String> dsJuctionList = new ArrayList<>();
            for (String dsJunctionValue : dsJunctionValues) {
                if (dsJunctionValue.trim().equals("")) {
                    dsJuctionList.add("null");
                } else {
                    dsJuctionList.add(dsJunctionValue.trim());
                }
            }


            Variable dsSa_2D = dataFile.findVariable(group, dsSa_2DName);
            Array dsSa_2DArr = dsSa_2D.read();
            String[] dsSa_2DValues = StringUtils.split(dsSa_2DArr.toString(), ",");
            List<String> dsSa_2DList = new ArrayList<>();
            for (String dsSa_2DValue : dsSa_2DValues) {
                if (dsSa_2DValue.trim().equals("")) {
                    dsSa_2DList.add("null");
                } else {
                    dsSa_2DList.add(dsSa_2DValue.trim());
                }
            }


            Variable polyLineInfo = dataFile.findVariable(group, polyLineInfoName);
            Array polyLineInfoArr = polyLineInfo.read();

            Variable polyLinePoints = dataFile.findVariable(group, polyLinePointsName);
            Array polyLinePointsArr = polyLinePoints.read();
            Index polyLinePointsIdx = polyLinePointsArr.getIndex();

            int[] polyLineShape = polyLineInfoArr.getShape();
            Index polyLineIdx = polyLineInfoArr.getIndex();

            List<String> polyLineList = new ArrayList<>();
            for(int i = 0, iCount = polyLineShape[0]; i < iCount; i++) {
                for(int j = 0, jCount = polyLineShape[1]; j < jCount; j=j+4) {

                    int pointStartIdx = polyLineInfoArr.getInt(polyLineIdx.set(i, j));
                    int pointCount = polyLineInfoArr.getInt(polyLineIdx.set(i, j+1));

                    StringBuilder geom = new StringBuilder("LINESTRING(");

                    for(int idx = 0; idx < pointCount; idx++) {

                        geom.append(polyLinePointsArr.getDouble(polyLinePointsIdx.set(pointStartIdx+idx, 0)));
                        geom.append(" ");
                        geom.append(polyLinePointsArr.getDouble(polyLinePointsIdx.set(pointStartIdx+idx, 1)));

                        if(idx < (pointCount-1)) {
                            geom.append(", ");
                        }
                    }
                    geom.append(")");

                    polyLineList.add(geom.toString());
                }
            }


            Variable reachNames = dataFile.findVariable(group, reachName);
            Array reachNamesArr = reachNames.read();
            String[] reachNamesValues = StringUtils.split(reachNamesArr.toString(), ",");
            List<String> reachNamesList = new ArrayList<>();
            for (String reachNamesValue : reachNamesValues) {
                if (reachNamesValue.trim().equals("")) {
                    reachNamesList.add("null");
                } else {
                    reachNamesList.add(reachNamesValue.trim());
                }
            }


            Variable riverNames = dataFile.findVariable(group, riverName);
            Array riverNamesArr = riverNames.read();
            String[] riverNamesValues = StringUtils.split(riverNamesArr.toString(), ",");
            List<String> riverNamesList = new ArrayList<>();
            for (String riverNamesValue : riverNamesValues) {
                if (riverNamesValue.trim().equals("")) {
                    riverNamesList.add("null");
                } else {
                    riverNamesList.add(riverNamesValue.trim());
                }
            }


            Variable usJunction = dataFile.findVariable(group, usJunctionName);
            Array usJunctionArr = usJunction.read();
            String[] usJunctionValues = StringUtils.split(usJunctionArr.toString(), ",");
            List<String> usJunctionList = new ArrayList<>();
            for (String usJunctionValue : usJunctionValues) {
                if (usJunctionValue.trim().equals("")) {
                    usJunctionList.add("null");
                } else {
                    usJunctionList.add(usJunctionValue.trim());
                }
            }


            Variable usSa_2D = dataFile.findVariable(group, usSa_2DName);
            Array usSa_2DArr = usSa_2D.read();
            String[] usSa_2DValues = StringUtils.split(usSa_2DArr.toString(), ",");
            List<String> usSa_2DList = new ArrayList<>();
            for (String usSa_2DValue : usSa_2DValues) {
                if (usSa_2DValue.trim().equals("")) {
                    usSa_2DList.add("null");
                } else {
                    usSa_2DList.add(usSa_2DValue.trim());
                }
            }

            // Attributes
            Variable attributes = dataFile.findVariable(group, "Attributes");
            Array attributesArr = attributes.read();
            int attributesSize = (int) attributesArr.getSize();

            for(int i=0; i < attributesSize; i++) {
                Map<String, String> resultMap = new HashMap<>();

                resultMap.put("dsJunction", dsJuctionList.get(i));
                resultMap.put("geom", String.valueOf(polyLineList.get(i)));
                resultMap.put("reachName", reachNamesList.get(i));
                resultMap.put("riverName", riverNamesList.get(i));
                resultMap.put("usJunction", usJunctionList.get(i));
                resultMap.put("dsSa2d", dsSa_2DList.get(i));
                resultMap.put("usSa2d", usSa_2DList.get(i));

                result.add(resultMap);
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (dataFile != null) {
                try {
                    dataFile.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }

        return result;
    }

    public List<String> riverEdgeLinesReadHdf(String fileName, String groupName, String infoName, String pointName) {

        NetcdfFile dataFile = null;

        List<String> result = new ArrayList<>();

        try {
            dataFile = NetcdfFile.open(fileName);

            Group group = dataFile.findGroup(groupName);

            Variable polylineInfo = dataFile.findVariable(group, infoName);
            Array polylineInfoArr = polylineInfo.read();

            Variable polylinePoints = dataFile.findVariable(group, pointName);
            Array polylinePointsArr = polylinePoints.read();
            Index polylinePointsIdx = polylinePointsArr.getIndex();


            int[] polylineInfoShape = polylineInfoArr.getShape();
            Index polylineInfoIdx = polylineInfoArr.getIndex();


            for(int i = 0, iCount = polylineInfoShape[0]; i < iCount; i++) {
                for(int j = 0, jCount = polylineInfoShape[1]; j < jCount; j=j+4) {

                    int pointStartIdx = polylineInfoArr.getInt(polylineInfoIdx.set(i, j));
                    int pointCount = polylineInfoArr.getInt(polylineInfoIdx.set(i, j+1));

//                    System.out.println(i + " => " + pointStartIdx + ", " + pointCount + ", " + partStartIdx + ", " + partCount);

                    StringBuffer sbGeom = new StringBuffer("LINESTRING(");

                    for(int idx = 0; idx < pointCount; idx++) {

                        sbGeom.append(polylinePointsArr.getDouble(polylinePointsIdx.set(pointStartIdx+idx, 0)));
                        sbGeom.append(" ");
                        sbGeom.append(polylinePointsArr.getDouble(polylinePointsIdx.set(pointStartIdx+idx, 1)));

                        if(idx < (pointCount-1)) {
                            sbGeom.append(", ");
                        }
                    }
                    sbGeom.append(")");

                    result.add(sbGeom.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (dataFile != null) {
                try {
                    dataFile.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }

        return result;
    }

    public List<Map<String, Object>> storageAreasReadHdf(String fileName, String groupName, String names, String polygonAreaName, String polygonInfoName,
                                                         String polygonPointsName, String volumeElevationInfoName, String volumeElevationValuesName) {

        NetcdfFile dataFile = null;

        List<Map<String, Object>> result = new ArrayList<>();

        try {
            dataFile = NetcdfFile.open(fileName);

            Group group = dataFile.findGroup(groupName);

            // Names
            Variable namesVariable = dataFile.findVariable(group, names);
            Array namesVariableArr = namesVariable.read();
            String[] namesVariableValue = StringUtils.split(String.valueOf(namesVariableArr), ",");

            // Polygon Area
            Variable polygonArea = dataFile.findVariable(group, polygonAreaName);
            Array polygonAreaArr = polygonArea.read();
            String[] polygonAreaValue = StringUtils.split(String.valueOf(polygonAreaArr));

            // Polygon
            Variable polygonInfo = dataFile.findVariable(group, polygonInfoName);
            Array polygonInfoArr = polygonInfo.read();

            Variable polygonPoints = dataFile.findVariable(group, polygonPointsName);
            Array polygonPointsArr = polygonPoints.read();
            Index polygonPointsIdx = polygonPointsArr.getIndex();


            int[] polygonInfoShape = polygonInfoArr.getShape();
            Index polygonInfoIdx = polygonInfoArr.getIndex();

            List<String> polygonGeomList = new ArrayList<>();
            for(int i = 0, iCount = polygonInfoShape[0]; i < iCount; i++) {
                for(int j = 0, jCount = polygonInfoShape[1]; j < jCount; j=j+4) {

                    int pointStartIdx = polygonInfoArr.getInt(polygonInfoIdx.set(i, j));
                    int pointCount = polygonInfoArr.getInt(polygonInfoIdx.set(i, j+1));

                    StringBuffer sbGeom = new StringBuffer("POLYGON((");

                    for(int idx = 0; idx < pointCount; idx++) {

                        sbGeom.append(polygonPointsArr.getDouble(polygonPointsIdx.set(pointStartIdx+idx, 0)));
                        sbGeom.append(" ");
                        sbGeom.append(polygonPointsArr.getDouble(polygonPointsIdx.set(pointStartIdx+idx, 1)));

                        if(idx < (pointCount-1)) {
                            sbGeom.append(", ");
                        }
                    }
                    sbGeom.append("))");
                    polygonGeomList.add(sbGeom.toString());
                }
            }

            // Volume Elevation
            Variable volumeElevationInfo = dataFile.findVariable(group, volumeElevationInfoName);
            Array volumeElevationInfoArr = volumeElevationInfo.read();

            Variable volumeElevationPoints = dataFile.findVariable(group, volumeElevationValuesName);
            Array volumeElevationPointsArr = volumeElevationPoints.read();
            Index volumeElevationPointsIdx = volumeElevationPointsArr.getIndex();


            int[] volumeElevationInfoShape = volumeElevationInfoArr.getShape();
            Index volumeElevationInfoIdx = volumeElevationInfoArr.getIndex();

            List<String> volumeElevationGeomList = new ArrayList<>();
            for(int i = 0, iCount = volumeElevationInfoShape[0]; i < iCount; i++) {
                for(int j = 0, jCount = volumeElevationInfoShape[1]; j < jCount; j=j+4) {

                    int pointStartIdx = volumeElevationInfoArr.getInt(volumeElevationInfoIdx.set(i, j));
                    int pointCount = volumeElevationInfoArr.getInt(volumeElevationInfoIdx.set(i, j+1));

                    StringBuffer sbGeom = new StringBuffer("LINESTRING(");

                    for(int idx = 0; idx < pointCount; idx++) {

                        sbGeom.append(volumeElevationPointsArr.getDouble(volumeElevationPointsIdx.set(pointStartIdx+idx, 0)));
                        sbGeom.append(" ");
                        sbGeom.append(volumeElevationPointsArr.getDouble(volumeElevationPointsIdx.set(pointStartIdx+idx, 1)));

                        if(idx < (pointCount-1)) {
                            sbGeom.append(", ");
                        }
                    }
                    sbGeom.append(")");
                    volumeElevationGeomList.add(sbGeom.toString());
                }
            }

            // Attributes
            Variable attributes = dataFile.findVariable(group, "Attributes");
            Array attributesArr = attributes.read();
            int attributesSize = (int) attributesArr.getSize();

            for(int i=0; i < attributesSize; i++) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("id", namesVariableValue[i]);
                resultMap.put("polygonGeom", polygonGeomList.get(i));
                resultMap.put("polygonArea", BigDecimal.valueOf(Double.parseDouble(polygonAreaValue[i])));
                resultMap.put("volumeElevationGeom", volumeElevationGeomList.get(i));
                result.add(resultMap);
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (dataFile != null) {
                try {
                    dataFile.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }

        return result;

    }

    public Map<String, Object> structuresReadHdf(String fileName, String groupName) {

        NetcdfFile dataFile = null;

        Map<String, Object> resultMap = new HashMap<>();

        try {
            dataFile = NetcdfFile.open(fileName);

            Group group = dataFile.findGroup(groupName);

            // Centerline
            Variable centerLineInfo = dataFile.findVariable(group, "Centerline_Info");
            Array centerLineInfoArr = centerLineInfo.read();

            Variable centerLinePoints = dataFile.findVariable(group, "Centerline_Points");
            Array centerLinePointsArr = centerLinePoints.read();
            Index centerLinePointsIdx = centerLinePointsArr.getIndex();

            int[] centerLineInfoShape = centerLineInfoArr.getShape();
            Index centerLineInfoIdx = centerLineInfoArr.getIndex();

            Map<String, String> centerLineGeomList = new HashMap<>();
            for(int i = 0, iCount = centerLineInfoShape[0]; i < iCount; i++) {
                for(int j = 0, jCount = centerLineInfoShape[1]; j < jCount; j=j+4) {

                    int pointStartIdx = centerLineInfoArr.getInt(centerLineInfoIdx.set(i, j));
                    int pointCount = centerLineInfoArr.getInt(centerLineInfoIdx.set(i, j+1));


                    StringBuffer sbGeom = new StringBuffer("LINESTRING(");
                    Map<String, String> map = new HashMap<>();

                    for(int idx = 0; idx < pointCount; idx++) {

                        sbGeom.append(centerLinePointsArr.getDouble(centerLinePointsIdx.set(pointStartIdx+idx, 0)));
                        sbGeom.append(" ");
                        sbGeom.append(centerLinePointsArr.getDouble(centerLinePointsIdx.set(pointStartIdx+idx, 1)));

                        if(idx < (pointCount-1)) {
                            sbGeom.append(", ");
                        }
                    }
                    sbGeom.append(")");
//                    System.out.println(i + " / " + sbGeom);

                    if(!sbGeom.toString().equals("LINESTRING()")) {
//                        System.out.println(i + " " + sbGeom);
                        centerLineGeomList.put(String.valueOf(i), sbGeom.toString());
//                        centerLineGeomList.add(map);
                    } else {
                        centerLineGeomList.put(String.valueOf(i), null);
                    }

                }
            }

            // Pier Data
            Variable pierAttribute = dataFile.findVariable(group, "Pier_Attributes");
            Array pierAttributeArr = pierAttribute.read();
            List<Map<String, String>> pierAttrData = new ArrayList<>();
            int cnt7 = 0;
            for(int i=0; i < pierAttributeArr.getSize(); i++) {
                ucar.ma2.StructureData sd = (ucar.ma2.StructureData) pierAttributeArr.getObject(i);
                Map<String, String> map = new HashMap<>();

                for (ucar.ma2.StructureMembers.Member m : sd.getStructureMembers().getMembers()) {
                    if(cnt7 < 11) {
                        switch(cnt7) {
                            case 0:
                                map.put("id", String.valueOf(sd.getScalarObject(m)));
                                break;
                            case 1:
                                map.put("usStation", String.valueOf(sd.getScalarObject(m)));
                                break;
                            case 2:
                                map.put("dsStation", String.valueOf(sd.getScalarObject(m)));
                                break;
                            case 6:
                                map.put("usIdx", String.valueOf(sd.getScalarObject(m)));
                                break;
                            case 7:
                                map.put("usCnt", String.valueOf(sd.getScalarObject(m)));
                                break;
                            case 8:
                                map.put("dsIdx", String.valueOf(sd.getScalarObject(m)));
                                break;
                            case 9:
                                map.put("dsCnt", String.valueOf(sd.getScalarObject(m)));
                                break;
                        }
//                        System.out.print(sd.getScalarObject(m) + " ");
                    }
                    cnt7++;
                }
                pierAttrData.add(map);
                cnt7 = 0;

//                System.out.println();
            }

            Variable pierData = dataFile.findVariable(group, "Pier_Data");
            Array pierDataArr = pierData.read();
            String[] pierValues = StringUtils.split(String.valueOf(pierDataArr), " ");

            List<Map<String, String>> pierDataList = new ArrayList<>();

//            System.out.println(pierAttrData);
            for(int i=0; i < pierAttrData.size(); i++) {
                Map<String, String> map = new HashMap<>();
                int usIdx = Integer.parseInt(pierAttrData.get(i).get("usIdx")) * 2;
                int usCnt = Integer.parseInt(pierAttrData.get(i).get("usCnt")) * 2;
                int dsIdx = Integer.parseInt(pierAttrData.get(i).get("dsIdx")) * 2;
                int dsCnt = Integer.parseInt(pierAttrData.get(i).get("dsCnt")) * 2;
                String id = pierAttrData.get(i).get("id");
                String usStation = pierAttrData.get(i).get("usStation");
                String dsStation = pierAttrData.get(i).get("dsStation");
                String usPierWidth = "";
                String usElevation = "";
                String dsPierWidth = "";
                String dsElevation = "";
                String usPierData = "";
                String dsPierData = "";

//                System.out.println("----" + id + " " + usStation + " " + dsStation);
                // Upstream Pier Width/ Elevation 값
                for (int k = 0; k < usCnt; k += 4) {
//                    System.out.print(pierValues[usIdx] + " " + pierValues[usIdx + 1] + " " + pierValues[usIdx + 2] + " " + pierValues[usIdx + 3] + " ");
                    usPierData = pierValues[usIdx] + " " + pierValues[usIdx + 1] + " " + pierValues[usIdx + 2] + " " + pierValues[usIdx + 3];
                }

                // Downstream Pier Width/ Elevation 값
                for (int j = 0; j < dsCnt; j += 4) {
//                    System.out.print(pierValues[dsIdx] + " " + pierValues[dsIdx + 1] + " " + pierValues[dsIdx + 2] + " " + pierValues[dsIdx + 3]);
                    dsPierData = pierValues[dsIdx] + " " + pierValues[dsIdx + 1] + " " + pierValues[dsIdx + 2] + " " + pierValues[dsIdx + 3];
                }

                map.put("id", id);
                map.put("usStation", usStation);
                map.put("dsStation", dsStation);
                map.put("usPierData", usPierData);
                map.put("dsPierData", dsPierData);
                pierDataList.add(map);
            }

            // Profile Data
            Variable tableInfo = dataFile.findVariable(group, "Table_Info");
            Array tableInfoArr = tableInfo.read();
            List<String> tableInfoList = new ArrayList<>();

            Variable profileData = dataFile.findVariable(group, "Profile_Data");
            Array profileDataArr = profileData.read();
            String[] profileValues = StringUtils.split(String.valueOf(profileDataArr), " ");

            int cnt = 0;
            for (int k = 0; k < tableInfoArr.getSize(); k++) {
                ucar.ma2.StructureData sd = (ucar.ma2.StructureData) tableInfoArr.getObject(k);

                for (ucar.ma2.StructureMembers.Member m : sd.getStructureMembers().getMembers()) {
                    if(cnt < 2) {
                        cnt++;
//                        System.out.println(k + " " + sd.getScalarObject(m));
                        tableInfoList.add(String.valueOf(sd.getScalarObject(m)));
                    }
                }
                cnt = 0;
            }

            Map<String, String> profileDataList = new HashMap<>();
            for(int i=0; i < tableInfoList.size(); i+=2) {
                int tableStart = Integer.parseInt(tableInfoList.get(i)) * 2;
                int tableCount = Integer.parseInt(tableInfoList.get(i + 1)) * 2;
                StringBuilder geom = new StringBuilder("LINESTRING(");
//                System.out.println(i/2 + "------------------");
                for (int j = 0; j < tableCount; j++) {
                    if(profileValues.length > tableStart + 1) {
                        geom.append(profileValues[tableStart]);
                        geom.append(" ");
                        geom.append(profileValues[tableStart + 1]);

//                        System.out.println((j + 1) + "  /  " +  tableCount);
//                        if((j + 1) < tableCount) {
                            geom.append(", ");
//                        }

                        tableStart += 2;
                    }
                }

                geom.deleteCharAt(geom.length() - 2);

                geom.append(")");
//                System.out.println(geom);
                if(!geom.toString().equals("LINESTRIN()")) {
                    profileDataList.put(String.valueOf(i / 2), geom.toString());
                } else {
                    profileDataList.put(String.valueOf(i / 2), null);
                }
            }

            // User Defined Weir Connectivity
            Variable udwc = dataFile.findVariable(group, "User_Defined_Weir_Connectivity");
            Array udwcArr = udwc.read();

            int cnt3 = 0;
            List<Map<String, String>> udwcList = new ArrayList<>();
            for (int k = 0; k < udwcArr.getSize(); k++) {
                ucar.ma2.StructureData sd = (ucar.ma2.StructureData) udwcArr.getObject(k);
                Map<String, String> udwcMap = new HashMap<>();
                for (ucar.ma2.StructureMembers.Member m : sd.getStructureMembers().getMembers()) {
//                    System.out.println(sd.getScalarObject(m));
                    switch (cnt3) {
                        case 0:
                            udwcMap.put("id", String.valueOf(sd.getScalarObject(m)));
                            break;
                        case 1:
                            udwcMap.put("hw_tw", String.valueOf(sd.getScalarObject(m)));
                            break;
                        case 2:
                            udwcMap.put("rs_fp", String.valueOf(sd.getScalarObject(m)));
                            break;
                        case 3:
                            udwcMap.put("station", String.valueOf(sd.getScalarObject(m)));
                            break;
                    }
                    cnt3++;
                }
                udwcList.add(udwcMap);
                cnt3 = 0;
            }

            Variable attr = dataFile.findVariable(group, "Attributes");
            Array attrArr = attr.read();
            int size = Integer.parseInt(String.valueOf(attrArr.getSize()));

            resultMap.put("centerLine", centerLineGeomList);
            resultMap.put("peirData", pierDataList);
            resultMap.put("profileData", profileDataList);
            resultMap.put("udwcData", udwcList);
            resultMap.put("size", size);

        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (dataFile != null) {
                try {
                    dataFile.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }

        return resultMap;
    }
}
