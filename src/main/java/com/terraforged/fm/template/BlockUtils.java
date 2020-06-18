package com.terraforged.fm.template;

import net.minecraft.block.BlockState;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class BlockUtils {

    public static boolean isSolid(IWorldReader reader, BlockPos pos) {
        BlockState state = reader.getBlockState(pos);
        return state.isSolid() || !state.allowsMovement(reader, pos, PathType.LAND);
    }
}
