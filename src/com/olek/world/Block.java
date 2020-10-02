package com.olek.world;

import java.io.Serializable;

public class Block {

    private String name;

    //TODO enum
    public Block(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
