package com.terraforged.fm.template.buffer;

import net.minecraft.util.math.BlockPos;

public interface BufferIterator {

    int size();

    boolean next();

    BlockPos getPos();
}
