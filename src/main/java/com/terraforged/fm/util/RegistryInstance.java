package com.terraforged.fm.util;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class RegistryInstance<T> implements Iterable<T> {

    private final Registry<T> registry;
    private final Map<T, T> remaps = new HashMap<>();

    public RegistryInstance(DynamicRegistries.Impl registry, RegistryKey<? extends Registry<T>> key) {
        this.registry = registry.func_243612_b(key);
    }

    public void addRemap(T in, T out) {
        remaps.put(in, out);
    }

    public T getRemap(T in) {
        return remaps.getOrDefault(in, in);
    }

    public Registry<T> getRegistry() {
        return registry;
    }

    public Optional<T> get(ResourceLocation name) {
        return registry.func_241873_b(name);
    }

    public T get(RegistryKey<T> key) {
        return registry.getValueForKey(key);
    }

    public T mustGet(ResourceLocation name) {
        return registry.func_241873_b(name).orElse(null);
    }

    public ResourceLocation getRegistryName(T t) {
        return registry.getKey(t);
    }

    public String getName(T t) {
        return registry.getKey(t).toString();
    }

    public boolean contains(ResourceLocation name) {
        return registry.containsKey(name);
    }

    @Override
    public Iterator<T> iterator() {
        return registry.iterator();
    }
}
