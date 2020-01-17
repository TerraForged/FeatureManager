package com.terraforged.feature;

import com.terraforged.feature.biome.BiomeFeature;
import com.terraforged.feature.biome.BiomeFeatures;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.WorldGenRegion;

public interface FeatureDecorator {

    FeatureManager getFeatureManager();

    default void decorate(ChunkGenerator<?> generator, WorldGenRegion region) {
        int chunkX = region.getMainChunkX();
        int chunkZ = region.getMainChunkZ();
        int blockX = chunkX << 4;
        int blockZ = chunkZ << 4;
        IChunk chunk = region.getChunk(chunkX, chunkZ);

        BlockPos pos = new BlockPos(blockX, 0, blockZ);
        Biome biome = region.func_225523_d_().func_226836_a_(pos.add(8, 8, 8));

        decorate(generator, region, chunk, biome, pos);
    }

    default void decorate(ChunkGenerator<?> generator, IWorld region, IChunk chunk, Biome biome, BlockPos pos) {
        SharedSeedRandom random = new SharedSeedRandom();
        long decorationSeed = random.setDecorationSeed(region.getSeed(), pos.getX(), pos.getZ());

        BiomeFeatures features = getFeatureManager().getFeatures(biome);
        for (GenerationStage.Decoration stage : GenerationStage.Decoration.values()) {
            int featureOrdinal = 0;
            for (BiomeFeature feature : features.getStage(stage)) {
                random.setFeatureSeed(decorationSeed, featureOrdinal++, stage.ordinal());

                if (feature.getPredicate().test(chunk, biome)) {
                    feature.getFeature().place(region, generator, random, pos);
                }
            }
        }
    }
}
