package com.olek.nbt.tags;

public class TagLong extends Tag {
    private Long payload;

    public TagLong(String name, Long payload) {
        super(name);
        this.payload = payload;
    }

    public Long getPayload() {
        return payload;
    }

    public void setPayload(Long payload) {
        this.payload = payload;
    }
}
