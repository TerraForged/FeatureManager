package com.terraforged.feature.template.decorator.tree;

import com.terraforged.feature.template.decorator.Decorator;
import net.minecraft.world.gen.treedecorator.TreeDecorator;

import java.util.Random;

public class TreeFeatureDecorator implements Decorator<TreeBuffer> {

    private final TreeDecorator decorator;

    public TreeFeatureDecorator(TreeDecorator decorator) {
        this.decorator = decorator;
    }

    @Override
    public void apply(TreeBuffer world, Random random) {
        decorator.func_225576_a_(
                world.getDelegate(),
                random,
                world.getLogs(),
                world.getLeaves(),
                world.getAllPositions(),
                world.getBounds()
        );
    }
}
