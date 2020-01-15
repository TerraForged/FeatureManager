package com.terraforged.feature.modifier;

import com.terraforged.feature.predicate.FeaturePredicate;
import com.terraforged.feature.transformer.FeatureTransformer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class FeatureModifiers extends ModifierList {

    public FeaturePredicate getPredicate(Biome biome, ConfiguredFeature<?, ?> feature) {
        for (Modifier<FeaturePredicate> modifier : getPredicates()) {
            if (modifier.getMatcher().test(biome, feature)) {
                return modifier.getModifier();
            }
        }
        return FeaturePredicate.NONE;
    }

    public ConfiguredFeature<?, ?> getFeature(Biome biome, ConfiguredFeature<?, ?> feature) {
        for (Modifier<FeatureTransformer> modifier : getTransformers()) {
            if (modifier.getMatcher().test(biome, feature)) {
                ConfiguredFeature<?, ?> result = modifier.getModifier().apply(feature);
                if (result != feature) {
                    return result;
                }
            }
        }
        return feature;
    }
}
