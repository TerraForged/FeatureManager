package com.terraforged.fm;

import com.terraforged.fm.util.RegistryInstance;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class GameContext {

    public final RegistryInstance<Biome> biomes;

    public GameContext(DynamicRegistries.Impl registries) {
        biomes = new RegistryInstance<>(registries, Registry.BIOME_KEY);
    }
}
