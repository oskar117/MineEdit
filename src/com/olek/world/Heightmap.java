package com.olek.world;

import java.io.Serializable;
import java.util.List;

public class Heightmap {

    private Block[][] map;

    public Heightmap(Block[][][] blocks) {

        map = new Block[16][16];

        for(int y = blocks.length-1; y >= 0; y--){
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    try {
                        if(!blocks[y][x][z].getName().equals("minecraft:air") && map[x][z] == null) {
                            map[x][z] = blocks[y][x][z];
                        }
                    } catch (NullPointerException e){
                        System.out.println("null w heightmapie: "+ y +" " + x + " " + z);
                    }

                }
            }
        }
    }

    public Heightmap() {
        map = new Block[16][16];
    }

    public void setBlock(int x, int y, Block block) {
        map[x][y] = block;
    }

    public boolean isHeightmapComplete() {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                if(map[x][y] == null) return false;
            }
        }
        return true;
    }

    public Block[][] getMap() {
        return map;
    }
}
