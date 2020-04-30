package com.olek.nbt.tags;

public class TagFloat extends Tag {

    private float payload;

    public TagFloat(String name, float payload) {
        super(name);
        this.payload = payload;
    }

    public float getPayload() {
        return payload;
    }

    public void setPayload(float payload) {
        this.payload = payload;
    }
}
