package com.terraforged.feature.template.decorator.tree;

import com.terraforged.feature.template.decorator.BoundsRecorder;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorld;

import java.util.*;

public class TreeRecorder extends BoundsRecorder {

    private Set<BlockPos> logs = null;
    private Set<BlockPos> leaves = null;

    private List<BlockPos> logList = null;
    private List<BlockPos> leafList = null;

    public TreeRecorder(IWorld delegate) {
        super(delegate);
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState state, int flags) {
        recordState(pos, state);
        return super.setBlockState(pos, state, flags);
    }

    public List<BlockPos> getLogs() {
        if (logList == null) {
            if (logs == null) {
                logList = Collections.emptyList();
            } else {
                logList = new ArrayList<>(logs);
                logList.sort(Comparator.comparingInt(Vec3i::getY));
            }
        }
        return logList;
    }

    public List<BlockPos> getLeaves() {
        if (leafList == null) {
            if (leaves == null) {
                leafList = Collections.emptyList();
            } else {
                leafList = new ArrayList<>(leaves);
                leafList.sort(Comparator.comparingInt(Vec3i::getY));
            }
        }
        return leafList;
    }

    private void recordState(BlockPos pos, BlockState state) {
        if (BlockTags.LEAVES.contains(state.getBlock())) {
            addLeaves(pos);
            return;
        }

        if (BlockTags.LOGS.contains(state.getBlock())) {
            addLog(pos);
        }
    }

    private void addLog(BlockPos pos) {
        if (logs == null) {
            logs = new HashSet<>();
        }
        logs.add(pos);
    }

    private void addLeaves(BlockPos pos) {
        if (leaves == null) {
            leaves = new HashSet<>();
        }
        leaves.add(pos);
    }
}
