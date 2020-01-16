package com.terraforged.feature.predicate;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;

import java.util.function.BiPredicate;

public interface FeaturePredicate extends BiPredicate<IChunk, Biome> {

    FeaturePredicate NONE = (c, b) -> true;
    FeaturePredicate DENY = (c, b) -> false;

    @Override
    boolean test(IChunk chunk, Biome biome);
}
