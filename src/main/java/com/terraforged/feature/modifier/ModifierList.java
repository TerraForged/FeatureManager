package com.terraforged.feature.modifier;

import com.terraforged.feature.matcher.FeatureMatcher;
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

    public void add(FeatureMatcher matcher, FeaturePredicate predicate) {
        if (predicates.isEmpty()) {
            predicates = new ArrayList<>();
        }
        predicates.add(new Modifier<>(matcher, predicate));
    }

    public void add(FeatureMatcher matcher, FeatureTransformer transformer) {
        if (transformer == FeatureTransformer.NONE) {
            return;
        }
        if (transformers.isEmpty()) {
            transformers = new ArrayList<>();
        }
        transformers.add(new Modifier<>(matcher, transformer));
    }
}
