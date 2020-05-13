package com.terraforged.mod.feature.manager.transformer;

import net.minecraft.world.gen.feature.ConfiguredFeature;

public class FeatureInjector {

    private final Type type;
    private final ConfiguredFeature<?, ?> feature;

    public FeatureInjector(ConfiguredFeature<?, ?> feature, Type type) {
        this.feature = feature;
        this.type = type;
    }

    public ConfiguredFeature<?, ?> getFeature() {
        return feature;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        BEFORE,
        AFTER,
    }
}
