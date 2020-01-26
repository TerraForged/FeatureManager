package com.terraforged.feature.matcher.dynamic;

import com.terraforged.feature.predicate.FeaturePredicate;

public class DynamicPredicate {

    private final DynamicMatcher matcher;
    private final FeaturePredicate predicate;

    public DynamicPredicate(DynamicMatcher matcher, FeaturePredicate predicate) {
        this.matcher = matcher;
        this.predicate = predicate;
    }

    public DynamicMatcher getMatcher() {
        return matcher;
    }

    public FeaturePredicate getPredicate() {
        return predicate;
    }
}
