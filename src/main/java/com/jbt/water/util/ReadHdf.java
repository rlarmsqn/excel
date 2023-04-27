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
                                                      String htseisName, String htvhsName, String lengthsName, String leveesName, String miName, String mvName,
                                                      String piName, String ppName, String rEACHName, String riverName, String riverSName, String seiName, String sevName,
                                                      String smiName, String smvName) {

        NetcdfFile dataFile = null;

        List<Map<String, String>> result = new ArrayList<>();

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

            List<String> bivValueList = new ArrayList<>();
            for(int i = 0, iCount = blInfoShape[0]; i < iCount; i++) {
                for (int j = 0, jCount = blInfoShape[1]; j < jCount; j = j + 2) {
                    int bivIdx = blInfoArr.getInt(blInfoIdx.set(i, j));
                    int bivCount = blInfoArr.getInt(blInfoIdx.set(i, j + 1));

                    System.out.println(i + " => " + bivIdx + ", " + bivCount);

                    for (int idx = 0; idx < bivCount; idx++) {
                        System.out.println(bivArr.getDouble(bivArrIdx.set(bivIdx + idx, 0)) + " " + bivArr.getDouble(bivArrIdx.set(bivIdx + idx, 1)) + " " + bivArr.getDouble(bivArrIdx.set(bivIdx + idx, 2)));
                    }
                }
            }


            /*int[] tinInfoShape = tinInfoArr.getShape();
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
                }
            }*/
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
