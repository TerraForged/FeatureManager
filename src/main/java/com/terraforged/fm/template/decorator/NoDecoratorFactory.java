package com.terraforged.fm.template.decorator;

import com.google.gson.JsonElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;

import java.util.Optional;

public class NoDecoratorFactory implements Decorator.Factory<IWorld> {

    @Override
    public ISeedReader wrap(ISeedReader world) {
        return world;
    }

    @Override
    public Optional<Decorator<IWorld>> parse(ResourceLocation type, JsonElement config) {
        return Optional.empty();
    }
}
