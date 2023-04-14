package com.jbt.water;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ReadExcel {
    public List<WaterVO> read() throws IOException {
        String filePath = "\\\\nas\\4_프로젝트\\프로젝트-2023\\3_수행\\X23004P_라오스 남능강\\998_공유폴더\\입수 정리 폴더\\20230410_수위 관측자료 (2022년 포함)\\Hydromet Data\\";
        String fileName = "Pakkangoung_waterlevel.xlsx";

        FileInputStream file = new FileInputStream(new File(filePath, fileName));

        // 엑셀 파일로 Workbook instance를 생성한다.
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        // workbook의 첫번째 sheet를 가저온다.
        XSSFSheet sheet = workbook.getSheetAt(0);

        // 만약 특정 이름의 시트를 찾는다면 workbook.getSheet("찾는 시트의 이름");
        // 만약 모든 시트를 순회하고 싶으면
        // for(Integer sheetNum : workbook.getNumberOfSheets()) {
        //      XSSFSheet sheet = workbook.getSheetAt(i);
        // }

        List<WaterVO> list = new ArrayList<>();
        String year = "";



        // 모든 행(row)들을 조회한다.
        for (Row row : sheet) {

            // 각각의 행에 존재하는 모든 열(cell)을 순회한다.
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                WaterVO waterVO = new WaterVO();

                // cell의 타입을 하고, 값을 가져온다.
                switch (cell.getCellType()) {
                    case NUMERIC:
//                        getNumericCellValue 메서드는 기본으로 double형 반환
                        System.out.print(String.format("%.2f",cell.getNumericCellValue()) + "\t");
                        /*if(String.format("%.2f",cell.getNumericCellValue()).split(".")[0].length() > 4) {
                            System.out.println("연도 " + cell.getNumericCellValue());
                        }*/

//                        if(String.format("%.2f", cell.getNumericCellValue()).length() == 7) {
//                            year = String.format("%.2f", cell.getNumericCellValue()).substring(0,4);
//                        } else {
//                            waterVO.setLevel(Double.parseDouble(String.format("%.2f", cell.getNumericCellValue())));
//                        }

                        break;

                    case STRING:
                        System.out.print(cell.getStringCellValue() + "\t");
                        break;

                    case BLANK:
                        System.out.print("      " + "\t");
                        break;
                }
                if(waterVO.getLevel() != null) {
                    /*if(String.valueOf(waterVO.getLevel()).split(".")[0].length() > 4) {
                        System.out.println("연도 : " + waterVO.getLevel());
                    }*/
                    waterVO.setYmd(year);
                    list.add(waterVO);
                }
            }
            System.out.println();
        }

        return list;
    }
}
