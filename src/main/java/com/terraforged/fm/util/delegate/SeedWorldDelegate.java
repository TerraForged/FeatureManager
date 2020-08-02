package com.terraforged.fm.util.delegate;

import net.minecraft.world.ISeedReader;

public class SeedWorldDelegate extends WorldDelegate<ISeedReader> implements ISeedReader {

    private long seed;

    public SeedWorldDelegate(ISeedReader world) {
        super(world);
        seed = world.getSeed();
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
}
