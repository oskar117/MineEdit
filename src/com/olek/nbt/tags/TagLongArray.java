package com.olek.nbt.tags;

import java.util.ArrayList;
import java.util.List;

public class TagLongArray extends Tag {
    private List<TagLong> payload;
    private int length;

    public TagLongArray(String name, int length) {
        super(name);
        this.length = length;
        this.payload = new ArrayList<>();
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
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