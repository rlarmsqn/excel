package com.jbt.water.util;

import jdk.swing.interop.SwingInterOpUtils;
import org.apache.commons.lang3.StringUtils;
import ucar.ma2.Array;
import ucar.ma2.Index;
import ucar.nc2.Group;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.io.IOException;
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

    public void crossSectionsHdf(String fileName, String groupName, String bankName, String biiName, String bivName, String cecName,
                                                      String htseisName, String htvhsName, String ineffectiveBlocksName, String ineffectiveInfoName, String lengthsName, String leveesName, String miName, String mvName,
                                                      String piName, String ppName, String reachName, String riverName, String riverSName, String seiName, String sevName,
                                                      String smiName, String smvName) {

        NetcdfFile dataFile = null;

        List<Map<String, Object>> result = new ArrayList<>();

        try {
            dataFile = NetcdfFile.open(fileName);

            Group group = dataFile.findGroup(groupName);

            // Bank Stations
            Variable bank = dataFile.findVariable(group, bankName);
            Array bankArr = bank.read();
            String[] bankValue = StringUtils.split(String.valueOf(bankArr)," ");
            List<String> leftBank = new ArrayList<>();
            List<String> rightBank = new ArrayList<>();
            for(int i=0; i < bankValue.length; i++) {
                if(i%2 == 0) {
                    leftBank.add(bankValue[i]);
                } else {
                    rightBank.add(bankValue[i]);
                }
            }

            // Blocked Ineffective
            Variable blInfo = dataFile.findVariable(group, biiName);
            Array blInfoArr = blInfo.read();

            Variable bivValues = dataFile.findVariable(group, bivName);
            Array bivArr = bivValues.read();
            Index bivArrIdx = bivArr.getIndex();

            int[] blInfoShape = blInfoArr.getShape();
            Index blInfoIdx =  blInfoArr.getIndex();

//            List<Map<String, String>> bivValueList = new ArrayList<>();
//            List<Map<String, String>> ineffectiveLeft = new ArrayList<>();
//            List<Map<String, String>> ineffectiveRight = new ArrayList<>();
//            List<Map<String, String>> ineffectiveElevation = new ArrayList<>();
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
//                        ineffectiveLeft.add(map1);
//                        ineffectiveRight.add(map2);
//                        ineffectiveElevation.add(map3);
//                        bivValueList.add(map);
                    }
                }
            }

            // Contr Expan Coef
            Variable cec = dataFile.findVariable(group, cecName);
            Array cecArr = cec.read();
            String[] cecValue = StringUtils.split(String.valueOf(cecArr)," ");
            List<String> contractionCoef = new ArrayList<>();
            List<String> expansionCoef = new ArrayList<>();
            for(int i=0; i < cecValue.length; i++) {
                if(i%2 == 0) {
                    contractionCoef.add(cecValue[i]);
                } else {
                    expansionCoef.add(cecValue[i]);
                }
            }

            // Hydraulic Tables Starting Elevation and Increment Size
            Variable htseis = dataFile.findVariable(group, htseisName);
            Array htseisArr = htseis.read();
            String[] htseisValue = StringUtils.split(String.valueOf(htseisArr)," ");
            List<String> startingElevation = new ArrayList<>();
            List<String> verticalIncrement = new ArrayList<>();
            for(int i=0; i < htseisValue.length; i++) {
                if(i%2 == 0) {
                    startingElevation.add(htseisValue[i]);
                } else {
                    verticalIncrement.add(htseisValue[i]);
                }
            }

            // Hydraulic Tables Vertical and Horizontal Slices
            Variable htvhs = dataFile.findVariable(group, htvhsName);
            Array htvhsArr = htvhs.read();
            String[] htvhsValue = StringUtils.split(String.valueOf(htvhsArr)," ");
            List<String> verticalSlices = new ArrayList<>();
            List<String> lobSlices = new ArrayList<>();
            List<String> channelSlices = new ArrayList<>();
            List<String> robSlices = new ArrayList<>();
            int lobSlicesIdx = 1;
            int channelSlicesIdx = 2;
            for(int i=0; i < htvhsValue.length; i++) {
                if(i%4 == 0) {
                    verticalSlices.add(htvhsValue[i]);
                } else if(lobSlicesIdx == i) {
                    lobSlices.add(htvhsValue[i]);
                    lobSlicesIdx += 4;
                } else if(channelSlicesIdx == i) {
                    channelSlices.add(htvhsValue[i]);
                    channelSlicesIdx += 4;
                } else {
                    robSlices.add(htvhsValue[i]);
                }
            }

            // Ineffective
            Variable ineffectivInfo = dataFile.findVariable(group, ineffectiveInfoName);
            Array ineffectivInfoArr = ineffectivInfo.read();

            Variable ineffectiveBlocks = dataFile.findVariable(group, ineffectiveBlocksName);
            Array ineffectiveBlocksArr = ineffectiveBlocks.read();
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
            List<Map<String, String>> ineffectiveLeft = new ArrayList<>();
            List<Map<String, String>> ineffectiveRight = new ArrayList<>();
            List<Map<String, String>> ineffectiveElevation = new ArrayList<>();
            List<Map<String, String>> ineffectivePermanent = new ArrayList<>();
            int cnt2 = 0;
            for(int i = 0, iCount = ineffectivInfoShape[0]; i < iCount; i++) {
                for (int j = 0, jCount = ineffectivInfoShape[1]; j < jCount; j = j + 2) {
                    int ineffectivCount = ineffectivInfoArr.getInt(ineffectivInfoIdx.set(i, j + 1));

                    for (int idx = 0; idx < ineffectivCount; idx++) {
//                        Map<String, String> map = new HashMap<>();

//                        map.put(String.valueOf(i), ineffectiveBlocksValues.get(cnt2));
//                        ineffectiveList.add(map);
                        Map<String, String> map1 = new HashMap<>();
                        Map<String, String> map2 = new HashMap<>();
                        Map<String, String> map3 = new HashMap<>();
                        Map<String, String> map4 = new HashMap<>();
                        map1.put(String.valueOf(i), String.valueOf(bivArr.getDouble(bivArrIdx.set(bivIdx + idx, 0))));
                        map2.put(String.valueOf(i), String.valueOf(bivArr.getDouble(bivArrIdx.set(bivIdx + idx, 1))));
                        map3.put(String.valueOf(i), String.valueOf(bivArr.getDouble(bivArrIdx.set(bivIdx + idx, 2))));
                        ineffectiveLeft.add(map1);
                        ineffectiveRight.add(map2);
                        ineffectiveElevation.add(map3);
                        ineffectivePermanent.add(map4);
                        cnt2++;
                    }
                }
            }
            System.out.println(ineffectiveList);

            // Lengths
            Variable lengths = dataFile.findVariable(group, lengthsName);
            Array lengthsArr = lengths.read();
            String[] lengthsValue = StringUtils.split(String.valueOf(lengthsArr)," ");
            List<String> lengthLob = new ArrayList<>();
            List<String> lengthChan = new ArrayList<>();
            List<String> lengthRob = new ArrayList<>();
            int lengthChanIdx = 1;
            for(int i=0; i < lengthsValue.length; i++) {
                if(i%3 == 0) {
                    lengthLob.add(lengthsValue[i]);
                } else if(lengthChanIdx == i){
                    lengthChan.add(lengthsValue[i]);
                    lengthChanIdx+=3;
                } else {
                    lengthRob.add(lengthsValue[i]);
                }
            }

            // Levees
            Variable levees = dataFile.findVariable(group, leveesName);
            Array leveesArr = levees.read();
            String[] leveesValue = StringUtils.split(String.valueOf(leveesArr)," ");
            List<String> leftLeveeSta = new ArrayList<>();
            List<String> leftLeveeElev = new ArrayList<>();
            List<String> rightLeveeSta = new ArrayList<>();
            List<String> rightLeveeElev = new ArrayList<>();
            int leftLeveeElevIdx = 1;
            int rightLeveeIdx = 2;
            for(int i=0; i < htvhsValue.length; i++) {
                if(i%4 == 0) {
                    leftLeveeSta.add(leveesValue[i]);
                } else if(leftLeveeElevIdx == i) {
                    leftLeveeElev.add(leveesValue[i]);
                    leftLeveeElevIdx += 4;
                } else if(rightLeveeIdx == i) {
                    rightLeveeSta.add(leveesValue[i]);
                    rightLeveeIdx += 4;
                } else {
                    rightLeveeElev.add(leveesValue[i]);
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

            List<String> stationList = new ArrayList<>();
            List<String> mannnList = new ArrayList<>();
            for(int i = 0, iCount = ManningInfoShape[0]; i < iCount; i++) {
                for (int j = 0, jCount = ManningInfoShape[1]; j < jCount; j = j + 2) {
                    int ManningIdx = ManningInfoArr.getInt(ManningInfoIdx.set(i, j));
                    int ManningCount = ManningInfoArr.getInt(ManningInfoIdx.set(i, j + 1));

                    for (int idx = 0; idx < ManningCount; idx++) {
                        stationList.add(String.valueOf(ManningArr.getDouble(ManningArrIdx.set(ManningIdx+idx, 0))));
                        mannnList.add(String.valueOf(ManningArr.getDouble(ManningArrIdx.set(ManningIdx+idx, 1))));
                    }
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
            Variable smInfo = dataFile.findVariable(group, smiName);
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
            }

            Map<String, Object> map = new HashMap<>(){{
                put("leftBank", leftBank);
                put("rightBank", rightBank);
                put("ineffectiveLeft", ineffectiveLeft);
                put("ineffectiveRight", ineffectiveRight);
                put("ineffectiveElevation", ineffectiveElevation);
                put("contractionCoef", contractionCoef);
                put("expansionCoef", expansionCoef);
                put("startingElevation", startingElevation);
                put("verticalSlices", verticalSlices);
                put("lobSlices", lobSlices);
                put("channelSlices", channelSlices);
                put("robSlices", robSlices);

            }};

            result.add(map);

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

    }
}
