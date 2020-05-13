package com.terraforged.mod.feature.manager.template.type.tree;


import com.terraforged.mod.feature.manager.template.decorator.Decorator;

import java.util.Random;

public class TreeDecorator implements Decorator<TreeDecoratorBuffer> {

    private final net.minecraft.world.gen.treedecorator.TreeDecorator decorator;

    public TreeDecorator(net.minecraft.world.gen.treedecorator.TreeDecorator decorator) {
        this.decorator = decorator;
    }

    @Override
    public void apply(TreeDecoratorBuffer buffer, Random random) {
        if (buffer.getLogs().isEmpty() || buffer.getLeaves().isEmpty()) {
            return;
        }

        decorator.func_225576_a_(
                buffer.getDelegate(),
                random,
                buffer.getLogs(),
                buffer.getLeaves(),
                buffer.getAllPositions(),
                buffer.getBounds()
        );
    }
}
