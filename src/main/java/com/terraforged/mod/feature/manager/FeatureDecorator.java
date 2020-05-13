/*
 *
 * MIT License
 *
 * Copyright (c) 2020 TerraForged
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.terraforged.mod.feature.manager;

import com.terraforged.mod.feature.manager.biome.BiomeFeature;
import com.terraforged.mod.feature.manager.biome.BiomeFeatures;
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
        Biome biome = region.getBiomeManager().getBiome(pos.add(8, 8, 8));

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
