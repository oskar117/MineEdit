package com.olek.app;

import com.olek.nbt.FileUtils;
import com.olek.nbt.NbtParser;
import com.olek.nbt.RegionFile;
import com.olek.nbt.tags.TagCompound;
import com.olek.world.Block;
import com.olek.world.Chunk;
import com.olek.world.Heightmap;
import com.olek.world.Region;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.nio.file.Files;

public class MapFrame extends JPanel {

    private Region region;
    private MapModel model;

    public MapFrame() {
        //File regionFile = new File("/home/olek/Projects/nbtparser/r.0.-1.mca");
        File regionFile = new File("D:\\zadanka\\nbtparser\\r.21.37.mca");
        region = new Region(regionFile);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        int chunkSize = model.getChunkSize();
        Chunk[][] regionChunks = region.getRegionChunks();

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
