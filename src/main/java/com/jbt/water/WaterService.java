package com.jbt.water;

import com.jbt.water.util.ExcelRead;
import com.jbt.water.util.ExcelReadOption;
import com.jbt.water.vo.WaterInfoVO;
import com.jbt.water.vo.WaterVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaterService {

    private final WaterMapper waterMapper;

    public StringBuilder nullCheck(String B, String C, String D, String E, String F, String G,
                                   String H, String I, String J, String K, String L, String M) {
        StringBuilder log = new StringBuilder();

        if(B == null && B.equals("")) {
            log.append("");
        }

        if(C == null && C.equals("")) {

        }

        if(D == null && D.equals("")) {

        }

        if(E == null && E.equals("")) {

        }

        if(F == null && F.equals("")) {

        }

        if(G == null && G.equals("")) {

        }

        if(H == null && H.equals("")) {

        }

        if(I == null && I.equals("")) {

        }

        if(J == null && J.equals("")) {

        }

        if(K == null && K.equals("")) {

        }

        if(L == null && L.equals("")) {

        }

        if(M == null && M.equals("")) {

        }

        return log;
    }

    public void test() throws IOException {
        String filePath = "C:\\Users\\srmsq\\Desktop";
        String fileName = "Pakkangoung_waterlevel.xlsx";
//        String fileName = "Phiengluang.xlsx";
//        String fileName = "Vernkham.xlsx";
//        String fileName = "H Nam Ngum Phiengluang.xls";

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

        System.out.println(year);

        String checkYear = year.get(0);
        int checkYearIndex = 0;
        String date = "";

        // water_level_info set
        String stationName = list.get(1).get("A").split(":")[1].replace("Latitude", "").trim();
        String river = list.get(2).get("A").split(":")[1].split("\\s")[1] + " " + list.get(2).get("A").split(":")[1].split("\\s")[2];
        String altitude = list.get(2).get("A").replaceAll("\\s","").split(":")[2].replace("Catchmentarea","");
        String catchmentArea = list.get(2).get("A").replaceAll("\\s","").split(":")[3];
        String province = list.get(3).get("A").split(":")[1].split("\\s")[1];
        String country = list.get(3).get("A").split(":")[2].split("\\s")[1] + " " + list.get(3).get("A").split(":")[2].split("\\s")[2];
        String agency = list.get(3).get("A").split(":")[3].trim();
        String lon = list.get(1).get("A").split(":")[3].replace("East", "").trim();
        String lat = list.get(1).get("A").split(":")[2].replace("Longitude", "").replace("North", "").trim();
        WaterInfoVO waterInfoVO = new WaterInfoVO(stationName, lat, lon, river, altitude, catchmentArea, province, country, agency);
        int degree = Integer.parseInt(lon.split("\\s")[0].replace("°",""));
        int min = Integer.parseInt(lon.split("\\s")[1].replace("'",""));
        int sec = Integer.parseInt(lon.split("\\s")[2].replace("\"",""));

        /*System.out.println(degree + "/" + min + "/" + sec);
        System.out.println(lon);
        System.out.println(degree + ((min/60)+(sec/3600)));*/

        if(waterMapper.stationNameCheck(stationName) != 1) {
            waterMapper.insertWaterInfo(waterInfoVO);
        }

        boolean nullCheck = true;
        StringBuilder log = new StringBuilder();
        GregorianCalendar gc = new GregorianCalendar();

        for(int i=0; i < list.size(); i++) {
            // A에 Date or Data 나오고난후
            if(list.get(i).get("A").equals("Date") || list.get(i).get("A").equals("Data") || list.get(i).get("A").equals("Days")) {
                for(int j=i + 1; j < i + 32; j++) {
                    List<WaterVO> result = new ArrayList<>();

                    log.append(nullCheck(list.get(i).get("B"), list.get(i).get("C"), list.get(i).get("D"), list.get(i).get("E"),
                                         list.get(i).get("F"), list.get(i).get("G"), list.get(i).get("H"), list.get(i).get("I"),
                                         list.get(i).get("J"), list.get(i).get("K"), list.get(i).get("L"), list.get(i).get("M")));

                    if(gc.isLeapYear(Integer.parseInt(checkYear))) {
                        System.out.println("윤년 : " + checkYear);
                    }
                    // 윤년 계산
                    if(Integer.parseInt(checkYear) % 4 == 0) {
//                        System.out.println(checkYear);
                        if(Integer.parseInt(checkYear)%100 != 0) {
                            if(Integer.parseInt(checkYear)%400 == 0) {
                                System.out.println("checkYear : " + checkYear);
                            }
                        }
                    }

                    /*if(log != "") {
                        nullCheck = false;
                    }*/

                    if (list.get(j).get("A").split("\\.")[0].length() < 2) {
                        date = "0" + list.get(j).get("A").split("\\.")[0];
                    } else {
                        date = list.get(j).get("A").split("\\.")[0];
                    }

                    // 2월
                    if(Integer.parseInt(checkYear) % 4 == 0) {
                        if(!date.equals("30") && !date.equals("31")) {
                            feb.setStationName(stationName);
                            feb.setYmd(checkYear + "-02-" + date);
                            feb.setLevel(list.get(j).get("C") != "" ? Double.parseDouble(list.get(j).get("C")) : Double.parseDouble("0.0"));
                            result.add(feb);
                        }
                    } else {
                        if(!date.equals("29") && !date.equals("30") && !date.equals("31")) {
                            feb.setStationName(stationName);
                            feb.setYmd(checkYear + "-02-" + date);
                            feb.setLevel(list.get(j).get("C") != "" ? Double.parseDouble(list.get(j).get("C")) : Double.parseDouble("0.0"));
                            result.add(feb);
                        }
                    }

                    // 4월, 6월, 9월, 11월
                    if(!date.equals("31")) {

                        apr.setStationName(stationName);
                        jun.setStationName(stationName);
                        sep.setStationName(stationName);
                        nov.setStationName(stationName);

                        apr.setYmd(checkYear + "-04-" + date);
                        jun.setYmd(checkYear + "-06-" + date);
                        sep.setYmd(checkYear + "-09-" + date);
                        nov.setYmd(checkYear + "-11-" + date);

                        apr.setLevel(list.get(j).get("E") != "" ? Double.parseDouble(list.get(j).get("E")) : Double.parseDouble("0.0"));
                        jun.setLevel(list.get(j).get("G") != "" ? Double.parseDouble(list.get(j).get("G")) : Double.parseDouble("0.0"));
                        sep.setLevel(list.get(j).get("J") != "" ? Double.parseDouble(list.get(j).get("J")) : Double.parseDouble("0.0"));
                        nov.setLevel(list.get(j).get("L") != "" ? Double.parseDouble(list.get(j).get("L")) : Double.parseDouble("0.0"));

                        result.add(apr);
                        result.add(jun);
                        result.add(sep);
                        result.add(nov);
                    }

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

                    jan.setLevel(list.get(j).get("B") != "" ? Double.parseDouble(list.get(j).get("B")) : Double.parseDouble("0.0"));
                    mar.setLevel(list.get(j).get("D") != "" ? Double.parseDouble(list.get(j).get("D")) : Double.parseDouble("0.0"));
                    may.setLevel(list.get(j).get("F") != "" ? Double.parseDouble(list.get(j).get("F")) : Double.parseDouble("0.0"));
                    jul.setLevel(list.get(j).get("H") != "" ? Double.parseDouble(list.get(j).get("H")) : Double.parseDouble("0.0"));
                    aug.setLevel(list.get(j).get("I") != "" ? Double.parseDouble(list.get(j).get("I")) : Double.parseDouble("0.0"));
                    oct.setLevel(list.get(j).get("K") != "" ? Double.parseDouble(list.get(j).get("K")) : Double.parseDouble("0.0"));
                    dec.setLevel(list.get(j).get("M") != "" ? Double.parseDouble(list.get(j).get("M")) : Double.parseDouble("0.0"));

                    result.add(jan);
                    result.add(mar);
                    result.add(may);
                    result.add(jul);
                    result.add(aug);
                    result.add(oct);
                    result.add(dec);

//                    if(nullCheck) {
//                        waterMapper.insertWaterLevel(result);
//                    }

//                    System.out.println(result);

                    // 31일되면 다음 연도로
                    if (list.get(j).get("A").equals("31.00")) {
                        checkYearIndex += 1;
                        if (checkYearIndex < year.size()) {
                            checkYear = year.get(checkYearIndex);
//                            System.out.println("checkYear = " + checkYear);
                        }
                    }
                }
            }
        }
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
        String altitude = list.get(2).get("A").replaceAll("\\s","").split(":")[2].replace("Catchmentarea","");
        String catchmentArea = list.get(2).get("A").replaceAll("\\s","").split(":")[3];
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
        String altitude = list.get(2).get("A").replaceAll("\\s","").split(":")[2].replace("Catchmentarea","");
        String catchmentArea = list.get(2).get("A").replaceAll("\\s","").split(":")[3];
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

            if(i == check) {
                i += 11;
                check = i + 31;
            }

            if(i > list.size()) {
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

            waterMapper.insertWaterLevel(result);

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
        String altitude = list.get(2).get("A").replaceAll("\\s","").split(":")[2].replace("Catchmentarea","");
        String catchmentArea = list.get(2).get("A").replaceAll("\\s","").split(":")[3];
        String province = list.get(3).get("A").split(":")[1].replace("Country","").trim();
        String country = list.get(3).get("A").split(":")[2].split("\\s")[1] + " " + list.get(3).get("A").split(":")[2].split("\\s")[2];
        String agency = list.get(3).get("A").split(":")[3].trim();
        String lon = list.get(1).get("A").split(":")[3].replace("East", "").trim();
        String lat = list.get(1).get("A").split(":")[2].replace("Longitude", "").replace("North", "").trim();

        List<Map<String, String>> list2 = new ArrayList<>();

        // 수위 구하기
        for (int i = 0; i < list.size(); i++) {
            if(!list.get(i).get("A").equals("")) {
                list2.add(list.get(i));
            }
        }

        int check = 39;
        for(int i=8; i < list2.size(); i++) {
            if(i == check) {
                i += 11;
                check = i + 31;
            }

            if(i > list2.size()) {
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

            waterMapper.insertWaterLevel(result);

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
        list = ExcelRead.read2(excelReadOption, workbook, 0);

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
        String altitude = list.get(2).get("A").replaceAll("\\s","").split(":")[2].replace("Catchmentarea","");
        String catchmentArea = list.get(2).get("A").replaceAll("\\s","").split(":")[3];
        String province = list.get(3).get("A").split(":")[1].split("\\s")[1];
        String country = list.get(3).get("A").split(":")[2].split("\\s")[1] + " " + list.get(3).get("A").split(":")[2].split("\\s")[2];
        String agency = list.get(3).get("A").split(":")[3].trim();
        String lon = list.get(1).get("A").split(":")[3].replace("East", "").trim();
        String lat = list.get(1).get("A").split(":")[2].replace("Longitude", "").replace("North", "").trim();

        List<Map<String, String>> list2 = ExcelRead.onlyDoubleRead(excelReadOption, workbook, 0);

        // 수위 구하기
        for (int i=0; i < list2.size(); i++) {
            if(!list2.get(i).get("A").equals("")) {
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

                waterMapper.insertWaterLevel(result);

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

}
