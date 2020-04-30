package com.olek.nbt.tags;

import java.util.ArrayList;
import java.util.List;

public class TagByteArray extends Tag {

    private List<TagByte> payload;
    private int length;

    public TagByteArray(String name, int length) {
        super(name);
        this.length = length;
        this.payload = new ArrayList<>();
    }

    public int getLength() {
        return length;
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