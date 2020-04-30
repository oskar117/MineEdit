package com.olek.nbt.tags;

import java.util.List;

public abstract class Tag {

    protected String name;

    protected Tag(String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
