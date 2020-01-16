package com.terraforged.feature.modifier;

import com.terraforged.feature.matcher.BiomeFeatureMatcher;
import com.terraforged.feature.matcher.biome.BiomeMatcher;
import com.terraforged.feature.matcher.feature.FeatureMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ModifierList<T> implements Iterable<Modifier<T>> {

    private List<Modifier<T>> list = Collections.emptyList();

    public int size() {
        return list.size();
    }

    public void sort() {
        Collections.sort(list);
    }

    @Override
    public Iterator<Modifier<T>> iterator() {
        return list.iterator();
    }

    public void add(FeatureMatcher featureMatcher, T modifier) {
        add(BiomeMatcher.ANY, featureMatcher, modifier);
    }

    public void add(BiomeMatcher biomeMatcher, FeatureMatcher featureMatcher, T modifier) {
        BiomeFeatureMatcher matcher = new BiomeFeatureMatcher(biomeMatcher, featureMatcher);
        add(matcher, modifier);
    }

    public void add(BiomeFeatureMatcher matcher, T modifier) {
        if (list.isEmpty()) {
            list = new ArrayList<>();
        }
        list.add(new Modifier<>(matcher, modifier));
    }
}
