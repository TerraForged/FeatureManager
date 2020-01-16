package com.terraforged.feature.data;

import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public interface ResourceVisitor<T> {

    void accept(ResourceLocation location, T data) throws IOException;
}
