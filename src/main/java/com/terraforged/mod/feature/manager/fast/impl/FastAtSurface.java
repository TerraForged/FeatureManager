package com.terraforged.mod.feature.manager.fast.impl;

import com.terraforged.mod.feature.manager.fast.FastFrequencyDecorator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.FrequencyConfig;

import java.util.Random;

public class FastAtSurface extends FastFrequencyDecorator {
    @Override
    protected boolean next(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random, FrequencyConfig config, BlockPos pos, BlockPos.Mutable mutable) {
        int x = random.nextInt(16) + pos.getX();
        int z = random.nextInt(16) + pos.getZ();
        int y = world.getHeight(Heightmap.Type.MOTION_BLOCKING, x, z);
        mutable.setPos(x, y, z);
        return true;
    }
}
