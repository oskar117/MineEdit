package com.olek.nbt;

import com.olek.nbt.tags.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static int pointer = 3;
    static List<Tag> stack;

    public static void main(String[] args) throws IOException {

        String outp = "/home/olek/Projects/nbtparser/level_de.dat";
        String in ="/home/olek/Projects/nbtparser/level.dat";

        FileUtils fileUtils = new FileUtils();
        FileOutputStream out = new FileOutputStream(outp);
        out = fileUtils.decompress(out, new FileInputStream(in));

        File file = new File(outp);
        byte[] fileContent = Files.readAllBytes(file.toPath());


        File region = new File("/home/olek/Projects/nbtparser/r.0.-1.mca");
        RegionFile regionFile = new RegionFile(region);
        DataInputStream test = regionFile.getChunkDataInputStream(0, 11);
        byte[] test2 = test.readAllBytes();

        for(int x = 0; x < test2.length; x++) {
            System.out.println(test2[x]);
        }
        TagCompound decodedTag = decodeTag(fileContent);
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

    public static void getTagPayload(Byte type, byte[] fileContent, String name) {
        switch(type) {
            case 0: {
                System.out.println("(end)");
                if(stack.size() <= 1) {
                    System.out.println("koniec");
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
                System.out.print("(short)");
                byte[] temp = getValue(fileContent, 2);
                ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
                short payload = buffer.put(temp).flip().getShort();
                System.out.println(": "+payload);
                ((TagCompound) stack.get(stack.size() - 1)).addTag(new TagShort(name, payload));
                break;
            }
            case 3: {
                System.out.print("(int)");
                byte[] temp = getValue(fileContent, 4);
                ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
                int payload = buffer.put(temp).flip().getInt();
                System.out.println(": "+payload);
                ((TagCompound) stack.get(stack.size() - 1)).addTag(new TagInt(name, payload));
                break;
            }
            case 4: {
                System.out.print("(long)");
                byte[] temp = getValue(fileContent, 8);
                ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
                Long payload = buffer.put(temp).flip().getLong();
                System.out.println(": "+ payload);
                ((TagCompound) stack.get(stack.size() - 1)).addTag(new TagLong(name, payload));
                break;
            }
            case 5: {
                System.out.print("(float)");
                byte[] temp = getValue(fileContent, 4);
                ByteBuffer buffer = ByteBuffer.wrap(temp);
                float payload = buffer.getFloat();
                System.out.println(": "+ payload);
                ((TagCompound) stack.get(stack.size() - 1)).addTag(new TagFloat(name, payload));
                break;
            }
            case 6: {
                System.out.print("(double)");
                byte[] temp = getValue(fileContent, 8);
                ByteBuffer buffer = ByteBuffer.wrap(temp);
                double payload = buffer.getDouble();
                System.out.println(": "+ payload);
                ((TagCompound) stack.get(stack.size() - 1)).addTag(new TagDouble(name, payload));
                break;
            }
            case 7: {
                System.out.println("(byte array)");
                byte[] temp = getValue(fileContent, 4);
                ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
                int listLength = buffer.put(temp).flip().getInt();

                TagList list = new TagList(name, (byte) 1, listLength);
                for(int x = 0; x < list.getLength(); x++) {
                    getTagPayload((byte)1, fileContent, null);
                }

                ((TagCompound) stack.get(stack.size() - 1)).addTag(list);

                break;
            }
            case 8: {
                System.out.print("(string): ");
                pointer++;
                int stringLength = fileContent[pointer++]+pointer;
                String payload="";
                for(;pointer < stringLength; pointer++) {
                    System.out.print(Character.toString(fileContent[pointer]));
                    payload += Character.toString(fileContent[pointer]);
                }
                System.out.print("\n");
                ((TagCompound) stack.get(stack.size() - 1)).addTag(new TagString(name, payload));
                break;
            }
            case 9: {
                System.out.println("(list)");
                byte listType = fileContent[pointer];
                pointer +=4;
                int listLength = fileContent[pointer++];
                TagList list = new TagList(name, listType, listLength);
                for(int x = 0; x < list.getLength(); x++) {
                    getTagPayload(listType, fileContent, null);
                }
                ((TagCompound) stack.get(stack.size() - 1)).addTag(list);
                break;
            }
            case 10: {
                System.out.println("(compound)");
                stack.add(new TagCompound(name));
                // pointer++;
                break;
            }
            case 11: {
                System.out.println("(int array)");
                byte[] temp = getValue(fileContent, 4);
                ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
                int listLength = buffer.put(temp).flip().getInt();
                TagList list = new TagList(name, (byte) 3, listLength);
                for(int x = 0; x < list.getLength(); x++) {
                    getTagPayload((byte)3, fileContent, null);
                }
                ((TagCompound) stack.get(stack.size() - 1)).addTag(list);
                break;
            }
            case 12: {
                System.out.println("(long array)");
                byte[] temp = getValue(fileContent, 4);
                ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
                int listLength = buffer.put(temp).flip().getInt();
                TagList list = new TagList(name, (byte) 4, listLength);
                for(int x = 0; x < list.getLength(); x++) {
                    getTagPayload((byte)4, fileContent, null);
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
