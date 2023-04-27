package com.jbt.water;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class StringTest {

    @Test
    void 문자_테스트() throws IOException {
        String s = "asb safd absdf      asdfga        asdbvs";
//        StringTokenizer stringTokenizer = new StringTokenizer(s);
//        while(stringTokenizer.hasMoreTokens()) {
//            System.out.println(stringTokenizer.nextToken());
//        }
        String filePath = "C:\\Users\\srmsq\\Desktop\\facility.hyd";
        File file = new File(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "euc-kr"));
        List<String> list = new ArrayList<>();

        String str;
        int count = 0;
        while ((str = reader.readLine()) != null) {
            list.add(str);
            count++;
//            if(count > 8 && count < 30) {
//                System.out.println(str);
//                if(offset < str.length()) {
//                    if(count == 9) {
//                        System.out.println(StringUtils.truncate(str, offset, firstMaxWidth));
//                    } else {
//                        System.out.println(StringUtils.truncate(str, offset, maxWidth));
//                    }
//                }
        }
//            if(count == 30) {
//                break;
//            }
        int offset = 16;
        int firstMaxWidth = 8;
        int maxWidth = 10;
        List<String> arrayList = new ArrayList<>();
        for(int i=9; i < list.size(); i++) {
            for(int j=0; j < list.get(i).length(); j++) {
                if(offset < list.get(i).length()) {
                    if (j == 0) {
                        arrayList.add(StringUtils.truncate(list.get(i), offset, 9).trim());
                        offset += 9;
                    } else {
                        if (j % 7 == 0) {
                            arrayList.add(StringUtils.truncate(list.get(i), offset, firstMaxWidth).trim());
                            offset += firstMaxWidth;
                        } else {
                            arrayList.add(StringUtils.truncate(list.get(i), offset, maxWidth).trim());
                            offset += maxWidth;
                        }
                    }
                }
            }
            offset = 0;
        }

        System.out.println(arrayList);

    }


    @Test
    void 테스트() throws IOException {
        String filePath = "C:\\Users\\srmsq\\Desktop\\facility.hyd";
        File file = new File(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "euc-kr"));

        String str;
        List<String[]> facilityInfo = new ArrayList<>();
        while ((str = reader.readLine()) != null) {
            facilityInfo.add(StringUtils.splitPreserveAllTokens(str));
        }

        for (String[] info : facilityInfo) {
            System.out.println(Arrays.toString(info));
        }

    }

}