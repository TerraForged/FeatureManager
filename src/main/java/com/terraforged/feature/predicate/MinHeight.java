package com.terraforged.feature.predicate;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;

public class MinHeight implements FeaturePredicate {

    public static final MinHeight HEIGHT64 = new MinHeight(64);
    public static final MinHeight HEIGHT80 = new MinHeight(80);
    public static final MinHeight HEIGHT100 = new MinHeight(100);

    private final int height;

    public MinHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean test(IChunk chunk, Biome biome) {
        return chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, 8, 8) > height;
    }
}
