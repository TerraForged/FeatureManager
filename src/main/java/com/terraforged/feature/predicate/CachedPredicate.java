package com.terraforged.feature.predicate;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;

public abstract class CachedPredicate implements FeaturePredicate {

    private Result result = null;

    @Override
    public boolean test(IChunk chunk, Biome biome) {
        if (result == null) {
            result = new Result();
            result.chunkX = chunk.getPos().x;
            result.chunkZ = chunk.getPos().z;
            result.result = doTest(chunk, biome);
            return result.result;
        }
        if (result.chunkX != chunk.getPos().x || result.chunkZ != chunk.getPos().z) {
            result.chunkX = chunk.getPos().x;
            result.chunkZ = chunk.getPos().z;
            result.result = doTest(chunk, biome);
            return result.result;
        }
        return result.result;
    }

    protected abstract boolean doTest(IChunk chunk, Biome biome);

    private static class Result {
        private int chunkX;
        private int chunkZ;
        private boolean result = false;
    }
}
