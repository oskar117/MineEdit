package com.olek.nbt.tags;

public abstract class Tag {
    public String name;
    private int length;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
