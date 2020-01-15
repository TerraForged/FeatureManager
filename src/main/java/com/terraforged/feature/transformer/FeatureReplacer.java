package com.terraforged.feature.transformer;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.function.Supplier;

public class FeatureReplacer implements Supplier<ConfiguredFeature<?, ?>> {

    private final ConfiguredFeature<?, ?> feature;

    public FeatureReplacer(ConfiguredFeature<?, ?> feature) {
        this.feature = feature;
    }

    @Override
    public ConfiguredFeature<?, ?> get() {
        return feature;
    }

    public static FeatureReplacer of(ConfiguredFeature<?, ?> feature) {
        return new FeatureReplacer(feature);
    }

    public static <T extends IFeatureConfig> FeatureReplacer of(Feature<T> feature, T config) {
        return of(feature.func_225566_b_(config));
    }
}
