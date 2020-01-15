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
