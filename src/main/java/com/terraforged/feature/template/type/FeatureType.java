package com.terraforged.feature.template.type;

import com.terraforged.feature.matcher.feature.FeatureMatcher;
import com.terraforged.feature.template.decorator.DecoratorFactory;
import com.terraforged.feature.template.placement.Placement;
import net.minecraft.world.gen.feature.Feature;

public class FeatureType {

    private final String name;
    private final Placement placement;
    private final DecoratorFactory factory;

    public FeatureType(String name, Placement placement, DecoratorFactory factory) {
        this.name = name;
        this.placement = placement;
        this.factory = factory;
    }

    public String getName() {
        return name;
    }

    public Placement getPlacement() {
        return placement;
    }

    public DecoratorFactory getFactory() {
        return factory;
    }

    @Override
    public String toString() {
        return name;
    }

    public FeatureMatcher matcher() {
        FeatureMatcher.Builder builder = FeatureMatcher.builder();
        for (Feature<?> feature : FeatureTypes.getFeatures(this)) {
            builder.or(feature);
        }
        return builder.build();
    }
}
