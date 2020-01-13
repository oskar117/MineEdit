package com.olek.nbt.tags;

public class TagString extends Tag {
    private String payload;

    public TagString(String name, String payload) {
        this.name = name;
        this.payload = payload;
    }
}