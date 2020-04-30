package com.olek.nbt.tags;

import java.util.ArrayList;
import java.util.List;

public class TagList extends Tag {

    private List<Tag> payload;
    private byte payloadType;
    private int length;

    public TagList(String name, byte type, int length) {
        super(name);
        this.payloadType = type;
        this.length = length;
        this.payload = new ArrayList<>();
    }

    public Tag getTag(String name) {
        for(Tag tag : payload) {
            if(tag.getName().equals(name)) {
                return tag;
            }
        }
        return null;
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

    public void addTag(Tag e) {
        payload.add(e);
    }

    public List<Tag> getPayload() {
        return payload;
    }

    public void setPayload(List<Tag> payload) {
        this.payload = payload;
    }
}
