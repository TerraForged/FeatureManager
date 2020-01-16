package com.terraforged.feature.template.type;

import com.terraforged.feature.template.placement.Placement;
import com.terraforged.feature.template.placement.TreePlacement;

import java.util.HashMap;
import java.util.Map;

public class FeatureTypes {

    private static final Map<String, FeatureType> types = new HashMap<>();

    public static final FeatureType ANY = register("any", Placement.ANY);
    public static final FeatureType TREE = register("tree", TreePlacement.PLACEMENT);

    public static FeatureType getType(String type) {
        return types.getOrDefault(type, ANY);
    }

    private static FeatureType register(String name, Placement placement) {
        return register(new FeatureType(name, placement));
    }

    private static FeatureType register(FeatureType type) {
        types.put(type.getName(), type);
        return type;
    }
}
