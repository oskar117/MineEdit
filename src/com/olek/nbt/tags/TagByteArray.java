package com.olek.nbt.tags;

import java.util.List;

public class TagByteArray extends Tag {
    private List<TagByte> payload;
    private byte payloadType;
    private int length;

    public TagByteArray(String name, byte type, int length) {
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

    public void addTag(TagByte e) {
        payload.add(e);
    }

    public List<TagByte> getPayload() {
        return payload;
    }

    public void setPayload(List<TagByte> payload) {
        this.payload = payload;
    }
}