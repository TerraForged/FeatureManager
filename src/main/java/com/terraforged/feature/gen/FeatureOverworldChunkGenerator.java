package com.terraforged.feature.gen;

import com.terraforged.feature.FeatureManager;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.OverworldChunkGenerator;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.WorldGenRegion;

public class FeatureOverworldChunkGenerator extends OverworldChunkGenerator implements FeatureDecorator {

    private final FeatureManager featureManager;

    public FeatureOverworldChunkGenerator(IWorld world, BiomeProvider biomeProvider, OverworldGenSettings settings) {
        super(world, biomeProvider, settings);
        this.featureManager = FeatureManager.create(world.getWorld().getWorldType());
    }

    @Override
    public FeatureManager getFeatureManager() {
        return featureManager;
    }

    @Override
    public void decorate(WorldGenRegion region) {
        decorate(this, region);
    }
}
