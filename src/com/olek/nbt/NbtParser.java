package com.olek.nbt;

import com.olek.nbt.tags.*;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class NbtParser {

    private int pointer;
    private LinkedList<TagCompound> stack;

    //TODO pointer od 0 (do zapisu (ewentualnie))
    public NbtParser() {
        setup();
    }

    public TagCompound decodeTag(byte[] fileContent) {
        String name="";

        while(pointer < fileContent.length) {
            byte type = fileContent[pointer];
            if(stack.size() == 0 || stack.getLast() != null && type != 0) {
                name = getTagName(fileContent);
            }
            getTagPayload(type, fileContent, name);
        }
        TagCompound temp = stack.getFirst();
        setup();
        return temp;
    }

    private void setup() {
        pointer = 3;
        if (stack == null) {
            stack = new LinkedList<>();
        }
        stack.clear();
    }

    private String getTagName(byte[] fileContent) {
        pointer += 2;

        int length = fileContent[pointer++];
        //TODO string builder
        String name = "";
        int y = pointer + length;
        for(;pointer < y; pointer++) {
            name += Character.toString(fileContent[pointer]);
        }
        return name;
    }

    private byte[] getValue(byte[] fileContent, int size) {
        byte[] temp = new byte[size];
        for(int z = 0; z < size; z++) {
            temp[z] = fileContent[pointer++];
        }
        return temp;
    }

    //TODO getCostam jako konstruktory tagów

    private TagInt getTagInt(String name, byte[] fileContent) {
        byte[] temp = getValue(fileContent, 4);
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        int payload = buffer.put(temp).flip().getInt();
        return new TagInt(name, payload);
    }

    private TagLong getTagLong(String name, byte[] fileContent) {
        byte[] temp = getValue(fileContent, 8);
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        Long payload = buffer.put(temp).flip().getLong();
        return new TagLong(name, payload);
    }

    private TagShort getTagShort(String name, byte[] fileContent) {
        byte[] temp = getValue(fileContent, 2);
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
        short payload = buffer.put(temp).flip().getShort();
        return new TagShort(name, payload);
    }

    private int getArrayLength(byte[] fileContent) {
        byte[] temp = getValue(fileContent, 4);
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        return buffer.put(temp).flip().getInt();
    }

    private TagFloat getTagFloat(String name, byte[] fileContent) {
        byte[] temp = getValue(fileContent, 4);
        ByteBuffer buffer = ByteBuffer.wrap(temp);
        float payload = buffer.getFloat();
        return new TagFloat(name, payload);
    }

    private TagDouble getTagDouble(String name, byte[] fileContent) {
        byte[] temp = getValue(fileContent, 8);
        ByteBuffer buffer = ByteBuffer.wrap(temp);
        double payload = buffer.getDouble();
        return new TagDouble(name, payload);
    }

    private TagString getTagString(String name, byte[] fileContent) {
        pointer++;
        int stringLength = fileContent[pointer++]+pointer;
        String payload="";
        for(;pointer < stringLength; pointer++) {
            payload += Character.toString(fileContent[pointer]);
        }
        return new TagString(name, payload);
    }

    private TagIntArray getTagIntArray(String name, byte[] fileContent) {
        TagIntArray list = new TagIntArray(name, getArrayLength(fileContent));
        for(int x = 0; x < list.getLength(); x++) {
            list.addTag(getTagInt(null, fileContent));
        }

        return list;
    }

    private TagList getTagList(String name, byte[] fileContent) {
        byte listType = fileContent[pointer];
        pointer +=4;
        int listLength = fileContent[pointer++];

        TagList list = new TagList(name, listType, listLength);
        for(int x = 0; x < list.getLength(); x++) {
            list.addTag(getTagListValue(listType, fileContent));
        }
        return list;
    }

    private TagLongArray getTagLongArray(String name, byte[] fileContent) {
        TagLongArray list = new TagLongArray(name, getArrayLength(fileContent));

        for(int x = 0; x < list.getLength(); x++) {
            list.addTag(getTagLong(null, fileContent));
        }

        return list;
    }

    private TagByteArray getTagByteArray(String name, byte[] fileContent) {
        TagByteArray list = new TagByteArray(name, getArrayLength(fileContent));

        for(int x = 0; x < list.getLength(); x++) {
            list.addTag(new TagByte(null, fileContent[pointer++]));
        }
        return list;
    }

    private Tag getTagListValue(byte listType, byte[] fileContent) {
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
            } case 7: {
                return getTagByteArray(null, fileContent);
            } case 8: {
                return getTagString(null, fileContent);
            } case 9: {
                return getTagList(null, fileContent);
            } case 10: {
                int counter = stack.size();
                stack.add(new TagCompound(null));
                pointer +=0;
                byte type;
                String name="";
                do {
                    type = fileContent[pointer];
                    if(stack.size() == 0 || stack.getLast() != null && type != 0) {
                        name = getTagName(fileContent);
                    }
                    getTagPayload(type, fileContent, name);
                } while(type != 0 || (stack.size() != (counter)));//do dopracowania

                Tag temp = stack.getLast().getLastTag();
                stack.getLast().deleteLast();
                return temp;
            } case 11: {
                return getTagIntArray(null, fileContent);
            } case 12: {
                return getTagLongArray(null, fileContent);
            } default: {
                return null;
            }
        }
    }

    private void getTagPayload(Byte type, byte[] fileContent, String name) {
        switch(type) {
            case 0: {
                if(stack.size() <= 1) {
                    pointer++;
                    break;
                }
                Tag lastTag = stack.getLast();
                stack.remove(lastTag);

                if(stack.getLast() != null) {
                    stack.getLast().addTag(lastTag);
                }
                pointer++;
                break;
            }
            case 1: {
                stack.getLast().addTag(new TagByte(name, fileContent[pointer++]));
                break;
            }
            case 2: {
                TagShort tag = getTagShort(name, fileContent);
                stack.getLast().addTag(tag);
                break;
            }
            case 3: {
                TagInt tag = getTagInt(name, fileContent);
                stack.getLast().addTag(tag);
                break;
            }
            case 4: {
                TagLong tag = getTagLong(name, fileContent);
                stack.getLast().addTag(tag);
                break;
            }
            case 5: {
                TagFloat tag = getTagFloat(name, fileContent);
                stack.getLast().addTag(tag);
                break;
            }
            case 6: {
                TagDouble tag = getTagDouble(name, fileContent);
                stack.getLast().addTag(tag);
                break;
            }
            case 7: {
                TagByteArray list = getTagByteArray(name, fileContent);
                stack.getLast().addTag(list);
                break;
            }
            case 8: {
                TagString tag = getTagString(name, fileContent);
                stack.getLast().addTag(tag);
                break;
            }
            case 9: {
                TagList list = getTagList(name, fileContent);
                stack.getLast().addTag(list);
                break;
            }
            case 10: {
                stack.add(new TagCompound(name));
                break;
            }
            case 11: {
                TagIntArray list = getTagIntArray(name, fileContent);
                stack.getLast().addTag(list);
                break;
            }
            case 12: {
                TagLongArray list = getTagLongArray(name, fileContent);
                stack.getLast().addTag(list);
                break;
            }
            default: {
                System.out.println("(cos innego): " + type);
                break;
            }
        }
    }
}
