package com.terraforged.fm.util.delegate;

import net.minecraft.util.math.SectionPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;

import java.util.stream.Stream;

public class SeedWorldDelegate extends WorldDelegate<ISeedReader> implements ISeedReader {

    private long seed;

    public SeedWorldDelegate(ISeedReader world) {
        super(world);
        seed = world.getSeed();
    }

    @Override
    public ServerWorld getWorld() {
        return delegate.getWorld();
    }

    @Override
    public void setDelegate(ISeedReader world) {
        super.setDelegate(world);
        seed = world.getSeed();
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public Stream<? extends StructureStart<?>> func_241827_a(SectionPos pos, Structure<?> structure) {
        return delegate.func_241827_a(pos, structure);
    }
}
