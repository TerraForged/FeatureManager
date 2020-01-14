package com.terraforged.feature;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FeatureJsonCache {

    private static final FeatureJsonCache instance = new FeatureJsonCache();

    private final Map<ConfiguredFeature<?, ?>, JsonElement> cache = new HashMap<>();

    private FeatureJsonCache() {

    }

    public JsonElement getJson(ConfiguredFeature<?, ?> feature) {
        return cache.computeIfAbsent(feature, FeatureJsonCache::serialize);
    }

    public static FeatureJsonCache getInstance() {
        return instance;
    }

    public static JsonElement serialize(ConfiguredFeature<?, ?> feature) {
        try {
            return feature.serialize(JsonOps.INSTANCE).getValue();
        } catch (Throwable t) {
            t.printStackTrace();
            return JsonNull.INSTANCE;
        }
    }

    public static Optional<ConfiguredFeature<?, ?>> deserialize(JsonElement element) {
        try {
            Dynamic<JsonElement> dynamic = new Dynamic<>(JsonOps.INSTANCE, element);
            ConfiguredFeature<?, ?> feature = ConfiguredFeature.deserialize(dynamic);
            return Optional.of(feature);
        } catch (Throwable t) {
            t.printStackTrace();
            return Optional.empty();
        }
    }
}
