package com.terraforged.mod.feature.manager.template;

import com.terraforged.mod.feature.manager.util.ObjectPool;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TemplateBuffer {

    private static final ObjectPool<TemplateBuffer> pool = new ObjectPool<>(10, TemplateBuffer::new);

    private IWorld world;
    private BlockPos origin;

    private final Set<BlockPos> mask = new HashSet<>();
    private final BlockPos.Mutable mutable = new BlockPos.Mutable();
    private final List<Template.BlockInfo> buffer = new LinkedList<>();

    public TemplateBuffer init(IWorld world, BlockPos origin) {
        this.world = world;
        this.origin = origin;
        return this;
    }

    public void flush() {
        this.mask.clear();
        this.buffer.clear();
    }

    public List<Template.BlockInfo> getBlocks() {
        return buffer;
    }

    public void record(BlockPos pos, BlockState state, PasteConfig config) {
        if (!config.replaceSolid && world.getBlockState(pos).isSolid()) {
            mask.add(pos);
            return;
        }

        if (!config.pasteAir && state.getBlock() == Blocks.AIR) {
            return;
        }

        buffer.add(new Template.BlockInfo(pos, state));
    }

    public boolean test(BlockPos pos) {
        int dx = pos.getX() - origin.getX();
        int dz = pos.getZ() - origin.getZ();
        if (dx == dz) {
            return test(pos, 1, 1);
        }
        if (Math.abs(dx) > Math.abs(dz)) {
            return test(pos, 1F, dz / (float) dx);
        } else {
            return test(pos, dx / (float) dz, 1F);
        }
    }

    private boolean test(BlockPos start, float dx, float dz) {
        float x = start.getX();
        float z = start.getZ();
        int count = 0;
        while (x != origin.getX() && z != origin.getZ() && count < 10) {
            mutable.setPos(x, start.getY(), z);
            if (mask.contains(mutable)) {
                return false;
            }
            x -= dx;
            z -= dz;
            count++;
        }
        return true;
    }

    public static ObjectPool.Item<TemplateBuffer> pooled() {
        return pool.get();
    }
}
