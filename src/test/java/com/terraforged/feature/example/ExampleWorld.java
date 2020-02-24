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

package com.terraforged.feature.example;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.biome.provider.OverworldBiomeProviderSettings;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.OverworldGenSettings;

public class ExampleWorld extends WorldType {

    public static final ExampleWorld TYPE = new ExampleWorld("example_world");

    public ExampleWorld(String name) {
        super(name);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator(World world) {
        if (world.getDimension().getType() != DimensionType.OVERWORLD) {
            return super.createChunkGenerator(world);
        }
        OverworldGenSettings genSettings = new OverworldGenSettings();
        OverworldBiomeProviderSettings biomeSettings = new OverworldBiomeProviderSettings(world.getWorldInfo());
        biomeSettings.setGeneratorSettings(genSettings);
        BiomeProvider biomes = new OverworldBiomeProvider(biomeSettings);
        return new ExampleChunkGenerator(world, biomes, genSettings);
    }

    public static void init() {

    }
}
