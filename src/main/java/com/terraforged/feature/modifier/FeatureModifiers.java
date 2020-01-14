package com.terraforged.feature.modifier;

import com.terraforged.feature.matcher.FeatureMatcher;
import com.terraforged.feature.predicate.FeaturePredicate;
import com.terraforged.feature.transformer.FeatureTransformer;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class FeatureModifiers extends ModifierList {

    private final Map<GenerationStage.Decoration, ModifierList> stages = new EnumMap<>(GenerationStage.Decoration.class);

    public List<Modifier<FeaturePredicate>> getPredicates(GenerationStage.Decoration stage) {
        ModifierList list = stages.get(stage);
        if (list == null) {
            return ModifierList.emptyPredicates;
        }
        return list.getPredicates();
    }

    public List<Modifier<FeatureTransformer>> getTransformers(GenerationStage.Decoration stage) {
        ModifierList list = stages.get(stage);
        if (list == null) {
            return ModifierList.emptyTransformers;
        }
        return list.getTransformers();
    }

    public void add(GenerationStage.Decoration stage, FeatureMatcher matcher, FeaturePredicate predicate) {
        stages.computeIfAbsent(stage, s -> new ModifierList()).add(matcher, predicate);
    }

    public void add(GenerationStage.Decoration stage, FeatureMatcher matcher, FeatureTransformer transformer) {
        stages.computeIfAbsent(stage, s -> new ModifierList()).add(matcher, transformer);
    }

    public FeaturePredicate getPredicate(GenerationStage.Decoration stage, ConfiguredFeature<?, ?> feature) {
        for (Modifier<FeaturePredicate> modifier : getPredicates(stage)) {
            if (modifier.getMatcher().test(feature)) {
                return modifier.getModifier();
            }
        }
        for (Modifier<FeaturePredicate> modifier : getPredicates()) {
            if (modifier.getMatcher().test(feature)) {
                return modifier.getModifier();
            }
        }
        return FeaturePredicate.ALLOW;
    }

    public ConfiguredFeature<?, ?> getFeature(GenerationStage.Decoration stage, ConfiguredFeature<?, ?> feature) {
        for (Modifier<FeatureTransformer> modifier : getTransformers(stage)) {
            if (modifier.getMatcher().test(feature)) {
                ConfiguredFeature<?, ?> result = modifier.getModifier().apply(feature);
                if (result != feature) {
                    return result;
                }
            }
        }
        for (Modifier<FeatureTransformer> modifier : getTransformers()) {
            if (modifier.getMatcher().test(feature)) {
                ConfiguredFeature<?, ?> result = modifier.getModifier().apply(feature);
                if (result != feature) {
                    return result;
                }
            }
        }
        return feature;
    }
}
