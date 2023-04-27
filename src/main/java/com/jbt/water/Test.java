package com.jbt.water;

import java.io.*;

public class Test {
    public static void main(String[] args) throws IOException {
        String filePath = "C:\\Users\\srmsq\\Desktop\\waterdata\\NN_SCN1.g02.hdf";
        File file = new File(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "euc-kr"));

        String str;
        while((str = reader.readLine()) != null) {
            System.out.println(str);
        }
    }
}
