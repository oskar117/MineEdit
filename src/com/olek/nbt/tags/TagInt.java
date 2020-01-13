package com.olek.nbt.tags;

public class TagInt extends Tag {
    private int payload;

    public TagInt(String name, int payload) {
        this.name = name;
        this.payload = payload;
    }
}
