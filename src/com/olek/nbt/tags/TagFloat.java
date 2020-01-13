package com.olek.nbt.tags;

public class TagFloat extends Tag {
    private float payload;

    public TagFloat(String name, float payload) {
        this.name = name;
        this.payload = payload;
    }
}
