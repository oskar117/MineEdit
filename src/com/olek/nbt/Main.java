package com.olek.nbt;

import java.io.*;
import java.nio.file.Files;
import java.util.zip.GZIPInputStream;

public class Main {

    public static void main(String[] args) throws IOException {

        FileUtils fileUtils = new FileUtils();
        FileOutputStream out = new FileOutputStream("/home/olek/Projects/nbtparser/level_de.dat");
        out = fileUtils.decompress(out, new FileInputStream("/home/olek/Projects/nbtparser/level.dat"));

        File file = new File("/home/olek/Projects/nbtparser/level_de.dat");
        byte[] fileContent = Files.readAllBytes(file.toPath());

      //  System.out.println(Character.toString(68)+Character.toString(97)+Character.toString(116)+Character.toString(97));

//        for(int y = 0; y < 100; y++) {
//            System.out.println(fileContent[y]);
//        }

        int pointer = 3;
        byte type = fileContent[pointer+=2];
        int length = fileContent[pointer++];
        for(int x = 0; x < length; x++) {
            System.out.print(Character.toString(fileContent[x+pointer]));
        }
        pointer =+ length+1;
    }
}
