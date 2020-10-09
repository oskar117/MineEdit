package com.olek.world;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class World {

    private Region[][] regions;

    public World(File[] files) {
        mapToArray(Arrays.stream(files).map(Region::new).collect(Collectors.toList()));
    }

    private void mapToArray(List<Region> regions) {
        int sizeX = getWorldSizeOrderedByAxis(Region::getRegionX, regions);
        int sizeY = getWorldSizeOrderedByAxis(Region::getRegionY, regions);
        this.regions = new Region[sizeX][sizeY];
        int highestNegativeX = getHighestNegativeCoordinate(Region::getRegionX, regions);
        int highestNegativeY = getHighestNegativeCoordinate(Region::getRegionY, regions);
        int offsetX = highestNegativeX < 0 ? Math.abs(highestNegativeX) : 0;
        int offsetY = highestNegativeY < 0 ? Math.abs(highestNegativeY) : 0;
        regions.stream().forEach(r -> this.regions[r.getRegionX()+offsetX][r.getRegionY()+offsetY] = r);
    }

    private int getWorldSizeOrderedByAxis(Function<Region, Integer> method, List<Region> regions) {
        return Math.toIntExact(Collections.max(regions.stream().collect(Collectors.groupingBy(method, Collectors.counting())).entrySet(), (a, b) -> a.getValue() > b.getValue() ? 1 : -1).getValue());
    }

    private int getHighestNegativeCoordinate(Function<Region, Integer> method, List<Region> regions) {
        return regions.stream().map(method).mapToInt(v -> v).min().getAsInt();
    }

    public Region[][] getRegions() {
        return regions;
    }
}
