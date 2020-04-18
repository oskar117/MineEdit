package com.olek.app;

public class MapModel {

    private MapModelListener listener;

    private int offsetX;
    private int offsetY;
    private int chunkSize;

    public MapModel() {
        chunkSize = 10;
        offsetX = 0;
        offsetY = 0;
    }

    public void update() {
        listener.modelChanged();
    }

    public void addListener(MapModelListener listener) {
        this.listener = listener;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }
}
