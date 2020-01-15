package com.terraforged.feature.matcher;

import com.google.gson.JsonElement;
import com.terraforged.feature.matcher.biome.BiomeMatcher;
import com.terraforged.feature.matcher.feature.FeatureMatcher;
import net.minecraft.world.biome.Biome;

import java.util.function.BiPredicate;

public class BiomeFeatureMatcher implements BiPredicate<Biome, JsonElement> {

    public static final BiomeFeatureMatcher ANY = new BiomeFeatureMatcher(BiomeMatcher.ANY, FeatureMatcher.ANY);

    private final BiomeMatcher biomeMatcher;
    private final FeatureMatcher featureMatcher;

    public BiomeFeatureMatcher(BiomeMatcher biomeMatcher, FeatureMatcher featureMatcher) {
        this.biomeMatcher = biomeMatcher;
        this.featureMatcher = featureMatcher;
    }

    @Override
    public boolean test(Biome biome, JsonElement feature) {
        return getBiomeMatcher().test(biome) && getFeatureMatcher().test(feature);
    }

    public BiomeMatcher getBiomeMatcher() {
        return biomeMatcher;
    }

    public FeatureMatcher getFeatureMatcher() {
        return featureMatcher;
    }
}
