package com.terraforged.feature.template.decorator;

import com.terraforged.feature.util.WorldDelegate;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BoundsRecorder extends WorldDelegate {

    private BlockPos.Mutable min = null;
    private BlockPos.Mutable max = null;
    private Set<BlockPos> allPositions = null;

    public BoundsRecorder(IWorld delegate) {
        super(delegate);
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState newState, int flags) {
        recordPos(pos);
        recordMin(pos.getX(), pos.getY(), pos.getZ());
        recordMax(pos.getX(), pos.getY(), pos.getZ());
        return super.setBlockState(pos, newState, flags);
    }

    public Set<BlockPos> getAllPositions() {
        if (allPositions == null) {
            return Collections.emptySet();
        }
        return allPositions;
    }

    public MutableBoundingBox getBounds() {
        if (min == null || max == null) {
            return MutableBoundingBox.getNewBoundingBox();
        }
        return MutableBoundingBox.createProper(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    private void recordPos(BlockPos pos) {
        if (allPositions == null) {
            allPositions = new HashSet<>();
        }
        allPositions.add(pos);
    }

    private void recordMin(int x, int y, int z) {
        if (min == null) {
            min = new BlockPos.Mutable(x, y, z);
        } else {
            x = Math.min(x, min.getX());
            y = Math.min(y, min.getY());
            z = Math.min(z, min.getZ());
            min.setPos(x, y, z);
        }
    }

    private void recordMax(int x, int y, int z) {
        if (max == null) {
            max = new BlockPos.Mutable(x, y, z);
        } else {
            x = Math.max(x, max.getX());
            y = Math.max(y, max.getY());
            z = Math.max(z, max.getZ());
            max.setPos(x, y, z);
        }
    }
}
