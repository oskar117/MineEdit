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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.nio.file.Files;

public class MapFrame extends JPanel {

    private Chunk[][] regionChunks;
    private MapModel model;

    public MapFrame() {
       /* String outp = "D:\\zadanka\\nbtparser\\level_de.dat";
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
        */

        //File region = new File("/home/olek/Projects/nbtparser/r.0.-1.mca");
        File region = new File("D:\\zadanka\\nbtparser\\r.21.37.mca");

        RegionFile regionFile = new RegionFile(region);
        NbtParser parser;

        regionChunks = new Chunk[32][32];
        DataInputStream chunkDataStream;
        for (int x = 0; x < 32; x++) {

            for (int z = 0; z < 32; z++) {

                System.out.println("CHUNK: "+ x + " " + z);

                try {
                    parser = new NbtParser();
                    chunkDataStream = regionFile.getChunkDataInputStream(x, z);

                    if(chunkDataStream != null) {
                        regionChunks[x][z] = new Chunk(parser.decodeTag(chunkDataStream.readAllBytes()));
                    } else {
                        regionChunks[x][z] = null;
                    }

                    //Heightmap map = chunkTest.getHeightMap();
                    //Block[][] mapArr = map.getMap();

                } catch (IOException | Chunk.EmptyChunkException e) {
                    regionChunks[x][z] = null;
                    System.out.println("err: " +e.getMessage());
                }

                System.out.println("CHUNK DONE");

            }

        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        int chunkSize = model.getChunkSize();

        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {

                if(regionChunks[x][y] == null) continue;
                Heightmap map = regionChunks[x][y].getHeightMap();
                Block[][] mapArr = map.getMap();

                for (int xx = 0; xx < 16; xx++) {
                    for (int yy = 0; yy < 16; yy++) {
                        String tile = mapArr[xx][yy].getName().replaceAll("minecraft:", "");
                        g.setColor(getTileColor(tile));
                        int xxx = (x * 16 * chunkSize) + (yy * chunkSize) + model.getOffsetX();
                        int yyy = (y * 16 * chunkSize) + (xx * chunkSize) + model.getOffsetY();
                        g.fillRect(xxx, yyy, chunkSize, chunkSize);
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
                return new Color(131, 150, 58);
            } case "sandstone":{
                return new Color(222,215, 172);
            } case "slime_block": {
                return Color.CYAN;
            } case "oak_leaves": {
                return new Color(79, 111, 32);
            } case "diorite":
            case "polished_diorite": {
                return Color.WHITE;
            }
            default: {
                return Color.GRAY;
            }
        }
    }

    public void setModel(MapModel model) {
        this.model = model;
    }
}
