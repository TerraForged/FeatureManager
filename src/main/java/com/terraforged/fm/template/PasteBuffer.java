package com.terraforged.fm.template;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasteBuffer {

    private boolean updatePostPlacement;
    private List<BlockPos> placedBlocks = Collections.emptyList();

    public Iterator iterator() {
        return new Iterator();
    }

    public void configure(PasteConfig config) {
        updatePostPlacement = config.checkBounds;
        placedBlocks.clear();
    }

    public void record(BlockPos position) {
        if (updatePostPlacement) {
            add(position);
        }
    }

    private void add(BlockPos pos) {
        if (placedBlocks.isEmpty()) {
            placedBlocks = new ArrayList<>();
        }
        placedBlocks.add(pos);
    }

    public class Iterator {

        private int index = -1;
        private BlockPos pos = BlockPos.ZERO;

        public boolean next() {
            while (++index < placedBlocks.size()) {
                pos = placedBlocks.get(index);
                if (pos != BlockPos.ZERO) {
                    return true;
                }
            }
            return false;
        }

        public BlockPos get() {
            return pos;
        }

        public void remove(BlockPos pos) {
            int index = placedBlocks.indexOf(pos);
            if (index > -1 && index < placedBlocks.size()) {
                placedBlocks.set(index, BlockPos.ZERO);
            }
        }
    }
}
