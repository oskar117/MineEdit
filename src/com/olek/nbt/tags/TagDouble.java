package com.olek.nbt.tags;

public class TagDouble extends Tag {
    private double payload;

    public TagDouble(String name, double payload) {
        this.payload = payload;
        this.name = name;
    }
}
