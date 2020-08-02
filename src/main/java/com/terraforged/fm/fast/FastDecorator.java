package com.terraforged.fm.fast;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.stream.Stream;

public abstract class FastDecorator<C extends IPlacementConfig> extends Placement<C> {

    public FastDecorator(Codec<C> codec) {
        super(codec);
    }

    public FastDecorator<C> name(String name) {
        setRegistryName(name);
        return this;
    }

    @Override
    public final Stream<BlockPos> getPositions(IWorld world, ChunkGenerator generator, Random random, C config, BlockPos pos) {
        return Stream.empty();
    }

    @Override
    protected final <FC extends IFeatureConfig, F extends Feature<FC>> boolean func_236963_a_(ISeedReader world, StructureManager structures, ChunkGenerator generator, Random random, BlockPos pos, C config, ConfiguredFeature<FC, F> feature) {
        boolean result = false;
        int count = getCount(config);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int i = 0; i < count; i++) {
            if (next(world, generator, random, config, pos, mutable, i)) {
                result |= feature.func_236265_a_(world, structures, generator, random, mutable);
            }
        }
        return result;
    }

    protected abstract int getCount(C config);

    protected abstract boolean next(IWorld world, ChunkGenerator generator, Random random, C config, BlockPos pos, BlockPos.Mutable mutable, int count);
}
