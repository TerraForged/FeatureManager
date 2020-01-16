package com.terraforged.feature.template.type;

import com.terraforged.feature.template.placement.Placement;

public class FeatureType {

    private final String name;
    private final Placement placement;

    public FeatureType(String name, Placement placement) {
        this.name = name;
        this.placement = placement;
    }

    public String getName() {
        return name;
    }

    public Placement getPlacement() {
        return placement;
    }

    @Override
    public String toString() {
        return name;
    }
}
