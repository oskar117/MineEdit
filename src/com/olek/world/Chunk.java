package com.olek.world;

import com.olek.nbt.tags.*;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

    private final TagCompound tg;
    private TagList section;
    private final Heightmap heightMap;
    private Block[][][] blocks;

    public Chunk(TagCompound tg) {
        heightMap = new Heightmap();
        this.tg = tg;
        blocks = new Block[256][16][16];
        section = (TagList) tg.getTag("Sections");
        List<Tag> tagList = section.getPayload();

        //przeskakujemy po sekcjach
        for(Tag t : tagList) {
            TagCompound x = (TagCompound)t;
            List<Tag> nestedList = x.getData();
            
            Tag blockStates = null;
            Tag palette = null;
            int y = -1;

            //zawartość compoundów w sekcji
            for(Tag searchThroughSectionData : nestedList) {
                if(searchThroughSectionData instanceof TagByte) {
                    if(((TagByte) searchThroughSectionData).getPayload() == -1 && searchThroughSectionData.getName().equals("Y")) break;
                    else y = ((TagByte) searchThroughSectionData).getPayload();
                } else if(searchThroughSectionData.getName().equals("BlockStates")) {
                    blockStates = searchThroughSectionData;
                } else if(searchThroughSectionData.getName().equals("Palette")) {
                    palette = searchThroughSectionData;
                }
            }
            
            if(blockStates != null && palette != null && y != -1) {
                generateBlocks(blockStates, palette, y);
            } else {
                System.out.println("error");
            }
        }
    }

    private void generateBlocks(Tag blockStates, Tag palette, int sectionY) {
         int[][][] localStates = generateBlockStates(blockStates);
         List<String> localBlocks = generatePalette(palette);

         for(int z = 0; z < 16; z++) {
             for(int y = 0; y < 16; y++) {
                 for(int x = 0; x<16; x++) {
                     blocks[(sectionY*16)+x][y][z] = new Block(localBlocks.get(localStates[x][y][z]));
                 }
             }
         }

    }

    private List<String> generatePalette(Tag palette) {

        List<String> blockList = new ArrayList<String>();

        if(palette instanceof TagList) {
            List<Tag> payload = ((TagList) palette).getPayload();

            for(Tag tag : payload) {
                if(tag instanceof TagCompound) {
                    List<Tag> compoundContent = ((TagCompound) tag).getData();
                    for(Tag nestedTag : compoundContent) {
                        if(nestedTag instanceof TagString && nestedTag.getName().equals("Name")) {
                            blockList.add(((TagString) nestedTag).getPayload());
                        }
                    }
                }
            }
        }

        return blockList;
    }

    private int[][][] generateBlockStates(Tag blockStates) {

        int[][][] returnArray = new int[16][16][16];

        if(blockStates instanceof TagLongArray) {

            List<TagLong> payload = ((TagLongArray) blockStates).getPayload();
            Long[] tempLongArray = new Long[payload.size()];

            for(int x = 0; x < payload.size(); x++) {
                tempLongArray[x] = payload.get(x).getPayload();
            }

            String byteString = "";

            for(Long x : tempLongArray) {
                String temp = Long.toBinaryString(x);
                while(temp.length() < 64) {
                    temp = "0"+temp;
                }
                temp = reverseString(temp);
                byteString += temp;
            }

            int bitsPerTile = (tempLongArray.length*64)/4096;
            String binaryNumber = "";
            int x = 0;
            int y = 0;
            int z = 0;

            for (int i = 1; i <= byteString.length(); i++){
                binaryNumber += byteString.charAt(i-1);
                if(y >= 16) {
                    ++z;
                    y = 0;
                }
                if(x >= 16) {
                    x = 0;
                    ++y;
                }
                if(i%bitsPerTile == 0){
                    returnArray[x][z][y] =  Integer.parseInt(reverseString(binaryNumber), 2);
                    binaryNumber="";
                    ++x;
                }
            }
        }
        return returnArray;
    }

    private void renderHeightMap(TagCompound tg) {

    }

    private String reverseString(String string) {
        StringBuilder input1 = new StringBuilder();
        input1.append(string);
        input1.reverse();
        return String.valueOf(input1);
    }
}