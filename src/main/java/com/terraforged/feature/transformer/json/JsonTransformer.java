package com.terraforged.feature.transformer.json;

import com.google.gson.*;
import com.terraforged.feature.FeatureJsonCache;
import com.terraforged.feature.transformer.FeatureTransformer;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JsonTransformer implements FeatureTransformer {

    private final Map<String, JsonElement> keyTransformers;
    private final Map<JsonPrimitive, JsonPrimitive> valueTransformers;

    private JsonTransformer(Builder builder) {
        this.keyTransformers = builder.keyTransformers;
        this.valueTransformers = builder.valueTransformers;
        builder.keyTransformers = Collections.emptyMap();
        builder.valueTransformers = Collections.emptyMap();
    }

    @Override
    public ConfiguredFeature<?, ?> apply(ConfiguredFeature<?, ?> feature) {
        JsonElement element = FeatureJsonCache.getInstance().getJson(feature);
        if (element == JsonNull.INSTANCE) {
            return feature;
        } else {
            return FeatureJsonCache.deserialize(transform(element)).orElse(feature);
        }
    }

    private JsonElement transform(JsonElement element) {
        if (element.isJsonArray()) {
            return transform(element.getAsJsonArray());
        }
        if (element.isJsonObject()) {
            return transform(element.getAsJsonObject());
        }
        if (element.isJsonPrimitive()) {
            return transform(element.getAsJsonPrimitive());
        }
        return element;
    }

    private JsonPrimitive transform(JsonPrimitive primitive) {
        return valueTransformers.getOrDefault(primitive, primitive);
    }

    private JsonArray transform(JsonArray source) {
        JsonArray dest = new JsonArray();
        for (JsonElement element : source) {
            dest.add(transform(element));
        }
        return dest;
    }

    private JsonObject transform(JsonObject source) {
        JsonObject dest = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : source.entrySet()) {
            JsonElement element = keyTransformers.get(entry.getKey());
            if (element == null) {
                element = transform(entry.getValue());
            }
            dest.add(entry.getKey(), element);
        }
        return dest;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Map<String, JsonElement> keyTransformers = Collections.emptyMap();
        private Map<JsonPrimitive, JsonPrimitive> valueTransformers = Collections.emptyMap();

        public Builder key(String key, boolean value) {
            return key(key, new JsonPrimitive(value));
        }

        public Builder key(String key, Number value) {
            return key(key, new JsonPrimitive(value));
        }

        public Builder key(String key, String value) {
            return key(key, new JsonPrimitive(value));
        }

        public Builder key(String key, JsonElement value) {
            if (keyTransformers.isEmpty()) {
                keyTransformers = new HashMap<>();
            }
            keyTransformers.put(key, value);
            return this;
        }

        public Builder value(boolean find, boolean replace) {
            return value(new JsonPrimitive(find), new JsonPrimitive(replace));
        }

        public Builder value(Number find, Number replace) {
            return value(new JsonPrimitive(find), new JsonPrimitive(replace));
        }

        public Builder value(String find, String replace) {
            return value(new JsonPrimitive(find), new JsonPrimitive(replace));
        }

        public Builder value(JsonPrimitive find, JsonPrimitive replace) {
            if (valueTransformers.isEmpty()) {
                valueTransformers = new HashMap<>();
            }
            valueTransformers.put(find, replace);
            return this;
        }

        public JsonTransformer build() {
            return new JsonTransformer(this);
        }
    }
}
