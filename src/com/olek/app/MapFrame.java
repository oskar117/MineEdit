package com.olek.app;

import com.olek.nbt.FileUtils;
import com.olek.nbt.NbtParser;
import com.olek.nbt.RegionFile;
import com.olek.nbt.tags.TagCompound;
import com.olek.world.Block;
import com.olek.world.Chunk;
import com.olek.world.Heightmap;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;

public class MapFrame extends JPanel {

    private Chunk[][] regionChunks;

    public MapFrame() {
        String outp = "D:\\zadanka\\nbtparser\\level_de.dat";
        String in ="D:\\zadanka\\nbtparser\\level.dat";

        //String outp = "/home/olek/Projects/nbtparser/level_de.dat";
        //String in ="/home/olek/Projects/nbtparser/level.dat";

        FileUtils fileUtils = new FileUtils();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            out = fileUtils.decompress(out, new FileInputStream(in));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        File file = new File(outp);
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }


        //File region = new File("/home/olek/Projects/nbtparser/r.0.-1.mca");
        File region = new File("D:\\zadanka\\nbtparser\\r.0.0.mca");

        RegionFile regionFile = new RegionFile(region);
        NbtParser parser;

        regionChunks = new Chunk[32][32];
        DataInputStream chunkDataStream;
        for (int x = 0; x < 32; x++) {

            for (int z = 0; z < 32; z++) {

                try {
                    parser = new NbtParser();
                    chunkDataStream = regionFile.getChunkDataInputStream(x, z);
                    regionChunks[x][z] = new Chunk(parser.decodeTag(chunkDataStream.readAllBytes()));

                    //Heightmap map = chunkTest.getHeightMap();
                    //Block[][] mapArr = map.getMap();

                } catch (IOException e) {
                    System.out.println("err: " +e.getMessage());
                }
            }

        }
    }

    @Override
    protected void paintComponent(Graphics g) {

    /*    DataInputStream test = regionFile.getChunkDataInputStream(1, 1);
        byte[] test2 = new byte[0];

        try {
            test2 = test.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TagCompound decodedTag = parser.decodeTag(test2);

        Chunk chunkTest = new Chunk(decodedTag);
        Heightmap map = chunkTest.getHeightMap();
        Block[][] mapArr = map.getMap();

        super.paintComponent(g);*/
        int chunkSize = 10;

        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {
                Heightmap map = regionChunks[x][y].getHeightMap();
                Block[][] mapArr = map.getMap();

                for (int xx = 0; xx < 16; xx++) {
                    for (int yy = 0; yy < 16; yy++) {
                        String tile = mapArr[xx][yy].getName().replaceAll("minecraft:", "");
                        g.setColor(getTileColor(tile));
                        int yyy = (y*16*chunkSize)+(xx*chunkSize);
                        int zzz = (x*16*chunkSize)+(yy*chunkSize);
                        //System.out.println("Koord yyy: "+yyy + " zzz: "+ zzz);
                        g.fillRect(zzz, yyy, chunkSize, chunkSize);
                    }
                }
            }
        }
        System.out.println("Rendered!");
    }

    private Color getTileColor(String name) {
        switch(name) {
            case "redstone_block":{
                return Color.RED;
            }
            case "grass":
            case "grass_block":
            case "tall_grass":
            case "poppy": {
                return Color.GREEN;
            }
            case "sandstone":{
                return new Color(222,215, 172);
            }
            case "slime_block": {
                return Color.CYAN;
            }
            case "diorite":
            case "polished_diorite": {
                return Color.WHITE;
            }
            default: {
                return Color.GRAY;
            }
        }
    }
}