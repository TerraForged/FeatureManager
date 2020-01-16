package com.terraforged.feature.template.placement;

import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;

public abstract class TreePlacement extends AbstractTreeFeature<BaseTreeFeatureConfig> {

    public static final Placement PLACEMENT = (world, pos) -> isDirtOrGrassBlock(world, pos.down());

    private TreePlacement() {
        super(BaseTreeFeatureConfig::func_227376_b_);
    }
}
