package com.olek.app;

import com.olek.nbt.FileUtils;
import com.olek.nbt.NbtParser;
import com.olek.nbt.RegionFile;
import com.olek.nbt.tags.*;
import com.olek.world.Chunk;

import javax.swing.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws IOException {

        String outp = "D:\\zadanka\\nbtparser\\level_de.dat";
        String in ="D:\\zadanka\\nbtparser\\level.dat";

        //String outp = "/home/olek/Projects/nbtparser/level_de.dat";
        //String in ="/home/olek/Projects/nbtparser/level.dat";

        FileUtils fileUtils = new FileUtils();
        FileOutputStream out = new FileOutputStream(outp);
        out = fileUtils.decompress(out, new FileInputStream(in));

        File file = new File(outp);
        byte[] fileContent = Files.readAllBytes(file.toPath());


        //File region = new File("/home/olek/Projects/nbtparser/r.0.-1.mca");
        File region = new File("D:\\zadanka\\nbtparser\\r.0.-1.mca");

        RegionFile regionFile = new RegionFile(region);
        DataInputStream test = regionFile.getChunkDataInputStream(0, 11);

        byte[] test2 = test.readAllBytes();

       NbtParser parser = new NbtParser();
       TagCompound decodedTag = parser.decodeTag(test2);

       Chunk chunkTest = new Chunk(decodedTag);
       System.out.print("xd");

        /*SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });*/
    }
}