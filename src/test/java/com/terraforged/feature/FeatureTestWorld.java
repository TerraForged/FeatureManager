package com.terraforged.feature;

import com.terraforged.feature.gen.FeatureOverworldChunkGenerator;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.biome.provider.OverworldBiomeProviderSettings;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.OverworldGenSettings;

public class FeatureTestWorld extends WorldType {

    private static final FeatureTestWorld TYPE = new FeatureTestWorld("feature_test_world");

    public FeatureTestWorld(String name) {
        super(name);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator(World world) {
        OverworldGenSettings genSettings = new OverworldGenSettings();

        OverworldBiomeProviderSettings biomeSettings = new OverworldBiomeProviderSettings(world.getWorldInfo());
        biomeSettings.setGeneratorSettings(genSettings);
        BiomeProvider biomes = new OverworldBiomeProvider(biomeSettings);

        return new FeatureOverworldChunkGenerator(world, biomes, genSettings);
    }

    public static WorldType get() {
        return TYPE;
    }
}
