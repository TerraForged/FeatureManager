package com.terraforged.feature.biome;

import com.terraforged.feature.predicate.FeaturePredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class BiomeFeature {

    private final FeaturePredicate predicate;
    private final ConfiguredFeature<?, ?> feature;

    public BiomeFeature(FeaturePredicate predicate, ConfiguredFeature<?, ?> feature) {
        this.predicate = predicate;
        this.feature = feature;
    }

    public FeaturePredicate getPredicate() {
        return predicate;
    }

    public ConfiguredFeature<?, ?> getFeature() {
        return feature;
    }
}
