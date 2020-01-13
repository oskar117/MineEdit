package com.olek.nbt;

import com.olek.nbt.tags.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {

    static int pointer = 3;
    static List<Tag> stack;

    public static void main(String[] args) throws IOException {

        String outp = "D:\\zadanka\\nbtparser\\level_de.dat";
        String in ="D:\\zadanka\\nbtparser\\level.dat";

        //String outp = "/home/olek/Projects/nbtparser/level_de.dat";
        //String in ="/home/olek/Projects/nbtparser/level.dat";

        FileUtils fileUtils = new FileUtils();
        FileOutputStream out = new FileOutputStream(outp);
        out = fileUtils.decompress(out, new FileInputStream(in));

        File file = new File(outp);
        byte[] fileContent = Files.readAllBytes(file.toPath());


        //File region = new File("/home/olek/Projects/nbtparser/r.0.-1.mca");
        File region = new File("D:\\zadanka\\nbtparser\\r.0.-1.mca");

        RegionFile regionFile = new RegionFile(region);
        DataInputStream test = regionFile.getChunkDataInputStream(0, 11);
        byte[] test2 = test.readAllBytes();

        TagCompound decodedTag = decodeTag(test2);
        System.out.print("xd");
    }

    public static String getTagName(byte[] fileContent) {
        pointer += 2;

        int length = fileContent[pointer++];
        String name = "";
        int y = pointer + length;
        for(;pointer < y; pointer++) {
            System.out.print(Character.toString(fileContent[pointer]));
            name += Character.toString(fileContent[pointer]);
        }
        return name;
    }

    public static byte[] getValue(byte[] fileContent, int size) {
        byte[] temp = new byte[size];
        for(int z = 0; z < size; z++) {
            temp[z] = fileContent[pointer++];
        }
        return temp;
    }

    public static TagInt getTagInt(String name, byte[] fileContent) {
        System.out.print("(int)");
        byte[] temp = getValue(fileContent, 4);
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        int payload = buffer.put(temp).flip().getInt();
        System.out.println(": "+payload);
        return new TagInt(name, payload);
    }

    public static TagLong getTagLong(String name, byte[] fileContent) {
        System.out.print("(long)");
        byte[] temp = getValue(fileContent, 8);
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        Long payload = buffer.put(temp).flip().getLong();
        System.out.println(": "+ payload);
        return new TagLong(name, payload);
    }

    public static TagShort getTagShort(String name, byte[] fileContent) {
        System.out.print("(short)");
        byte[] temp = getValue(fileContent, 2);
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
        short payload = buffer.put(temp).flip().getShort();
        System.out.println(": "+payload);
        return new TagShort(name, payload);
    }

    public static int getArrayLength(byte[] fileContent) {
        byte[] temp = getValue(fileContent, 4);
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        return buffer.put(temp).flip().getInt();
    }

    public static TagFloat getTagFloat(String name, byte[] fileContent) {
        System.out.print("(float)");
        byte[] temp = getValue(fileContent, 4);
        ByteBuffer buffer = ByteBuffer.wrap(temp);
        float payload = buffer.getFloat();
        System.out.println(": "+ payload);
        return new TagFloat(name, payload);
    }

    public static TagDouble getTagDouble(String name, byte[] fileContent) {
        System.out.print("(double)");
        byte[] temp = getValue(fileContent, 8);
        ByteBuffer buffer = ByteBuffer.wrap(temp);
        double payload = buffer.getDouble();
        System.out.println(": "+ payload);
        return new TagDouble(name, payload);
    }

    public static TagString getTagString(String name, byte[] fileContent) {
        System.out.print("(string): ");
        pointer++;
        int stringLength = fileContent[pointer++]+pointer;
        String payload="";
        for(;pointer < stringLength; pointer++) {
            System.out.print(Character.toString(fileContent[pointer]));
            payload += Character.toString(fileContent[pointer]);
        }
        System.out.print("\n");
        return new TagString(name, payload);
    }

    private static Tag getTagListValue(byte listType, byte[] fileContent) {
        switch (listType) {
            case 1: {
                return new TagByte(null, fileContent[pointer++]);
            } case 2: {
                return getTagShort(null, fileContent);
            } case 3: {
                return getTagInt(null, fileContent);
            } case 4: {
                return getTagLong(null, fileContent);
            } case 5: {
                return getTagFloat(null, fileContent);
            } case 6: {
                return getTagDouble(null, fileContent);
            } case 8: {
                return getTagString(null, fileContent);
            } case 10: {
                stack.add(new TagCompound(null));
                pointer +=0;
                byte type;
                String name="";
                do {
                    type = fileContent[pointer];
                    if(stack.size() == 0 || stack.get(stack.size() - 1) instanceof TagCompound && type != 0) {
                        name = getTagName(fileContent);
                    }
                    getTagPayload(type, fileContent, name);
                } while(type != 0);

                return new TagCompound("daszke");
            }
            default: {
                return null;
            }
        }
    }

    public static void getTagPayload(Byte type, byte[] fileContent, String name) {
        switch(type) {
            case 0: {
                System.out.println("(end)");
                if(stack.size() <= 1) {
                    pointer++;
                    break;
                }
                Tag lastTag = stack.get(stack.size() - 1);
                stack.remove(lastTag);

                if(stack.get(stack.size() - 1) instanceof TagCompound) {
                    ((TagCompound) stack.get(stack.size() - 1)).addTag(lastTag);
                }
                pointer++;
                break;
            }
            case 1: {
                System.out.println("(byte): "+  fileContent[pointer]);
                ((TagCompound) stack.get(stack.size() - 1)).addTag(new TagByte(name, fileContent[pointer++]));
                break;
            }
            case 2: {
                TagShort tag = getTagShort(name, fileContent);
                ((TagCompound) stack.get(stack.size() - 1)).addTag(tag);
                break;
            }
            case 3: {
                TagInt tag = getTagInt(name, fileContent);
                ((TagCompound) stack.get(stack.size() - 1)).addTag(tag);
                break;
            }
            case 4: {
                TagLong tag = getTagLong(name, fileContent);
                ((TagCompound) stack.get(stack.size() - 1)).addTag(tag);
                break;
            }
            case 5: {
                TagFloat tag = getTagFloat(name, fileContent);
                ((TagCompound) stack.get(stack.size() - 1)).addTag(tag);
                break;
            }
            case 6: {
                TagDouble tag = getTagDouble(name, fileContent);
                ((TagCompound) stack.get(stack.size() - 1)).addTag(tag);
                break;
            }
            case 7: {
                System.out.println("(byte array)");

                TagByteArray list = new TagByteArray(name, getArrayLength(fileContent));

                for(int x = 0; x < list.getLength(); x++) {
                    list.addTag(new TagByte(null, fileContent[pointer++]));
                }

                ((TagCompound) stack.get(stack.size() - 1)).addTag(list);

                break;
            }
            case 8: {
                TagString tag = getTagString(name, fileContent);
                ((TagCompound) stack.get(stack.size() - 1)).addTag(tag);
                break;
            }
            case 9: {
                System.out.println("(list)");
                byte listType = fileContent[pointer];
                pointer +=4;
                int listLength = fileContent[pointer++];

                TagList list = new TagList(name, listType, listLength);
                for(int x = 0; x < list.getLength(); x++) {
                    list.addTag(getTagListValue(listType, fileContent));
                }
                ((TagCompound) stack.get(stack.size() - 1)).addTag(list);
                break;
            }
            case 10: {
                System.out.println("(compound)");
                stack.add(new TagCompound(name));
                break;
            }
            case 11: {
                System.out.println("(int array)");

                TagIntArray list = new TagIntArray(name, getArrayLength(fileContent));

                for(int x = 0; x < list.getLength(); x++) {
                    list.addTag(getTagInt(null, fileContent));
                }

                ((TagCompound) stack.get(stack.size() - 1)).addTag(list);
                break;
            }
            case 12: {
                System.out.println("(long array)");

                TagLongArray list = new TagLongArray(name, getArrayLength(fileContent));

                for(int x = 0; x < list.getLength(); x++) {
                    list.addTag(getTagLong(null, fileContent));
                }

                ((TagCompound) stack.get(stack.size() - 1)).addTag(list);
                break;
            }
            default: {
                System.out.println("(cos innego): "+type);
                break;
            }
        }
    }

    public static TagCompound decodeTag(byte[] fileContent) {

        stack = new ArrayList<Tag>();

        String name="";

        while(pointer < fileContent.length) {
            byte type = fileContent[pointer];
            if(stack.size() == 0 || stack.get(stack.size() - 1) instanceof TagCompound && type != 0) {
                name = getTagName(fileContent);
            }
            getTagPayload(type, fileContent, name);
        }

        return (TagCompound) stack.get(0);
    }
}
