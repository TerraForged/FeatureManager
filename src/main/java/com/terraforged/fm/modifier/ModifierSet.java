package com.terraforged.fm.modifier;

import com.terraforged.fm.biome.BiomeFeature;

import java.util.Collections;
import java.util.List;

public class ModifierSet {

    public final BiomeFeature feature;
    public final List<BiomeFeature> before;
    public final List<BiomeFeature> after;

    public ModifierSet(BiomeFeature feature) {
        this(feature, Collections.emptyList(), Collections.emptyList());
    }

    public ModifierSet(BiomeFeature feature, List<BiomeFeature> before, List<BiomeFeature> after) {
        this.feature = feature;
        this.before = before;
        this.after = after;
    }
}
