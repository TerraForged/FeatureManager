package com.terraforged.feature.predicate;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;

public class MatchBiome implements FeaturePredicate {

    private final Biome biome;

    public MatchBiome(Biome biome) {
        this.biome = biome;
    }

    @Override
    public boolean test(IChunk chunk, Biome biome) {
        return biome == this.biome;
    }
}
