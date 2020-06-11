package com.terraforged.fm.template.processor;

import net.minecraft.block.BlockState;

import java.util.Map;

public class BlockReplacer implements StateProcessor {

    private final Map<BlockState, BlockState> states;

    public BlockReplacer(Map<BlockState, BlockState> states) {
        this.states = states;
    }

    @Override
    public BlockState process(BlockState state) {
        return states.getOrDefault(state, state);
    }
}
