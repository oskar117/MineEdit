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

    @Override
    protected void paintComponent(Graphics g) {

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
        DataInputStream test = regionFile.getChunkDataInputStream(1, 1);

        byte[] test2 = new byte[0];
        try {
            test2 = test.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NbtParser parser = new NbtParser();
        TagCompound decodedTag = parser.decodeTag(test2);

        Chunk chunkTest = new Chunk(decodedTag);
        Heightmap map = chunkTest.getHeightMap();
        Block[][] mapArr = map.getMap();

        super.paintComponent(g);

        for (int x = 0; x < MainFrame.HEIGHT; x+= 45) {
            for (int y = 0; y < MainFrame.HEIGHT; y+= 45) {
                String test23 = mapArr[x/45][y/45].getName().replaceAll("minecraft:", "");
                switch(test23) {
                    case "redstone_block":{
                        g.setColor(Color.RED);
                        break;
                    }
                    case "grass":
                    case "grass_block":
                    case "tall_grass":
                    case "poppy": {
                        g.setColor(Color.GREEN);
                        break;
                    }
                    case "sandstone":{
                        g.setColor(Color.YELLOW);
                        break;
                    }
                    case "slime_block": {
                        g.setColor(Color.cyan);
                        break;
                    }
                    case "polished_diorite": {
                        g.setColor(Color.white);
                        break;
                    }
                    default: {
                        g.setColor(Color.gray);
                        break;
                    }

                }
                g.fillRect(y, x, 45, 45);
            }
        }
    }
}
