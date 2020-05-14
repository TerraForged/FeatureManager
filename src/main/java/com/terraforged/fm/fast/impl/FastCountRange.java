package com.terraforged.fm.fast.impl;

import com.terraforged.fm.fast.FastDecorator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.placement.CountRangeConfig;

import java.util.Random;

public class FastCountRange extends FastDecorator<CountRangeConfig> {

    public FastCountRange() {
        super(CountRangeConfig::deserialize);
    }

    @Override
    protected int getCount(CountRangeConfig config) {
        return config.count;
    }

    @Override
    protected boolean next(IWorld world, ChunkGenerator<?> generator, Random random, CountRangeConfig config, BlockPos pos, BlockPos.Mutable mutable) {
        int x = random.nextInt(16) + pos.getX();
        int z = random.nextInt(16) + pos.getZ();
        int y = random.nextInt(config.maximum - config.topOffset) + config.bottomOffset;
        mutable.setPos(x, y, z);
        return true;
    }
}
