package com.terraforged.feature.modifier;

import com.google.gson.JsonElement;
import com.terraforged.feature.FeatureSerializer;
import com.terraforged.feature.biome.BiomeFeature;
import com.terraforged.feature.predicate.FeaturePredicate;
import com.terraforged.feature.transformer.FeatureReplacer;
import com.terraforged.feature.transformer.FeatureTransformer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class FeatureModifiers {

    private final ModifierList<FeatureReplacer> replacers = new ModifierList<>();
    private final ModifierList<FeaturePredicate> predicates = new ModifierList<>();
    private final ModifierList<FeatureTransformer> transformers = new ModifierList<>();

    public ModifierList<FeatureReplacer> getReplacers() {
        return replacers;
    }

    public ModifierList<FeaturePredicate> getPredicates() {
        return predicates;
    }

    public ModifierList<FeatureTransformer> getTransformers() {
        return transformers;
    }

    public BiomeFeature getFeature(Biome biome, ConfiguredFeature<?, ?> feature) {
        JsonElement element = FeatureSerializer.serialize(feature);
        FeaturePredicate predicate = getPredicate(biome, element);
        ConfiguredFeature<?, ?> result = getFeature(biome, feature, element);
        return new BiomeFeature(predicate, result);
    }

    private FeaturePredicate getPredicate(Biome biome, JsonElement element) {
        for (Modifier<FeaturePredicate> modifier : predicates) {
            if (modifier.getMatcher().test(biome, element)) {
                return modifier.getModifier();
            }
        }
        return FeaturePredicate.NONE;
    }

    private ConfiguredFeature<?, ?> getFeature(Biome biome, ConfiguredFeature<?, ?> feature, JsonElement element) {
        for (Modifier<FeatureReplacer> modifier : replacers) {
            if (modifier.getMatcher().test(biome, element)) {
                return modifier.getModifier().get();
            }
        }

        for (Modifier<FeatureTransformer> modifier : transformers) {
            if (modifier.getMatcher().test(biome, element)) {
                element = modifier.getModifier().apply(element);
            }
        }

        return FeatureSerializer.deserialize(element).orElse(feature);
    }
}
