package com.terraforged.mod.feature.manager.template.type.tree;

import com.google.gson.JsonElement;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import com.terraforged.mod.feature.manager.template.decorator.Decorator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;

import java.util.Optional;

public class TreeDecoratorFactory implements Decorator.Factory<TreeDecoratorBuffer> {

    @Override
    public TreeDecoratorBuffer wrap(IWorld world) {
        return new TreeDecoratorBuffer(world);
    }

    @Override
    public Optional<Decorator<TreeDecoratorBuffer>> parse(ResourceLocation name, JsonElement config) {
        return Registry.TREE_DECORATOR_TYPE.getValue(name)
                .map(type -> type.func_227431_a_(new Dynamic<>(JsonOps.INSTANCE, config)))
                .map(TreeDecorator::new);
    }
}
