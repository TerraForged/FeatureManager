package com.terraforged.feature.predicate;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;

public class MatchBiome extends CachedPredicate {

    private final Biome biome;

    public MatchBiome(Biome biome) {
        this.biome = biome;
    }

    @Override
    protected boolean doTest(IChunk chunk, Biome biome) {
        return biome == this.biome;
    }
}
