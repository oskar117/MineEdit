package com.olek.nbt.tags;

import java.util.ArrayList;
import java.util.List;

public class TagIntArray extends Tag {
    private List<TagInt> payload;
    private int length;

    public TagIntArray(String name, int length) {
        this.name = name;
        this.length = length;
        this.payload = new ArrayList<TagInt>();
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
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