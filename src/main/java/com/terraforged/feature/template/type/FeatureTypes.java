package com.terraforged.feature.template.type;

import com.terraforged.feature.template.decorator.DecoratorFactory;
import com.terraforged.feature.template.decorator.tree.TreeDecoratorFactory;
import com.terraforged.feature.template.placement.Placement;
import com.terraforged.feature.template.placement.TreePlacement;
import net.minecraft.world.gen.feature.Feature;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FeatureTypes {

    private static final Map<String, FeatureType> types = new HashMap<>();
    private static final Map<FeatureType, Set<Feature<?>>> features = new HashMap<>();

    public static final FeatureType ANY = register("any", Placement.ANY, DecoratorFactory.NONE);
    public static final FeatureType TREE = register("tree", TreePlacement.PLACEMENT, TreeDecoratorFactory.INSTANCE);

    public static FeatureType getType(String type) {
        return types.getOrDefault(type, ANY);
    }

    public static void register(FeatureType type, Feature<?> feature) {
        features.computeIfAbsent(type, t -> new HashSet<>()).add(feature);
    }

    protected static Collection<Feature<?>> getFeatures(FeatureType type) {
        return features.getOrDefault(type, Collections.emptySet());
    }

    private static FeatureType register(String name, Placement placement, DecoratorFactory factory) {
        return register(new FeatureType(name, placement, factory));
    }

    private static FeatureType register(FeatureType type) {
        types.put(type.getName(), type);
        return type;
    }
}
