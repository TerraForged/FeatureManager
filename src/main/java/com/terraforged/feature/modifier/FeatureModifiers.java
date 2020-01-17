package com.terraforged.feature.modifier;

import com.google.gson.JsonElement;
import com.terraforged.feature.FeatureSerializer;
import com.terraforged.feature.biome.BiomeFeature;
import com.terraforged.feature.predicate.FeaturePredicate;
import com.terraforged.feature.transformer.FeatureReplacer;
import com.terraforged.feature.transformer.FeatureTransformer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.eventbus.api.Event;

public class FeatureModifiers extends Event {

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

    public void sort() {
        replacers.sort();
        predicates.sort();
        transformers.sort();
        ;
    }

    public BiomeFeature getFeature(Biome biome, ConfiguredFeature<?, ?> feature) {
        JsonElement element = FeatureSerializer.serialize(feature);
        ConfiguredFeature<?, ?> result = getFeature(biome, feature, element);
        if (result != feature) {
            // re-serialize if feature has been changed
            element = FeatureSerializer.serialize(result);
        }
        FeaturePredicate predicate = getPredicate(biome, element);
        return new BiomeFeature(predicate, result);
    }

    private ConfiguredFeature<?, ?> getFeature(Biome biome, ConfiguredFeature<?, ?> feature, JsonElement element) {
        for (Modifier<FeatureReplacer> modifier : replacers) {
            if (modifier.getMatcher().test(biome, element)) {
                return modifier.getModifier().get();
            }
        }

        boolean modified = false;
        for (Modifier<FeatureTransformer> modifier : transformers) {
            if (modifier.getMatcher().test(biome, element)) {
                modified = true;
                element = modifier.getModifier().apply(element);
            }
        }

        if (!modified) {
            return feature;
        }

        return FeatureSerializer.deserialize(element).orElse(feature);
    }

    private FeaturePredicate getPredicate(Biome biome, JsonElement element) {
        for (Modifier<FeaturePredicate> modifier : predicates) {
            if (modifier.getMatcher().test(biome, element)) {
                return modifier.getModifier();
            }
        }
        return FeaturePredicate.PASS;
    }
}
