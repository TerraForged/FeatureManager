package com.terraforged.feature.gen;

import com.terraforged.feature.FeatureManager;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.WorldGenRegion;

public abstract class AbstractChunkGenerator<T extends GenerationSettings> extends ChunkGenerator<T> implements FeatureDecorator {

    private final FeatureManager featureManager;

    public AbstractChunkGenerator(IWorld world, BiomeProvider biomeProvider, T config) {
        super(world, biomeProvider, config);
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
