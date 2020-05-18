package com.terraforged.fm.fast.impl;

import com.terraforged.fm.fast.FastDecorator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.NoPlacementConfig;

import java.util.Random;

public class FastSurfaceLayer extends FastDecorator<NoPlacementConfig> {

    public FastSurfaceLayer() {
        super(NoPlacementConfig::deserialize);
    }

    @Override
    protected int getCount(NoPlacementConfig config) {
        return 256;
    }

    @Override
    protected boolean next(IWorld world, ChunkGenerator<?> generator, Random random, NoPlacementConfig config, BlockPos pos, BlockPos.Mutable mutable, int i) {
        int x = mutable.getX();
        int z = mutable.getZ();
        if (i == 0) {
            x = pos.getX();
            z = pos.getZ();
        } else {
            if ((x & 15) < 15) {
                x++;
            } else {
                z++;
                x = pos.getX();
            }
        }
        int y = world.getHeight(Heightmap.Type.MOTION_BLOCKING, x, z);
        mutable.setPos(x, y, z);
        return true;
    }
}
