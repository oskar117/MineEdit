package com.olek.world;

import com.olek.nbt.tags.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chunk {

    private TagList section;
    private Heightmap heightMap;
    private Block[][][] blocks;

    private Heightmap tempHeightmap = new Heightmap();

    public Chunk(TagCompound tg) throws EmptyChunkException {
        /*section = (TagList) tg.getTag("Sections");
        List<Tag> tagList = section.getPayload();
        if(tagList.size() == 0) throw new EmptyChunkException();
        blocks = new Block[16*(tagList.size()-1)][16][16];

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

            if(y != -1) {
                if(blockStates != null && palette != null) {
                    generateBlocks(blockStates, palette, y);
                } else {
                    fillWithAir(y);
                }
            } else {
                //System.out.println("y = -1");
            }
        }
        testHeightmap(tg);
        heightMap = new Heightmap(blocks);

        for(int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                System.out.println(tempHeightmap.getMap()[x][y].getName() + " : " + heightMap.getMap()[x][y].getName());
            }
        }*/
        testHeightmap(tg);
        heightMap = tempHeightmap;
    }

    private void testHeightmap(TagCompound tg) throws EmptyChunkException {

        section = (TagList) tg.getTag("Sections");
        List<Tag> tagList = section.getPayload();
        int sectionSize = tagList.size();
        if(sectionSize == 0) throw new EmptyChunkException();

        Tag blockStates = null;
        Tag palette = null;
        int y = -1;

        for (int currentY = sectionSize-1; currentY > 0; currentY--) {
            List<Tag> sectionContent = ((TagCompound)tagList.get(currentY)).getData();

            for(Tag searchThroughSectionData : sectionContent) {
                if(searchThroughSectionData instanceof TagByte) {
                    if(((TagByte) searchThroughSectionData).getPayload() == -1 && searchThroughSectionData.getName().equals("Y")) break;
                    else y = ((TagByte) searchThroughSectionData).getPayload();
                } else if(searchThroughSectionData.getName().equals("BlockStates")) {
                    blockStates = searchThroughSectionData;
                } else if(searchThroughSectionData.getName().equals("Palette")) {
                    palette = searchThroughSectionData;
                }
            }

            if(y != -1) {
                if(blockStates != null && palette != null) {
                    if(!tempHeightmap.isHeightmapComplete())
                        tryToGenerateHeightmap(blockStates, palette, y);
                    else
                        break;
                }
            } else {
                //System.out.println("y = -1");
            }
        }

    }

    private void tryToGenerateHeightmap(Tag blockStates, Tag palette, int sectionY) {
        int[][][] localStates = generateBlockStates(blockStates);
        List<String> localBlocks = generatePalette(palette);
        for(int z = 15; z >= 0; z--) {
            for(int x = 0; x < 16; x++) {
                for(int y = 0; y<16; y++) {
                    String blockName = localBlocks.get(localStates[z][x][y]);
                    if (!blockName.equals("minecraft:air") && blockName != null && tempHeightmap.getMap()[x][y] == null)
                        tempHeightmap.setBlock(x, y, new Block(blockName));
                }
            }
        }
    }

    private void fillWithAir(int sectionY) {
        Block air = new Block("minecraft:air");
        try {
            for(int z = 0; z < 16; z++) {
                for(int y = 0; y < 16; y++) {
                    for(int x = 0; x<16; x++) {
                        blocks[(sectionY*16)+x][y][z] = air;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("fill with air exception");
        }

    }

    public Heightmap getHeightMap() {
        return heightMap;
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
                    returnArray[z][y][x] =  Integer.parseInt(reverseString(binaryNumber), 2);
                    binaryNumber="";
                    ++x;
                }
            }
        }
        return returnArray;
    }

    private String reverseString(String string) {
        StringBuilder input1 = new StringBuilder();
        input1.append(string);
        input1.reverse();
        return String.valueOf(input1);
    }

    public class EmptyChunkException extends Exception {
        public EmptyChunkException() {
            super();
        }
    }
}
