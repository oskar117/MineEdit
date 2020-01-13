package com.olek.nbt.tags;

import java.util.List;

public class TagIntArray extends Tag {
    private List<TagInt> payload;
    private byte payloadType;
    private int length;

    public TagIntArray(String name, byte type, int length) {
        this.name = name;
        this.payloadType = type;
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(byte payloadType) {
        this.payloadType = payloadType;
    }

    public void addTag(TagInt e) {
        payload.add(e);
    }

    public List<TagInt> getPayload() {
        return payload;
    }

    public void setPayload(List<TagInt> payload) {
        this.payload = payload;
    }
}