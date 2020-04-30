package com.olek.nbt.tags;

import java.util.ArrayList;
import java.util.List;

public class TagCompound extends Tag {

    private List<Tag> data;

    public TagCompound(String name) {
        super(name);
        this.data = new ArrayList<>();
    }

    public void addTag(Tag tag) {
        data.add(tag);
    }

    public void deleteLast() {
        data.remove(data.size()-1);
    }

    public Tag getTag(String name) {
        for(Tag tag : data) {
            if(tag.getName().equals(name)) {
                return tag;
            }
        }
        return null;
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

    public Tag getLastTag() {
        return data.get(data.size() - 1);
    }
}
