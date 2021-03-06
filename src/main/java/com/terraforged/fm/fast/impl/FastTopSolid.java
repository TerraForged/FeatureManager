package com.terraforged.fm.fast.impl;

import com.terraforged.fm.fast.FastFrequencyDecorator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.FrequencyConfig;

import java.util.Random;

public class FastTopSolid extends FastFrequencyDecorator {
    @Override
    protected boolean next(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random, FrequencyConfig config, BlockPos pos, BlockPos.Mutable mutable, int i) {
        int x = random.nextInt(16) + pos.getX();
        int z = random.nextInt(16) + pos.getZ();
        int y = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, x, z);
        mutable.setPos(x, y, z);
        return true;
    }
}
