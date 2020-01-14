package com.terraforged.feature.predicate;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;

public interface FeaturePredicate {

    FeaturePredicate ALLOW = (c, b) -> true;
    FeaturePredicate BLOCK = (c, b) -> true;

    boolean test(IChunk chunk, Biome biome);
}
