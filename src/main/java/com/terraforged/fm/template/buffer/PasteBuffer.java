package com.terraforged.fm.template.buffer;

import com.terraforged.fm.template.PasteConfig;
import com.terraforged.fm.util.ObjectPool;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasteBuffer implements BufferIterator {

    private static final ObjectPool<PasteBuffer> pool = new ObjectPool<>(8, PasteBuffer::new);

    private boolean updatePostPlacement;
    private List<BlockPos> placedBlocks = Collections.emptyList();

    private int index = -1;
    private BlockPos pos = BlockPos.ZERO;

    @Override
    public int size() {
        return placedBlocks.size();
    }

    @Override
    public boolean next() {
        while (++index < placedBlocks.size()) {
            pos = placedBlocks.get(index);
            if (pos != BlockPos.ZERO) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    public PasteBuffer configure(PasteConfig config) {
        updatePostPlacement = config.checkBounds;
        placedBlocks.clear();
        index = -1;
        return this;
    }

    public void record(BlockPos position) {
        if (updatePostPlacement) {
            if (placedBlocks.isEmpty()) {
                placedBlocks = new ArrayList<>();
            }
            placedBlocks.add(position);
        }
    }

    public static ObjectPool.Item<PasteBuffer> retain(PasteConfig config) {
        ObjectPool.Item<PasteBuffer> buffer = pool.get();
        buffer.getValue().configure(config);
        return buffer;
    }
}
