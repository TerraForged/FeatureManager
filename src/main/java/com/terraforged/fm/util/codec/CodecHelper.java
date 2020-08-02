package com.terraforged.fm.util.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.OptionalDynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class CodecHelper {

    public static <T> BlockState getState(Dynamic<T> dynamic) {
        return BlockState.BLOCKSTATE_CODEC.decode(dynamic).result().map(Pair::getFirst).orElseGet(Blocks.AIR::getDefaultState);
    }

    public static <T> BlockState getState(OptionalDynamic<T> dynamic) {
        return dynamic.result().map(CodecHelper::getState).orElseGet(Blocks.AIR::getDefaultState);
    }

    public static <T> T setState(BlockState state, DynamicOps<T> ops) {
        return BlockState.BLOCKSTATE_CODEC.encodeStart(ops, state).result().orElseThrow(RuntimeException::new);
    }

    public static <T> TreeDecorator treeDecorator(TreeDecoratorType<?> type, T t, DynamicOps<T> ops) {
        return type.func_236876_a_().decode(ops, t).map(Pair::getFirst).result().orElseThrow(RuntimeException::new);
    }
}
