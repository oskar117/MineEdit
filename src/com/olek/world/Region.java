package com.olek.world;

import com.olek.nbt.NbtParser;
import com.olek.nbt.RegionFile;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Region {

    private int regionX;
    private int regionY;
    private Chunk[][] regionChunks;

    public Region(File file) {
        regionChunks = new Chunk[32][32];
        parseRegion(new RegionFile(file));
        getChunkCoordinates(file.getName());
        System.out.println(regionX + " : " +regionY);
    }

    //TODO code again (that parser xd)
    private void parseRegion(RegionFile regionFile) {

        DataInputStream chunkDataStream;
        NbtParser parser = new NbtParser();

        for (int x = 0; x < 32; x++) {

            for (int z = 0; z < 32; z++) {

                //System.out.println("CHUNK: "+ x + " " + z);

                try {
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
                    System.err.println("err: " +e.getMessage());
                }

                //System.out.println("CHUNK DONE");

            }
        }
    }

    private void getChunkCoordinates(String fileName) {
        final Pattern pattern = Pattern.compile("(-?\\d+)\\D+(-?\\d+)");
        Matcher matcher = pattern.matcher(fileName);
        if(matcher.find()) {
            regionX = Integer.parseInt(matcher.group(1));
            regionY = Integer.parseInt(matcher.group(2));
        }
    }

    public Chunk[][] getRegionChunks() {
        return regionChunks;
    }
}
