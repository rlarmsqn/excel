package com.jbt.water.service;

import com.jbt.water.mapper.WaterMapper;
import com.jbt.water.vo.util.ExcelFilType;
import com.jbt.water.vo.util.ExcelRead;
import com.jbt.water.vo.util.ExcelReadOption;
import com.jbt.water.vo.FacilityVO;
import com.jbt.water.vo.RainFallVO;
import com.jbt.water.vo.WaterInfoVO;
import com.jbt.water.vo.WaterVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaterService {

    private final WaterMapper waterMapper;

    public String insertData() throws IOException {
        String filePath = "C:\\Users\\srmsq\\Desktop";
        String fileName = "Pakkangoung_waterlevel.xlsx";
//        String fileName = "Phiengluang.xlsx";
//        String fileName = "Vernkham.xlsx";
//        String fileName = "H Nam Ngum Phiengluang.xls";


        ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setOutputColumns("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M");

        List<Map<String, String>> list = ExcelRead.read(excelReadOption, ExcelFilType.getWorkbook("C:\\Users\\srmsq\\Desktop\\Pakkangoung_waterlevel.xlsx"), 0);
        List<String> year = new ArrayList<>();

        WaterVO jan = new WaterVO();
        WaterVO feb = new WaterVO();
        WaterVO mar = new WaterVO();
        WaterVO apr = new WaterVO();
        WaterVO may = new WaterVO();
        WaterVO jun = new WaterVO();
        WaterVO jul = new WaterVO();
        WaterVO aug = new WaterVO();
        WaterVO sep = new WaterVO();
        WaterVO oct = new WaterVO();
        WaterVO nov = new WaterVO();
        WaterVO dec = new WaterVO();

        // 연도
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).get("G").length() > 6) {
                year.add(list.get(i).get("G").substring(0, 4));
            }
        }

        // 다른 파일 연도
        /*for (int i = 1; i < list.size(); i++) {
            if (list.get(i).get("A").contains("Year")) {
                year.add(list.get(i).get("A").split(":")[1].trim());
            }
        }*/

        
        /*for (int i = 1; i < list.size(); i++) {
            if (list.get(i).get("A").contains("YEAR")) {
                year.add(list.get(i).get("A").trim().split("\\s")[4]);
            }
        }*/

        
        /*for (int i = 1; i < list.size(); i++) {
            if (list.get(i).get("A").contains("YEAR")) {
                year.add(list.get(i).get("A").trim().split("\\s")[4]);
            }
        }*/

        String checkYear = year.get(0);
        int checkYearIndex = 0;
        String date = "";

        // water_level_info set
        String list1 = list.get(1).get("A");
        String list2 = list.get(2).get("A");
        String list3 = list.get(3).get("A");

        String stationName = list1.split(":")[1].replace("Latitude", "").trim();
        String river = list2.split(":")[1].split("\\s")[1] + " " + list.get(2).get("A").split(":")[1].split("\\s")[2];
        String altitude = list2.replaceAll("\\s", "").split(":")[2].replace("Catchmentarea", "");
        String catchmentArea = list2.replaceAll("\\s", "").split(":")[3];
        String province = list3.split(":")[1].split("\\s")[1];
        String country = list3.split(":")[2].split("\\s")[1] + " " + list.get(3).get("A").split(":")[2].split("\\s")[2];
        String agency = list3.split(":")[3].trim();
        String lonTemp = list1.split(":")[3].replace("East", "").trim();
        String latTemp = list1.split(":")[2].replace("Longitude", "").replace("North", "").trim();

        int lonDegree = Integer.parseInt(lonTemp.split("\\s")[0].replace("°", ""));
        int lonMin = Integer.parseInt(lonTemp.split("\\s")[1].replace("'", ""));
        int lonSec = Integer.parseInt(lonTemp.split("\\s")[2].replace("\"", ""));

        int latDegree = Integer.parseInt(latTemp.split("\\s")[0].replace("°", ""));
        int latMin = Integer.parseInt(latTemp.split("\\s")[1].replace("'", ""));
        int latSec = Integer.parseInt(latTemp.split("\\s")[2].replace("\"", ""));

        Double lon = (double) (lonDegree + ((lonMin / 60) + (lonSec / 3600)));
        Double lat = (double) (latDegree + ((latMin / 60) + (latSec / 3600)));

        WaterInfoVO waterInfoVO = new WaterInfoVO(stationName, lat, lon, river, altitude, catchmentArea, province, country, agency);

        if (waterMapper.stationNameCheck(stationName) != 1) {
            waterMapper.insertWaterInfo(waterInfoVO);
        }

        boolean nullCheck = true;
        StringBuilder logTxt = new StringBuilder();
        GregorianCalendar gc = new GregorianCalendar(); // 윤년계산

        // null값 로그
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("A").equals("Date") || list.get(i).get("A").equals("Data") || list.get(i).get("A").equals("Days")) {
                for (int j = i + 1; j < i + 32; j++) {
                    if (list.get(j).get("A").split("\\.")[0].length() < 2) {
                        date = "0" + list.get(j).get("A").split("\\.")[0];
                    } else {
                        date = list.get(j).get("A").split("\\.")[0];
                    }

                    logTxt.append(nullCheck(list.get(j).get("B"), list.get(j).get("C"), list.get(j).get("D"), list.get(j).get("E"),
                            list.get(j).get("F"), list.get(j).get("G"), list.get(j).get("H"), list.get(j).get("I"),
                            list.get(j).get("J"), list.get(j).get("K"), list.get(j).get("L"), list.get(j).get("M"), checkYear, date));

                    if (list.get(j).get("A").equals("31.00")) {
                        checkYearIndex += 1;
                        if (checkYearIndex < year.size()) {
                            checkYear = year.get(checkYearIndex);
                        }
                    }
                }
            }
        }

        if (logTxt.length() == 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).get("A").equals("Date") || list.get(i).get("A").equals("Data") || list.get(i).get("A").equals("Days")) {
                    for (int j = i + 1; j < i + 32; j++) {
                        List<WaterVO> result = new ArrayList<>();

                        if (list.get(j).get("A").split("\\.")[0].length() < 2) {
                            date = "0" + list.get(j).get("A").split("\\.")[0];
                        } else {
                            date = list.get(j).get("A").split("\\.")[0];
                        }

                        // 2월
                        if (gc.isLeapYear(Integer.parseInt(checkYear))) {
                            if (!date.equals("30") && !date.equals("31")) {
                                feb.setStationName(stationName);
                                feb.setYmd(checkYear + "-02-" + date);
                                feb.setLevel(Double.parseDouble(list.get(j).get("C")));
                                result.add(feb);
                            }
                        } else {
                            if (!date.equals("29") && !date.equals("30") && !date.equals("31")) {
                                feb.setStationName(stationName);
                                feb.setYmd(checkYear + "-02-" + date);
                                feb.setLevel(Double.parseDouble(list.get(j).get("C")));
                                result.add(feb);
                            }
                        }

                        // 4월, 6월, 9월, 11월
                        if (!date.equals("31")) {
                            apr.setStationName(stationName);
                            jun.setStationName(stationName);
                            sep.setStationName(stationName);
                            nov.setStationName(stationName);

                            apr.setYmd(checkYear + "-04-" + date);
                            jun.setYmd(checkYear + "-06-" + date);
                            sep.setYmd(checkYear + "-09-" + date);
                            nov.setYmd(checkYear + "-11-" + date);

                            apr.setLevel(Double.parseDouble(list.get(j).get("E")));
                            jun.setLevel(Double.parseDouble(list.get(j).get("G")));
                            sep.setLevel(Double.parseDouble(list.get(j).get("J")));
                            nov.setLevel(Double.parseDouble(list.get(j).get("L")));

                            result.add(apr);
                            result.add(jun);
                            result.add(sep);
                            result.add(nov);
                        }

                        // 1월, 3월, 5월, 7월, 8월, 10월, 12월
                        jan.setStationName(stationName);
                        mar.setStationName(stationName);
                        may.setStationName(stationName);
                        jul.setStationName(stationName);
                        aug.setStationName(stationName);
                        oct.setStationName(stationName);
                        dec.setStationName(stationName);

                        jan.setYmd(checkYear + "-01-" + date);
                        mar.setYmd(checkYear + "-03-" + date);
                        may.setYmd(checkYear + "-05-" + date);
                        jul.setYmd(checkYear + "-07-" + date);
                        aug.setYmd(checkYear + "-08-" + date);
                        oct.setYmd(checkYear + "-10-" + date);
                        dec.setYmd(checkYear + "-12-" + date);

                        jan.setLevel(Double.parseDouble(list.get(j).get("B")));
                        mar.setLevel(Double.parseDouble(list.get(j).get("D")));
                        may.setLevel(Double.parseDouble(list.get(j).get("F")));
                        jul.setLevel(Double.parseDouble(list.get(j).get("H")));
                        aug.setLevel(Double.parseDouble(list.get(j).get("I")));
                        oct.setLevel(Double.parseDouble(list.get(j).get("K")));
                        dec.setLevel(Double.parseDouble(list.get(j).get("M")));

                        result.add(jan);
                        result.add(mar);
                        result.add(may);
                        result.add(jul);
                        result.add(aug);
                        result.add(oct);
                        result.add(dec);

                        waterMapper.insertWaterLevel(result);

                        // 31일되면 다음 연도로
                        if (list.get(j).get("A").equals("31.00")) {
                            checkYearIndex += 1;
                            if (checkYearIndex < year.size()) {
                                checkYear = year.get(checkYearIndex);
                            }
                        }
                    }
                }
            }
        } else {
            log.error(String.valueOf(logTxt));
        }

        return String.valueOf(logTxt);
    }


    public StringBuilder nullCheck(String B, String C, String D, String E, String F, String G,
                                   String H, String I, String J, String K, String L, String M, String year, String date) {

        StringBuilder log = new StringBuilder();
        GregorianCalendar gc = new GregorianCalendar();

        if (B == null || B.equals("")) {
            log.append(year + "-01-" + date + " 수위값이 없습니다.");
        } else {
            try {
                Double.parseDouble(B);
            } catch (NumberFormatException e) {
                log.append(year + "-01-" + date + " 수위값이 문자열입니다.");
            }
        }

        if (gc.isLeapYear(Integer.parseInt(year))) {
            if (!date.equals("30") && !date.equals("31")) {
                if (C == null || C.equals("")) {
                    log.append(year + "-02-" + date + " 수위값이 없습니다.");
                } else {
                    try {
                        Double.parseDouble(C);
                    } catch (NumberFormatException e) {
                        log.append(year + "-02-" + date + " 수위값이 문자열입니다.");
                    }
                }
            }
        } else {
            if (!date.equals("29") && !date.equals("30") && !date.equals("31")) {
                if (C == null || C.equals("")) {
                    log.append(year + "-02-" + date + " 수위값이 없습니다.");
                } else {
                    try {
                        Double.parseDouble(C);
                    } catch (NumberFormatException e) {
                        log.append(year + "-02-" + date + " 수위값이 문자열입니다.");
                    }
                }
            }
        }


        if (!date.equals("31")) {
            if (E == null || E.equals("")) {
                log.append(year + "-04-" + date + " 수위값이 없습니다.");
            } else {
                try {
                    Double.parseDouble(E);
                } catch (NumberFormatException e) {
                    log.append(year + "-04-" + date + " 수위값이 문자열입니다.");
                }
            }
            if (G == null || G.equals("")) {
                log.append(year + "-06-" + date + " 수위값이 없습니다.");
            } else {
                try {
                    Double.parseDouble(G);
                } catch (NumberFormatException e) {
                    log.append(year + "-06-" + date + " 수위값이 문자열입니다.");
                }
            }
            if (J == null || J.equals("")) {
                log.append(year + "-09-" + date + " 수위값이 없습니다.");
            } else {
                try {
                    Double.parseDouble(J);
                } catch (NumberFormatException e) {
                    log.append(year + "-09-" + date + " 수위값이 문자열입니다.");
                }
            }
            if (L == null || L.equals("")) {
                log.append(year + "-11-" + date + " 수위값이 없습니다.");
            } else {
                try {
                    Double.parseDouble(L);
                } catch (NumberFormatException e) {
                    log.append(year + "-11-" + date + " 수위값이 문자열입니다.");
                }
            }
        }

        if (D == null || D.equals("")) {
            log.append(year + "-03-" + date + " 수위값이 없습니다.");
        } else {
            try {
                Double.parseDouble(D);
            } catch (NumberFormatException e) {
                log.append(year + "-03-" + date + " 수위값이 문자열입니다.");
            }
        }

        if (F == null || F.equals("")) {
            log.append(year + "-05-" + date + " 수위값이 없습니다.");
        } else {
            try {
                Double.parseDouble(F);
            } catch (NumberFormatException e) {
                log.append(year + "-05-" + date + " 수위값이 문자열입니다.");
            }
        }

        if (H == null || H.equals("")) {
            log.append(year + "-07-" + date + " 수위값이 없습니다.");
        } else {
            try {
                Double.parseDouble(H);
            } catch (NumberFormatException e) {
                log.append(year + "-07-" + date + " 수위값이 문자열입니다.");
            }
        }

        if (I == null || I.equals("")) {
            log.append(year + "-08-" + date + " 수위값이 없습니다.");
        } else {
            try {
                Double.parseDouble(I);
            } catch (NumberFormatException e) {
                log.append(year + "-08-" + date + " 수위값이 문자열입니다.");
            }
        }


        if (K == null || K.equals("")) {
            log.append(year + "-10-" + date + " 수위값이 없습니다.");
        } else {
            try {
                Double.parseDouble(K);
            } catch (NumberFormatException e) {
                log.append(year + "-10-" + date + " 수위값이 문자열입니다.");
            }
        }

        if (M == null || M.equals("")) {
            log.append(year + "-12-" + date + " 수위값이 없습니다.");
        } else {
            try {
                Double.parseDouble(M);
            } catch (NumberFormatException e) {
                log.append(year + "-12-" + date + " 수위값이 문자열입니다.");
            }
        }

        return log;
    }

    public void insertWaterLevelByPakkangoung() throws IOException {
        String filePath = "C:\\Users\\srmsq\\Desktop";
        String fileName = "Pakkangoung_waterlevel.xlsx";

        FileInputStream file = new FileInputStream(new File(filePath, fileName));
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        List<Map<String, String>> list;

        ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setOutputColumns("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M");
        list = ExcelRead.read(excelReadOption, workbook, 0);
        List<String> year = new ArrayList<>();

        WaterVO jan = new WaterVO();
        WaterVO feb = new WaterVO();
        WaterVO mar = new WaterVO();
        WaterVO apr = new WaterVO();
        WaterVO may = new WaterVO();
        WaterVO jun = new WaterVO();
        WaterVO jul = new WaterVO();
        WaterVO aug = new WaterVO();
        WaterVO sep = new WaterVO();
        WaterVO oct = new WaterVO();
        WaterVO nov = new WaterVO();
        WaterVO dec = new WaterVO();


        // 연도
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).get("G").length() > 6) {
                year.add(list.get(i).get("G").substring(0, 4));
            }
        }

        int check = 85;
        String checkYear = year.get(0);
        int checkYearIndex = 0;
        String date = "";

        String stationName = list.get(1).get("A").split(":")[1].replace("Latitude", "").trim();
        String river = list.get(2).get("A").split(":")[1].split("\\s")[1] + " " + list.get(2).get("A").split(":")[1].split("\\s")[2];
        String altitude = list.get(2).get("A").replaceAll("\\s", "").split(":")[2].replace("Catchmentarea", "");
        String catchmentArea = list.get(2).get("A").replaceAll("\\s", "").split(":")[3];
        String province = list.get(3).get("A").split(":")[1].split("\\s")[1];
        String country = list.get(3).get("A").split(":")[2].split("\\s")[1] + " " + list.get(3).get("A").split(":")[2].split("\\s")[2];
        String agency = list.get(3).get("A").split(":")[3].trim();
        String lon = list.get(1).get("A").split(":")[3].replace("East", "").trim();
        String lat = list.get(1).get("A").split(":")[2].replace("Longitude", "").replace("North", "").trim();

        // 수위 구하기
        for (int i = 0; i < list.size(); i++) {

            if (i == 0) {
                i += 9;
            }

            if (i <= 40) {
                if ((i % 40) == 0) {
                    i += 14;
                }
            } else {
                if (i == check) {
                    check = i + 45;
                    i += 14;
                }
            }

            if (i > list.size()) {
                break;
            }

            if (list.get(i).get("A").split("\\.")[0].length() < 2) {
                date = "0" + list.get(i).get("A").split("\\.")[0];
            } else {
                date = list.get(i).get("A").split("\\.")[0];
            }

            jan.setStationName(stationName);
            feb.setStationName(stationName);
            mar.setStationName(stationName);
            apr.setStationName(stationName);
            may.setStationName(stationName);
            jun.setStationName(stationName);
            jul.setStationName(stationName);
            aug.setStationName(stationName);
            sep.setStationName(stationName);
            oct.setStationName(stationName);
            nov.setStationName(stationName);
            dec.setStationName(stationName);

            jan.setYmd(checkYear + "-01-" + date);
            feb.setYmd(checkYear + "-02-" + date);
            mar.setYmd(checkYear + "-03-" + date);
            apr.setYmd(checkYear + "-04-" + date);
            may.setYmd(checkYear + "-05-" + date);
            jun.setYmd(checkYear + "-06-" + date);
            jul.setYmd(checkYear + "-07-" + date);
            aug.setYmd(checkYear + "-08-" + date);
            sep.setYmd(checkYear + "-09-" + date);
            oct.setYmd(checkYear + "-10-" + date);
            nov.setYmd(checkYear + "-11-" + date);
            dec.setYmd(checkYear + "-12-" + date);

            jan.setLevel(list.get(i).get("B") != "" ? Double.parseDouble(list.get(i).get("B")) : Double.parseDouble("0.0"));
            feb.setLevel(list.get(i).get("C") != "" ? Double.parseDouble(list.get(i).get("C")) : Double.parseDouble("0.0"));
            mar.setLevel(list.get(i).get("D") != "" ? Double.parseDouble(list.get(i).get("D")) : Double.parseDouble("0.0"));
            apr.setLevel(list.get(i).get("E") != "" ? Double.parseDouble(list.get(i).get("E")) : Double.parseDouble("0.0"));
            may.setLevel(list.get(i).get("F") != "" ? Double.parseDouble(list.get(i).get("F")) : Double.parseDouble("0.0"));
            jun.setLevel(list.get(i).get("G") != "" ? Double.parseDouble(list.get(i).get("G")) : Double.parseDouble("0.0"));
            jul.setLevel(list.get(i).get("H") != "" ? Double.parseDouble(list.get(i).get("H")) : Double.parseDouble("0.0"));
            aug.setLevel(list.get(i).get("I") != "" ? Double.parseDouble(list.get(i).get("I")) : Double.parseDouble("0.0"));
            sep.setLevel(list.get(i).get("J") != "" ? Double.parseDouble(list.get(i).get("J")) : Double.parseDouble("0.0"));
            oct.setLevel(list.get(i).get("K") != "" ? Double.parseDouble(list.get(i).get("K")) : Double.parseDouble("0.0"));
            nov.setLevel(list.get(i).get("L") != "" ? Double.parseDouble(list.get(i).get("L")) : Double.parseDouble("0.0"));
            dec.setLevel(list.get(i).get("M") != "" ? Double.parseDouble(list.get(i).get("M")) : Double.parseDouble("0.0"));

            List<WaterVO> result = new ArrayList<>();
            result.add(jan);
            result.add(feb);
            result.add(mar);
            result.add(apr);
            result.add(may);
            result.add(jun);
            result.add(jul);
            result.add(aug);
            result.add(sep);
            result.add(oct);
            result.add(nov);
            result.add(dec);

//            waterMapper.insertWaterLevel(result);

            System.out.println(i + " / " + list.get(i));

            if (list.get(i).get("A").equals("31.00")) {
                checkYearIndex += 1;
                if (checkYearIndex < year.size()) {
                    checkYear = year.get(checkYearIndex);
                }
            }
        }

        workbook.close();

    }

    public void insertWaterLevelByPhiengluang() throws IOException {
        String filePath = "C:\\Users\\srmsq\\Desktop";
        String fileName = "Phiengluang.xlsx";

        FileInputStream file = new FileInputStream(new File(filePath, fileName));
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        List<Map<String, String>> list;

        ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setOutputColumns("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M");
        list = ExcelRead.read(excelReadOption, workbook, 1);

        List<String> year = new ArrayList<>();

        WaterVO jan = new WaterVO();
        WaterVO feb = new WaterVO();
        WaterVO mar = new WaterVO();
        WaterVO apr = new WaterVO();
        WaterVO may = new WaterVO();
        WaterVO jun = new WaterVO();
        WaterVO jul = new WaterVO();
        WaterVO aug = new WaterVO();
        WaterVO sep = new WaterVO();
        WaterVO oct = new WaterVO();
        WaterVO nov = new WaterVO();
        WaterVO dec = new WaterVO();

        // 연도
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).get("A").contains("Year")) {
                year.add(list.get(i).get("A").split(":")[1].trim());
            }
        }

        String checkYear = year.get(0);
        int checkYearIndex = 0;
        String date = "";

        String stationName = list.get(1).get("A").split(":")[1].replace("Latitude", "").trim();
        String river = list.get(2).get("A").split(":")[1].split("\\s")[1] + " " + list.get(2).get("A").split(":")[1].split("\\s")[2];
        String altitude = list.get(2).get("A").replaceAll("\\s", "").split(":")[2].replace("Catchmentarea", "");
        String catchmentArea = list.get(2).get("A").replaceAll("\\s", "").split(":")[3];
        String province = list.get(3).get("A").split(":")[1].split("\\s")[1];
        String country = list.get(3).get("A").split(":")[2].split("\\s")[1] + " " + list.get(3).get("A").split(":")[2].split("\\s")[2];
        String agency = list.get(3).get("A").split(":")[3].trim();
        String lon = list.get(1).get("A").split(":")[3].replace("East", "").trim();
        String lat = list.get(1).get("A").split(":")[2].replace("Longitude", "").replace("North", "").trim();

        int check = 38;

        // 수위 구하기
        for (int i = 0; i < list.size(); i++) {

            if (i == 0) {
                i += 7;
            }

            if (i == check) {
                i += 11;
                check = i + 31;
            }

            if (i > list.size()) {
                break;
            }

            if (list.get(i).get("A").split("\\.")[0].length() < 2) {
                date = "0" + list.get(i).get("A").split("\\.")[0];
            } else {
                date = list.get(i).get("A").split("\\.")[0];
            }

            jan.setStationName(stationName);
            feb.setStationName(stationName);
            mar.setStationName(stationName);
            apr.setStationName(stationName);
            may.setStationName(stationName);
            jun.setStationName(stationName);
            jul.setStationName(stationName);
            aug.setStationName(stationName);
            sep.setStationName(stationName);
            oct.setStationName(stationName);
            nov.setStationName(stationName);
            dec.setStationName(stationName);

            jan.setYmd(checkYear + "-01-" + date);
            feb.setYmd(checkYear + "-02-" + date);
            mar.setYmd(checkYear + "-03-" + date);
            apr.setYmd(checkYear + "-04-" + date);
            may.setYmd(checkYear + "-05-" + date);
            jun.setYmd(checkYear + "-06-" + date);
            jul.setYmd(checkYear + "-07-" + date);
            aug.setYmd(checkYear + "-08-" + date);
            sep.setYmd(checkYear + "-09-" + date);
            oct.setYmd(checkYear + "-10-" + date);
            nov.setYmd(checkYear + "-11-" + date);
            dec.setYmd(checkYear + "-12-" + date);

            jan.setLevel(list.get(i).get("B") != "" ? Double.parseDouble(list.get(i).get("B")) : Double.parseDouble("0.0"));
            feb.setLevel(list.get(i).get("C") != "" ? Double.parseDouble(list.get(i).get("C")) : Double.parseDouble("0.0"));
            mar.setLevel(list.get(i).get("D") != "" ? Double.parseDouble(list.get(i).get("D")) : Double.parseDouble("0.0"));
            apr.setLevel(list.get(i).get("E") != "" ? Double.parseDouble(list.get(i).get("E")) : Double.parseDouble("0.0"));
            may.setLevel(list.get(i).get("F") != "" ? Double.parseDouble(list.get(i).get("F")) : Double.parseDouble("0.0"));
            jun.setLevel(list.get(i).get("G") != "" ? Double.parseDouble(list.get(i).get("G")) : Double.parseDouble("0.0"));
            jul.setLevel(list.get(i).get("H") != "" ? Double.parseDouble(list.get(i).get("H")) : Double.parseDouble("0.0"));
            aug.setLevel(list.get(i).get("I") != "" ? Double.parseDouble(list.get(i).get("I")) : Double.parseDouble("0.0"));
            sep.setLevel(list.get(i).get("J") != "" ? Double.parseDouble(list.get(i).get("J")) : Double.parseDouble("0.0"));
            oct.setLevel(list.get(i).get("K") != "" ? Double.parseDouble(list.get(i).get("K")) : Double.parseDouble("0.0"));
            nov.setLevel(list.get(i).get("L") != "" ? Double.parseDouble(list.get(i).get("L")) : Double.parseDouble("0.0"));
            dec.setLevel(list.get(i).get("M") != "" ? Double.parseDouble(list.get(i).get("M")) : Double.parseDouble("0.0"));

            List<WaterVO> result = new ArrayList<>();
            result.add(jan);
            result.add(feb);
            result.add(mar);
            result.add(apr);
            result.add(may);
            result.add(jun);
            result.add(jul);
            result.add(aug);
            result.add(sep);
            result.add(oct);
            result.add(nov);
            result.add(dec);

//            waterMapper.insertWaterLevel(result);

            if (list.get(i).get("A").equals("31.00")) {
                checkYearIndex += 1;
                if (checkYearIndex < year.size()) {
                    checkYear = year.get(checkYearIndex);
                }
            }
        }

        workbook.close();

    }

    public void insertWaterLevelByVernkham() throws IOException {
        String filePath = "C:\\Users\\srmsq\\Desktop";
        String fileName = "Vernkham.xlsx";

        FileInputStream file = new FileInputStream(new File(filePath, fileName));
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        List<Map<String, String>> list;

        ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setOutputColumns("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M");
        list = ExcelRead.read(excelReadOption, workbook, 0);

        List<String> year = new ArrayList<>();

        WaterVO jan = new WaterVO();
        WaterVO feb = new WaterVO();
        WaterVO mar = new WaterVO();
        WaterVO apr = new WaterVO();
        WaterVO may = new WaterVO();
        WaterVO jun = new WaterVO();
        WaterVO jul = new WaterVO();
        WaterVO aug = new WaterVO();
        WaterVO sep = new WaterVO();
        WaterVO oct = new WaterVO();
        WaterVO nov = new WaterVO();
        WaterVO dec = new WaterVO();

        // 연도
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).get("A").contains("YEAR")) {
                year.add(list.get(i).get("A").trim().split("\\s")[4]);
            }
        }

        String checkYear = year.get(0);
        int checkYearIndex = 0;
        String date = "";

        String stationName = list.get(1).get("A").split(":")[1].replace("Latitude", "").trim();
        String river = list.get(2).get("A").split(":")[1].split("\\s")[1] + " " + list.get(2).get("A").split(":")[1].split("\\s")[2];
        String altitude = list.get(2).get("A").replaceAll("\\s", "").split(":")[2].replace("Catchmentarea", "");
        String catchmentArea = list.get(2).get("A").replaceAll("\\s", "").split(":")[3];
        String province = list.get(3).get("A").split(":")[1].replace("Country", "").trim();
        String country = list.get(3).get("A").split(":")[2].split("\\s")[1] + " " + list.get(3).get("A").split(":")[2].split("\\s")[2];
        String agency = list.get(3).get("A").split(":")[3].trim();
        String lon = list.get(1).get("A").split(":")[3].replace("East", "").trim();
        String lat = list.get(1).get("A").split(":")[2].replace("Longitude", "").replace("North", "").trim();

        List<Map<String, String>> list2 = new ArrayList<>();

        // 수위 구하기
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).get("A").equals("")) {
                list2.add(list.get(i));
            }
        }

        int check = 39;
        for (int i = 8; i < list2.size(); i++) {
            if (i == check) {
                i += 11;
                check = i + 31;
            }

            if (i > list2.size()) {
                break;
            }

            if (list2.get(i).get("A").split("\\.")[0].length() < 2) {
                date = "0" + list2.get(i).get("A").split("\\.")[0];
            } else {
                date = list2.get(i).get("A").split("\\.")[0];
            }

            jan.setStationName(stationName);
            feb.setStationName(stationName);
            mar.setStationName(stationName);
            apr.setStationName(stationName);
            may.setStationName(stationName);
            jun.setStationName(stationName);
            jul.setStationName(stationName);
            aug.setStationName(stationName);
            sep.setStationName(stationName);
            oct.setStationName(stationName);
            nov.setStationName(stationName);
            dec.setStationName(stationName);

            jan.setYmd(checkYear + "-01-" + date);
            feb.setYmd(checkYear + "-02-" + date);
            mar.setYmd(checkYear + "-03-" + date);
            apr.setYmd(checkYear + "-04-" + date);
            may.setYmd(checkYear + "-05-" + date);
            jun.setYmd(checkYear + "-06-" + date);
            jul.setYmd(checkYear + "-07-" + date);
            aug.setYmd(checkYear + "-08-" + date);
            sep.setYmd(checkYear + "-09-" + date);
            oct.setYmd(checkYear + "-10-" + date);
            nov.setYmd(checkYear + "-11-" + date);
            dec.setYmd(checkYear + "-12-" + date);

            jan.setLevel(list2.get(i).get("B") != "" ? Double.parseDouble(list2.get(i).get("B")) : Double.parseDouble("0.0"));
            feb.setLevel(list2.get(i).get("C") != "" ? Double.parseDouble(list2.get(i).get("C")) : Double.parseDouble("0.0"));
            mar.setLevel(list2.get(i).get("D") != "" ? Double.parseDouble(list2.get(i).get("D")) : Double.parseDouble("0.0"));
            apr.setLevel(list2.get(i).get("E") != "" ? Double.parseDouble(list2.get(i).get("E")) : Double.parseDouble("0.0"));
            may.setLevel(list2.get(i).get("F") != "" ? Double.parseDouble(list2.get(i).get("F")) : Double.parseDouble("0.0"));
            jun.setLevel(list2.get(i).get("G") != "" ? Double.parseDouble(list2.get(i).get("G")) : Double.parseDouble("0.0"));
            jul.setLevel(list2.get(i).get("H") != "" ? Double.parseDouble(list2.get(i).get("H")) : Double.parseDouble("0.0"));
            aug.setLevel(list2.get(i).get("I") != "" ? Double.parseDouble(list2.get(i).get("I")) : Double.parseDouble("0.0"));
            sep.setLevel(list2.get(i).get("J") != "" ? Double.parseDouble(list2.get(i).get("J")) : Double.parseDouble("0.0"));
            oct.setLevel(list2.get(i).get("K") != "" ? Double.parseDouble(list2.get(i).get("K")) : Double.parseDouble("0.0"));
            nov.setLevel(list2.get(i).get("L") != "" ? Double.parseDouble(list2.get(i).get("L")) : Double.parseDouble("0.0"));
            dec.setLevel(list2.get(i).get("M") != "" ? Double.parseDouble(list2.get(i).get("M")) : Double.parseDouble("0.0"));

            List<WaterVO> result = new ArrayList<>();
            result.add(jan);
            result.add(feb);
            result.add(mar);
            result.add(apr);
            result.add(may);
            result.add(jun);
            result.add(jul);
            result.add(aug);
            result.add(sep);
            result.add(oct);
            result.add(nov);
            result.add(dec);

//            waterMapper.insertWaterLevel(result);

            if (list2.get(i).get("A").equals("31.00")) {
                checkYearIndex += 1;
                if (checkYearIndex < year.size()) {
                    checkYear = year.get(checkYearIndex);
                }
            }
        }

        workbook.close();

    }

    public void insertWaterLevelByHNamNgumPhiengluang() throws IOException {
        String filePath = "C:\\Users\\srmsq\\Desktop";
        String fileName = "H Nam Ngum Phiengluang.xls";

        FileInputStream file = new FileInputStream(new File(filePath, fileName));
        HSSFWorkbook workbook = new HSSFWorkbook(file);

        List<Map<String, String>> list;

        ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setOutputColumns("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M");
        list = ExcelRead.readHSSF(excelReadOption, workbook, 0);

        List<String> year = new ArrayList<>();

        WaterVO jan = new WaterVO();
        WaterVO feb = new WaterVO();
        WaterVO mar = new WaterVO();
        WaterVO apr = new WaterVO();
        WaterVO may = new WaterVO();
        WaterVO jun = new WaterVO();
        WaterVO jul = new WaterVO();
        WaterVO aug = new WaterVO();
        WaterVO sep = new WaterVO();
        WaterVO oct = new WaterVO();
        WaterVO nov = new WaterVO();
        WaterVO dec = new WaterVO();

        // 연도
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).get("A").contains("YEAR")) {
                year.add(list.get(i).get("A").trim().split("\\s")[4]);
            }
        }

        String checkYear = year.get(0);
        int checkYearIndex = 0;
        String date = "";

        String stationName = list.get(1).get("A").split(":")[1].replace("Latitude", "").trim();
        String river = list.get(2).get("A").split(":")[1].split("\\s")[1] + " " + list.get(2).get("A").split(":")[1].split("\\s")[2];
        String altitude = list.get(2).get("A").replaceAll("\\s", "").split(":")[2].replace("Catchmentarea", "");
        String catchmentArea = list.get(2).get("A").replaceAll("\\s", "").split(":")[3];
        String province = list.get(3).get("A").split(":")[1].split("\\s")[1];
        String country = list.get(3).get("A").split(":")[2].split("\\s")[1] + " " + list.get(3).get("A").split(":")[2].split("\\s")[2];
        String agency = list.get(3).get("A").split(":")[3].trim();
        String lon = list.get(1).get("A").split(":")[3].replace("East", "").trim();
        String lat = list.get(1).get("A").split(":")[2].replace("Longitude", "").replace("North", "").trim();

        List<Map<String, String>> list2 = ExcelRead.onlyDoubleRead(excelReadOption, workbook, 0);

        // 수위 구하기
        for (int i = 0; i < list2.size(); i++) {
            if (!list2.get(i).get("A").equals("")) {
                if (list2.get(i).get("A").split("\\.")[0].length() < 2) {
                    date = "0" + list2.get(i).get("A").split("\\.")[0];
                } else {
                    date = list2.get(i).get("A").split("\\.")[0];
                }

                jan.setStationName(stationName);
                feb.setStationName(stationName);
                mar.setStationName(stationName);
                apr.setStationName(stationName);
                may.setStationName(stationName);
                jun.setStationName(stationName);
                jul.setStationName(stationName);
                aug.setStationName(stationName);
                sep.setStationName(stationName);
                oct.setStationName(stationName);
                nov.setStationName(stationName);
                dec.setStationName(stationName);

                jan.setYmd(checkYear + "-01-" + date);
                feb.setYmd(checkYear + "-02-" + date);
                mar.setYmd(checkYear + "-03-" + date);
                apr.setYmd(checkYear + "-04-" + date);
                may.setYmd(checkYear + "-05-" + date);
                jun.setYmd(checkYear + "-06-" + date);
                jul.setYmd(checkYear + "-07-" + date);
                aug.setYmd(checkYear + "-08-" + date);
                sep.setYmd(checkYear + "-09-" + date);
                oct.setYmd(checkYear + "-10-" + date);
                nov.setYmd(checkYear + "-11-" + date);
                dec.setYmd(checkYear + "-12-" + date);

                jan.setLevel(list2.get(i).get("B") != "" ? Double.parseDouble(list2.get(i).get("B")) : Double.parseDouble("0.0"));
                feb.setLevel(list2.get(i).get("C") != "" ? Double.parseDouble(list2.get(i).get("C")) : Double.parseDouble("0.0"));
                mar.setLevel(list2.get(i).get("D") != "" ? Double.parseDouble(list2.get(i).get("D")) : Double.parseDouble("0.0"));
                apr.setLevel(list2.get(i).get("E") != "" ? Double.parseDouble(list2.get(i).get("E")) : Double.parseDouble("0.0"));
                may.setLevel(list2.get(i).get("F") != "" ? Double.parseDouble(list2.get(i).get("F")) : Double.parseDouble("0.0"));
                jun.setLevel(list2.get(i).get("G") != "" ? Double.parseDouble(list2.get(i).get("G")) : Double.parseDouble("0.0"));
                jul.setLevel(list2.get(i).get("H") != "" ? Double.parseDouble(list2.get(i).get("H")) : Double.parseDouble("0.0"));
                aug.setLevel(list2.get(i).get("I") != "" ? Double.parseDouble(list2.get(i).get("I")) : Double.parseDouble("0.0"));
                sep.setLevel(list2.get(i).get("J") != "" ? Double.parseDouble(list2.get(i).get("J")) : Double.parseDouble("0.0"));
                oct.setLevel(list2.get(i).get("K") != "" ? Double.parseDouble(list2.get(i).get("K")) : Double.parseDouble("0.0"));
                nov.setLevel(list2.get(i).get("L") != "" ? Double.parseDouble(list2.get(i).get("L")) : Double.parseDouble("0.0"));
                dec.setLevel(list2.get(i).get("M") != "" ? Double.parseDouble(list2.get(i).get("M")) : Double.parseDouble("0.0"));

                List<WaterVO> result = new ArrayList<>();
                result.add(jan);
                result.add(feb);
                result.add(mar);
                result.add(apr);
                result.add(may);
                result.add(jun);
                result.add(jul);
                result.add(aug);
                result.add(sep);
                result.add(oct);
                result.add(nov);
                result.add(dec);

//                waterMapper.insertWaterLevel(result);

                if (list2.get(i).get("A").equals("31.00")) {
                    checkYearIndex += 1;
                    if (checkYearIndex < year.size()) {
                        checkYear = year.get(checkYearIndex);
                    }
                }
            }
        }

        workbook.close();

    }

    public void insertFacility() throws IOException {
        if(waterMapper.countFacility() != 0) {
            waterMapper.deleteFacility();
        }
        String filePath = "C:\\Users\\srmsq\\Desktop\\facility.hyd";
        File file = new File(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "euc-kr"));

        List<String> list = new ArrayList<>();

        List<String> idTemp = new ArrayList<>();
        List<String> nameTemp = new ArrayList<>();
        List<String> id = new ArrayList<>();
        List<String> name = new ArrayList<>();

        List<String[]> facility = new ArrayList<>();

        String str;
        // 파일 한줄씩
        while ((str = reader.readLine()) != null) {
            list.add(str);
            if (str.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
                String tempString = str.substring(2).replaceAll("\\s", "").trim();
                idTemp = Arrays.asList(tempString.split("[가-힣]"));
                nameTemp = Arrays.asList(tempString.split("[0-9]"));
            }
        }

        // id
        for (int i = 0; i < idTemp.size(); i++) {
            if (!idTemp.get(i).equals("")) {
                id.add(idTemp.get(i).trim());
            }
        }

        // name
        for (int i = 0; i < nameTemp.size(); i++) {
            if (!nameTemp.get(i).equals("")) {
                name.add(nameTemp.get(i).trim());
            }
        }

        // facility info
        for (int i = 8; i < list.size(); i++) {
            facility.add(StringUtils.split(list.get(i))); // 공백길이상관없이 공백기준으로 가져옴
        }

        List<FacilityVO> result = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < facility.size(); i++) {
            for (int j = 0; j < facility.get(i).length; j+=6) {
                if(j+1 < facility.get(i).length) {
                    if (j != 0) {
                        if (j % 6 == 0) {
                            index++;
                        }
                    }
                   /*System.out.println(j + " " + id.get(index) + " " + " " + name.get(index) + " " + facility.get(i)[0] + " " +
                                      facility.get(i)[j + 1] + " " + facility.get(i)[j + 2] + " " + facility.get(i)[j + 3] + " " + facility.get(i)[j + 4] + " " + facility.get(i)[j + 5] + " " + facility.get(i)[j + 6]);*/
                    FacilityVO facilityVO = new FacilityVO();
                    facilityVO.setId(id.get(index));
                    facilityVO.setName(name.get(index));
                    facilityVO.setYmdHm(facility.get(i)[0].replace("@"," "));
                    facilityVO.setWaterLevel( facility.get(i)[j + 1]);
                    facilityVO.setInflow( facility.get(i)[j + 2]);
                    facilityVO.setTotalDischarge( facility.get(i)[j + 3]);
                    facilityVO.setFall(facility.get(i)[j + 4]);
                    facilityVO.setLowYield( facility.get(i)[j + 5]);
                    facilityVO.setReservoir( facility.get(i)[j + 6]);
                    result.add(facilityVO);
                } else {
                    index = 0;
                }
            }
            waterMapper.insertFacility(result);
            result.clear();
        }
    }

    public void insertRainFall() throws IOException {
        if (waterMapper.countRainFall() != 0) {
            waterMapper.deleteRainFall();
        }
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\srmsq\\Desktop\\rainfall.hyd"));

        List<String> list = new ArrayList<>();
        List<String[]> rainfall = new ArrayList<>();
        List<RainFallVO> rainFallVOList = new ArrayList<>();

        String str;
        // 파일 한줄씩
        while ((str = reader.readLine()) != null) {
            list.add(str);
        }

        // 관측소 id
        String[] id = list.get(0).trim().split("\\s\\s");

        // 강우량
        for (int i = 1; i < list.size(); i++) {
            rainfall.add(list.get(i).trim().split("\\s\\s\\s\\s"));
        }

        for (int i = 0; i < rainfall.size(); i++) {
            for (int j = 0; j < rainfall.get(i).length; j++) {
                if (j + 1 < rainfall.get(i).length) {
//                    System.out.println(id[j] + " " + rainfall.get(i)[0] + " " + rainfall.get(i)[j + 1]);
                    RainFallVO rainFallVO = new RainFallVO();
                    rainFallVO.setId(id[j]);
                    rainFallVO.setYmdHm(rainfall.get(i)[0].replace("@", " "));
                    rainFallVO.setFall(rainfall.get(i)[j + 1].trim());
                    rainFallVOList.add(rainFallVO);
                }
            }
            waterMapper.insertRainFall(rainFallVOList);
            rainFallVOList.clear();
        }
    }

    public void generateRainFallFile() {
        List<RainFallVO> rainFallVOList = waterMapper.selectRainFall();
        List<String> rainFallId = waterMapper.selectRainFallId();
        String fileName = "C:\\Users\\srmsq\\Desktop\\rerainfall.hyd";
        File file = new File(fileName);

        StringBuilder sb = new StringBuilder();

        // 0 부터 rainFallVOList.size()까지 돌리면서 rainFallId.size() 마다 담아
        try {
            if (file.exists()) {
                file.delete();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
            bw.write("                   ");

            for (int i = 0; i < rainFallId.size(); i++) {
                if ((i + 1) == rainFallId.size()) {
                    bw.write(rainFallId.get(i));
                } else {
                    bw.write(rainFallId.get(i) + "  ");
                }
            }

            bw.write("\n");

            for (int i = 0; i < rainFallVOList.size(); i++) {
                sb.append(rainFallVOList.get(i).getFall() + "    ");
                if (i != 0) {
                    if ((i % (rainFallId.size() - 1)) == 0) {
                        sb.delete(sb.length() - 4, sb.length());
                        sb.insert(0, rainFallVOList.get(i).getYmdHm().replace(" ", "@") + "     ");
                        sb.append("\n");
                        bw.write(sb.toString());
                        sb.setLength(0);
                    }
                }
            }

            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateFacilityFile() {
        List<FacilityVO> facilityVOList = waterMapper.selectFacility();
        List<Map<String,String>> facilityIdName = waterMapper.selectFacilityIdName();
        String fileName = "C:\\Users\\srmsq\\Desktop\\refacility.hyd";
        File file = new File(fileName);

        StringBuilder sb = new StringBuilder();

        try {
            if (file.exists()) {
                file.delete();
            }

//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName, "EUC-KR"));

            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), "EUC-KR");


            //BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true) );
//            bw.write(facilityIdName.size() + "                ");
            //bw.write(String.format("%-18s", facilityIdName.size()));
            osw.write(String.format("%-18s", facilityIdName.size()));

            // id + name
            for (int i = 0; i < facilityIdName.size(); i++) {
//                bw.write(facilityIdName.get(i).get("id") + " " + facilityIdName.get(i).get("name") + "                                            ");
                //bw.write(String.format("%-55s", facilityIdName.get(i).get("id") + " " + facilityIdName.get(i).get("name")));
                osw.write(String.format("%-55s", facilityIdName.get(i).get("id") + " " + facilityIdName.get(i).get("name")));
            }

            int index = facilityIdName.size();
            // facility
            for(int i=0; i < facilityVOList.size(); i++) {
                if (i == index) {
                    //bw.write("\n");
                    osw.write("\n");
                    sb.insert(0, String.format("%-17s",facilityVOList.get(index-1).getYmdHm().replace(" ","@")));
//                    bw.write(sb.toString());
                    osw.write(sb.toString());
                    sb.setLength(0);
                    index += facilityIdName.size();
                }

                sb.append(String.format("%8s",facilityVOList.get(i).getWaterLevel()) + String.format("%10s",facilityVOList.get(i).getInflow()) + String.format("%10s",facilityVOList.get(i).getTotalDischarge()) +
                          String.format("%10s",facilityVOList.get(i).getFall()) + String.format("%10s",facilityVOList.get(i).getLowYield()) + String.format("%10s",facilityVOList.get(i).getReservoir()));

                // 마지막인애는 위에 조건이 안되서
                if(i+1 == facilityVOList.size()) {
//                    bw.write("\n");
                    osw.write("\n");
                    sb.insert(0, facilityVOList.get(index-1).getYmdHm().replace(" ","@") + " ");

//                    bw.write(sb.toString());
                    osw.write(sb.toString());
                }

            }

//            bw.flush();
//            bw.close();
            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
