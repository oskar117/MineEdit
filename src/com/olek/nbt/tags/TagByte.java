package com.olek.nbt.tags;

public class TagByte extends Tag {
    private Byte payload;

    public TagByte(String name, byte payload) {
        this.name = name;
        this.payload = payload;
    }

    public Byte getPayload() {
        return payload;
    }

    public void setPayload(Byte payload) {
        this.payload = payload;
    }
}
