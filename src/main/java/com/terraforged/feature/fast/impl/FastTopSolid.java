package com.terraforged.feature.fast.impl;

import com.terraforged.feature.fast.FastFrequencyDecorator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.FrequencyConfig;

import java.util.Random;

public class FastTopSolid extends FastFrequencyDecorator {
    @Override
    protected boolean next(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random, FrequencyConfig config, BlockPos pos, BlockPos.Mutable mutable) {
        int i = random.nextInt(16) + pos.getX();
        int j = random.nextInt(16) + pos.getZ();
        int k = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, i, j);
        mutable.setPos(i, k, j);
        return true;
    }
}
