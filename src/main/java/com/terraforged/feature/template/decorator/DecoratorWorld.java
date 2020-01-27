package com.terraforged.feature.template.decorator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public interface DecoratorWorld extends IWorld {

    void setDelegate(IWorld world);

    void translate(BlockPos offset);
}
