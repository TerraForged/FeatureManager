package com.terraforged.feature.predicate;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;

public class DeepWater extends CachedPredicate {

    public static final DeepWater INSTANCE = new DeepWater();

    @Override
    public boolean doTest(IChunk chunk, Biome biome) {
        return biome.getCategory() == Biome.Category.OCEAN && biome.getDepth() < -1;
    }
}
