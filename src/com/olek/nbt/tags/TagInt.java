package com.olek.nbt.tags;

public class TagInt extends Tag {

    private int payload;

    public TagInt(String name, int payload) {
        super(name);
        this.payload = payload;
    }

    public int getPayload() {
        return payload;
    }

    public void setPayload(int payload) {
        this.payload = payload;
    }
}
