package com.terraforged.feature.modifier;

import com.terraforged.feature.matcher.BiomeFeatureMatcher;
import com.terraforged.feature.matcher.biome.BiomeMatcher;
import com.terraforged.feature.matcher.feature.FeatureMatcher;
import com.terraforged.feature.predicate.FeaturePredicate;
import com.terraforged.feature.transformer.FeatureTransformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModifierList {

    protected static final List<Modifier<FeaturePredicate>> emptyPredicates = Collections.emptyList();
    protected static final List<Modifier<FeatureTransformer>> emptyTransformers = Collections.emptyList();

    private List<Modifier<FeaturePredicate>> predicates = emptyPredicates;
    private List<Modifier<FeatureTransformer>> transformers = emptyTransformers;

    public List<Modifier<FeaturePredicate>> getPredicates() {
        return predicates;
    }

    public List<Modifier<FeatureTransformer>> getTransformers() {
        return transformers;
    }

    public void add(FeatureMatcher featureMatcher, FeaturePredicate predicate) {
        add(BiomeMatcher.ANY, featureMatcher, predicate);
    }

    public void add(BiomeMatcher biomeMatcher, FeatureMatcher featureMatcher, FeaturePredicate predicate) {
        BiomeFeatureMatcher matcher = new BiomeFeatureMatcher(biomeMatcher, featureMatcher);
        add(matcher, predicate);
    }

    public void add(BiomeFeatureMatcher matcher, FeaturePredicate predicate) {
        if (predicates.isEmpty()) {
            predicates = new ArrayList<>();
        }
        predicates.add(new Modifier<>(matcher, predicate));
    }

    public void add(FeatureMatcher featureMatcher, FeatureTransformer transformer) {
        add(BiomeMatcher.ANY, featureMatcher, transformer);
    }

    public void add(BiomeMatcher biomeMatcher, FeatureMatcher featureMatcher, FeatureTransformer transformer) {
        BiomeFeatureMatcher matcher = new BiomeFeatureMatcher(biomeMatcher, featureMatcher);
        add(matcher, transformer);
    }

    public void add(BiomeFeatureMatcher matcher, FeatureTransformer transformer) {
        if (transformer == FeatureTransformer.NONE) {
            return;
        }
        if (transformers.isEmpty()) {
            transformers = new ArrayList<>();
        }
        transformers.add(new Modifier<>(matcher, transformer));
    }
}
