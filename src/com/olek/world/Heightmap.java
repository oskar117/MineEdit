package com.olek.world;

import java.util.List;

public class Heightmap {

    private Block[][] map;

    public Heightmap(Block[][][] blocks) {

        map = new Block[16][16];

        for(int y = 80; y >= 0; y--){
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    if(!blocks[y][x][z].getName().equals("minecraft:air") && map[x][z] == null) {
                        map[x][z] = blocks[y][x][z];
                    }
                }
            }
        }
    }

    public Block[][] getMap() {
        return map;
    }
}
