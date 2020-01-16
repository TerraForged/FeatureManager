package com.terraforged.feature.template.placement;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.IWorldGenerationReader;

public interface Placement {

    Placement ANY = (r, p) -> true;

    boolean canPlaceAt(IWorldGenerationReader reader, BlockPos pos);
}
