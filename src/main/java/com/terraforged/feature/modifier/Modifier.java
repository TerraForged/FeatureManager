package com.terraforged.feature.modifier;

import com.terraforged.feature.matcher.FeatureMatcher;

public class Modifier<T> {

    private final FeatureMatcher matcher;
    private final T modifier;

    public Modifier(FeatureMatcher matcher, T modifier) {
        this.matcher = matcher;
        this.modifier = modifier;
    }

    public FeatureMatcher getMatcher() {
        return matcher;
    }

    public T getModifier() {
        return modifier;
    }
}
