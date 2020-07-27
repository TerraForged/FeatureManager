package com.terraforged.fm.template.processor;

import net.minecraft.block.BlockState;

public interface StateProcessor {

    BlockState process(BlockState state);
}
