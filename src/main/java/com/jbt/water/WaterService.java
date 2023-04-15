package com.jbt.water;

import com.jbt.water.util.ExcelRead;
import com.jbt.water.util.ExcelReadOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaterService {

    private final WaterMapper waterMapper;

    public void insertWaterLevelByPakkangoung() throws IOException {
        String filePath = "C:\\Users\\W\\Desktop";
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

            jan.setLat(lat);
            feb.setLat(lat);
            mar.setLat(lat);
            apr.setLat(lat);
            may.setLat(lat);
            jun.setLat(lat);
            jul.setLat(lat);
            aug.setLat(lat);
            sep.setLat(lat);
            oct.setLat(lat);
            nov.setLat(lat);
            dec.setLat(lat);

            jan.setLon(lon);
            feb.setLon(lon);
            mar.setLon(lon);
            apr.setLon(lon);
            may.setLon(lon);
            jun.setLon(lon);
            jul.setLon(lon);
            aug.setLon(lon);
            sep.setLon(lon);
            oct.setLon(lon);
            nov.setLon(lon);
            dec.setLon(lon);

            jan.setProvince(province);
            feb.setProvince(province);
            mar.setProvince(province);
            apr.setProvince(province);
            may.setProvince(province);
            jun.setProvince(province);
            jul.setProvince(province);
            aug.setProvince(province);
            sep.setProvince(province);
            oct.setProvince(province);
            nov.setProvince(province);
            dec.setProvince(province);

            jan.setCountry(country);
            feb.setCountry(country);
            mar.setCountry(country);
            apr.setCountry(country);
            may.setCountry(country);
            jun.setCountry(country);
            jul.setCountry(country);
            aug.setCountry(country);
            sep.setCountry(country);
            oct.setCountry(country);
            nov.setCountry(country);
            dec.setCountry(country);

            jan.setAgency(agency);
            feb.setAgency(agency);
            mar.setAgency(agency);
            apr.setAgency(agency);
            may.setAgency(agency);
            jun.setAgency(agency);
            jul.setAgency(agency);
            aug.setAgency(agency);
            sep.setAgency(agency);
            oct.setAgency(agency);
            nov.setAgency(agency);
            dec.setAgency(agency);

            jan.setRiver(river);
            feb.setRiver(river);
            mar.setRiver(river);
            apr.setRiver(river);
            may.setRiver(river);
            jun.setRiver(river);
            jul.setRiver(river);
            aug.setRiver(river);
            sep.setRiver(river);
            oct.setRiver(river);
            nov.setRiver(river);
            dec.setRiver(river);

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

    public void insertWaterLevelByPhiengluang() throws IOException {
        String filePath = "C:\\Users\\W\\Desktop";
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
        String province = list.get(3).get("A").split(":")[1].split("\\s")[1];
        String country = list.get(3).get("A").split(":")[2].split("\\s")[1] + " " + list.get(3).get("A").split(":")[2].split("\\s")[2];
        String agency = list.get(3).get("A").split(":")[3].trim();
        String lon = list.get(1).get("A").split(":")[3].replace("East", "").trim();
        String lat = list.get(1).get("A").split(":")[2].replace("Longitude", "").replace("North", "").trim();

        System.out.println("stationName = " + stationName);
        System.out.println("river = " + river);
        System.out.println("province = " + province);
        System.out.println("country = " + country);
        System.out.println("agency = " + agency);
        System.out.println("lon = " + lon);
        System.out.println("lat = " + lat);

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

            jan.setLat(lat);
            feb.setLat(lat);
            mar.setLat(lat);
            apr.setLat(lat);
            may.setLat(lat);
            jun.setLat(lat);
            jul.setLat(lat);
            aug.setLat(lat);
            sep.setLat(lat);
            oct.setLat(lat);
            nov.setLat(lat);
            dec.setLat(lat);

            jan.setLon(lon);
            feb.setLon(lon);
            mar.setLon(lon);
            apr.setLon(lon);
            may.setLon(lon);
            jun.setLon(lon);
            jul.setLon(lon);
            aug.setLon(lon);
            sep.setLon(lon);
            oct.setLon(lon);
            nov.setLon(lon);
            dec.setLon(lon);

            jan.setProvince(province);
            feb.setProvince(province);
            mar.setProvince(province);
            apr.setProvince(province);
            may.setProvince(province);
            jun.setProvince(province);
            jul.setProvince(province);
            aug.setProvince(province);
            sep.setProvince(province);
            oct.setProvince(province);
            nov.setProvince(province);
            dec.setProvince(province);

            jan.setCountry(country);
            feb.setCountry(country);
            mar.setCountry(country);
            apr.setCountry(country);
            may.setCountry(country);
            jun.setCountry(country);
            jul.setCountry(country);
            aug.setCountry(country);
            sep.setCountry(country);
            oct.setCountry(country);
            nov.setCountry(country);
            dec.setCountry(country);

            jan.setAgency(agency);
            feb.setAgency(agency);
            mar.setAgency(agency);
            apr.setAgency(agency);
            may.setAgency(agency);
            jun.setAgency(agency);
            jul.setAgency(agency);
            aug.setAgency(agency);
            sep.setAgency(agency);
            oct.setAgency(agency);
            nov.setAgency(agency);
            dec.setAgency(agency);

            jan.setRiver(river);
            feb.setRiver(river);
            mar.setRiver(river);
            apr.setRiver(river);
            may.setRiver(river);
            jun.setRiver(river);
            jul.setRiver(river);
            aug.setRiver(river);
            sep.setRiver(river);
            oct.setRiver(river);
            nov.setRiver(river);
            dec.setRiver(river);

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
//                    System.out.println(checkYear);
                }
            }

        }


        workbook.close();

    }

    public void insertWaterLevelByVernkham() throws IOException {
        String filePath = "C:\\Users\\W\\Desktop";
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
                year.add(list.get(i).get("A").split("\\s")[4].trim());
            }
        }

        String checkYear = year.get(0);
        int checkYearIndex = 0;
        String date = "";

        String stationName = list.get(1).get("A").split(":")[1].replace("Latitude", "").trim();
        String river = list.get(2).get("A").split(":")[1].split("\\s")[1] + " " + list.get(2).get("A").split(":")[1].split("\\s")[2];
        String province = list.get(3).get("A").split(":")[1].split("\\s")[1];
        String country = list.get(3).get("A").split(":")[2].split("\\s")[1] + " " + list.get(3).get("A").split(":")[2].split("\\s")[2];
        String agency = list.get(3).get("A").split(":")[3].trim();
        String lon = list.get(1).get("A").split(":")[3].replace("East", "").trim();
        String lat = list.get(1).get("A").split(":")[2].replace("Longitude", "").replace("North", "").trim();

        System.out.println("stationName = " + stationName);
        System.out.println("river = " + river);
        System.out.println("province = " + province);
        System.out.println("country = " + country);
        System.out.println("agency = " + agency);
        System.out.println("lon = " + lon);
        System.out.println("lat = " + lat);

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

            jan.setLat(lat);
            feb.setLat(lat);
            mar.setLat(lat);
            apr.setLat(lat);
            may.setLat(lat);
            jun.setLat(lat);
            jul.setLat(lat);
            aug.setLat(lat);
            sep.setLat(lat);
            oct.setLat(lat);
            nov.setLat(lat);
            dec.setLat(lat);

            jan.setLon(lon);
            feb.setLon(lon);
            mar.setLon(lon);
            apr.setLon(lon);
            may.setLon(lon);
            jun.setLon(lon);
            jul.setLon(lon);
            aug.setLon(lon);
            sep.setLon(lon);
            oct.setLon(lon);
            nov.setLon(lon);
            dec.setLon(lon);

            jan.setProvince(province);
            feb.setProvince(province);
            mar.setProvince(province);
            apr.setProvince(province);
            may.setProvince(province);
            jun.setProvince(province);
            jul.setProvince(province);
            aug.setProvince(province);
            sep.setProvince(province);
            oct.setProvince(province);
            nov.setProvince(province);
            dec.setProvince(province);

            jan.setCountry(country);
            feb.setCountry(country);
            mar.setCountry(country);
            apr.setCountry(country);
            may.setCountry(country);
            jun.setCountry(country);
            jul.setCountry(country);
            aug.setCountry(country);
            sep.setCountry(country);
            oct.setCountry(country);
            nov.setCountry(country);
            dec.setCountry(country);

            jan.setAgency(agency);
            feb.setAgency(agency);
            mar.setAgency(agency);
            apr.setAgency(agency);
            may.setAgency(agency);
            jun.setAgency(agency);
            jul.setAgency(agency);
            aug.setAgency(agency);
            sep.setAgency(agency);
            oct.setAgency(agency);
            nov.setAgency(agency);
            dec.setAgency(agency);

            jan.setRiver(river);
            feb.setRiver(river);
            mar.setRiver(river);
            apr.setRiver(river);
            may.setRiver(river);
            jun.setRiver(river);
            jul.setRiver(river);
            aug.setRiver(river);
            sep.setRiver(river);
            oct.setRiver(river);
            nov.setRiver(river);
            dec.setRiver(river);

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
//                    System.out.println(checkYear);
                }
            }

        }

        workbook.close();

    }
}
