package com.terraforged.feature.template.decorator;

import net.minecraft.world.IWorld;

import java.util.Random;

public interface Decorator<T extends IWorld> {

    void apply(T world, Random random);
}
