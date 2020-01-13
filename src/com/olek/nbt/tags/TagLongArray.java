package com.olek.nbt.tags;

import java.util.List;

public class TagLongArray extends Tag {
    private List<TagLong> payload;
    private byte payloadType;
    private int length;

    public TagLongArray(String name, byte type, int length) {
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

    public void addTag(TagLong e) {
        payload.add(e);
    }

    public List<TagLong> getPayload() {
        return payload;
    }

    public void setPayload(List<TagLong> payload) {
        this.payload = payload;
    }
}