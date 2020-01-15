package com.terraforged.feature.matcher;

import com.terraforged.feature.matcher.biome.BiomeMatcher;
import com.terraforged.feature.matcher.feature.FeatureMatcher;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.function.BiPredicate;

public class BiomeFeatureMatcher implements BiPredicate<Biome, ConfiguredFeature<?, ?>> {

    public static final BiomeFeatureMatcher ANY = new BiomeFeatureMatcher(BiomeMatcher.ANY, FeatureMatcher.ANY);

    private final BiomeMatcher biomeMatcher;
    private final FeatureMatcher featureMatcher;

    public BiomeFeatureMatcher(BiomeMatcher biomeMatcher, FeatureMatcher featureMatcher) {
        this.biomeMatcher = biomeMatcher;
        this.featureMatcher = featureMatcher;
    }

    @Override
    public boolean test(Biome biome, ConfiguredFeature<?, ?> feature) {
        return getBiomeMatcher().test(biome) && getFeatureMatcher().test(feature);
    }

    public BiomeMatcher getBiomeMatcher() {
        return biomeMatcher;
    }

    public FeatureMatcher getFeatureMatcher() {
        return featureMatcher;
    }
}
