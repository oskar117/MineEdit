package com.olek.nbt.tags;

public class TagString extends Tag {
    private String payload;

    public TagString(String name, String payload) {
        super(name);
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}