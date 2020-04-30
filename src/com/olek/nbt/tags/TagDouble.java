package com.olek.nbt.tags;

public class TagDouble extends Tag {

    private double payload;

    public TagDouble(String name, double payload) {
        super(name);
        this.name = name;
    }

    public double getPayload() {
        return payload;
    }

    public void setPayload(double payload) {
        this.payload = payload;
    }
}
