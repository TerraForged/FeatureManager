package com.terraforged.feature.modifier;

import com.terraforged.feature.matcher.BiomeFeatureMatcher;

public class Modifier<T> implements Comparable<Modifier<T>> {

    private final BiomeFeatureMatcher matcher;
    private final T modifier;

    public Modifier(BiomeFeatureMatcher matcher, T modifier) {
        this.matcher = matcher;
        this.modifier = modifier;
    }

    public BiomeFeatureMatcher getMatcher() {
        return matcher;
    }

    public T getModifier() {
        return modifier;
    }

    @Override
    public int compareTo(Modifier<T> o) {
        return matcher.compareTo(o.matcher);
    }
}
