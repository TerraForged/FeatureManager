package com.terraforged.feature.predicate;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;

public class MinDepth extends CachedPredicate {

    public static final MinDepth DEPTH60 = new MinDepth(60);
    public static final MinDepth DEPTH50 = new MinDepth(50);
    public static final MinDepth DEPTH40 = new MinDepth(40);

    private final int depth;

    public MinDepth(int depth) {
        this.depth = depth;
    }

    @Override
    protected boolean doTest(IChunk chunk, Biome biome) {
        return chunk.getTopBlockY(Heightmap.Type.OCEAN_FLOOR, 8, 8) < depth;
    }
}
