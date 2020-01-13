package com.olek.nbt.tags;

public class TagShort extends Tag {
    private short payload;

    public TagShort(String name, short payload) {
        this.name = name;
        this.payload = payload;
    }
}
