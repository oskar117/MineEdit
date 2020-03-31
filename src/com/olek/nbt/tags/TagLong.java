package com.olek.nbt.tags;

public class TagLong extends Tag {
    private Long payload;

    public TagLong(String name, Long payload) {
        this.name = name;
        this.payload = payload;
    }

    public Long getPayload() {
        return payload;
    }

    public void setPayload(Long payload) {
        this.payload = payload;
    }
}
