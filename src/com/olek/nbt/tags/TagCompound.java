package com.olek.nbt.tags;

import java.util.ArrayList;
import java.util.List;

public class TagCompound extends Tag {

    private List<Tag> data;

    public TagCompound(String name) {
        this.name = name;
        this.data = new ArrayList<Tag>();
    }

    public void addTag(Tag tag) {
        data.add(tag);
    }

    public List<Tag> getData() {
        return data;
    }

    public Tag getData(int x) {
        return data.get(x);
    }

    public void setData(List<Tag> data) {
        this.data = data;
    }
}
