package com.terraforged.fm.template.type.tree;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.terraforged.fm.template.decorator.Decorator;
import com.terraforged.fm.util.codec.CodecHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ISeedReader;

import java.util.Optional;

public class TreeDecoratorFactory implements Decorator.Factory<TreeDecoratorBuffer> {

    @Override
    public TreeDecoratorBuffer wrap(ISeedReader world) {
        return new TreeDecoratorBuffer(world);
    }

    @Override
    public Optional<Decorator<TreeDecoratorBuffer>> parse(ResourceLocation name, JsonElement config) {
        return Registry.TREE_DECORATOR_TYPE.func_241873_b(name)
                .map(type -> CodecHelper.treeDecorator(type, config, JsonOps.INSTANCE))
                .map(TreeDecorator::new);
    }
}
