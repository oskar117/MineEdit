package com.olek.world;

import com.olek.nbt.NbtParser;
import com.olek.nbt.RegionFile;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Region {

    private Chunk[][] regionChunks;

    public Region(File file) {
        regionChunks = new Chunk[32][32];
        RegionFile regionFile = new RegionFile(file);
        parseRegion(regionFile);
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

    public Chunk[][] getRegionChunks() {
        return regionChunks;
    }
}
