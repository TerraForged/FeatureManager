package com.terraforged.fm.transformer;

import net.minecraft.world.gen.feature.ConfiguredFeature;

public class FeatureInjector {

    private final InjectionPosition position;
    private final ConfiguredFeature<?, ?> feature;

    public FeatureInjector(ConfiguredFeature<?, ?> feature, InjectionPosition position) {
        this.feature = feature;
        this.position = position;
    }

    public ConfiguredFeature<?, ?> getFeature() {
        return feature;
    }

    public InjectionPosition getPosition() {
        return position;
    }
}
