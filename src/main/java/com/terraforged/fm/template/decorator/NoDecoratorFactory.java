package com.terraforged.fm.template.decorator;

import com.google.gson.JsonElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;

import java.util.Optional;

public class NoDecoratorFactory implements Decorator.Factory<IWorld> {

    @Override
    public IWorld wrap(IWorld world) {
        return world;
    }

    @Override
    public Optional<Decorator<IWorld>> parse(ResourceLocation type, JsonElement config) {
        return Optional.empty();
    }
}
