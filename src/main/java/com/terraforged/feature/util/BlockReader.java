package com.terraforged.feature.util;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockReader implements IBlockReader {

    private BlockState state;

    public BlockReader setState(BlockState state) {
        this.state = state;
        return this;
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return null;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return state;
    }

    @Override
    public IFluidState getFluidState(BlockPos pos) {
        return Fluids.EMPTY.getDefaultState();
    }
}
