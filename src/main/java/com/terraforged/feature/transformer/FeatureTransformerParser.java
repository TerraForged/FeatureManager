package com.terraforged.feature.transformer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.terraforged.feature.FeatureSerializer;

import java.util.Map;
import java.util.Optional;

public class FeatureTransformerParser {

    public static Optional<FeatureReplacer> parseReplacer(JsonObject root) {
        if (root.has("replace")) {
            return FeatureSerializer.deserialize(root.get("replace")).map(FeatureReplacer::of);
        }
        return Optional.empty();
    }

    public static Optional<FeatureTransformer> parseTransformer(JsonObject root) {
        if (root.has("transform")) {
            return parseTransform(root.get("transform"));
        }
        return Optional.empty();
    }

    private static Optional<FeatureTransformer> parseTransform(JsonElement element) {
        if (!element.isJsonObject()) {
            return Optional.empty();
        }
        FeatureTransformer.Builder builder = FeatureTransformer.builder();
        for (Map.Entry<String, JsonElement> e : element.getAsJsonObject().entrySet()) {
            if (e.getValue().isJsonPrimitive()) {
                JsonPrimitive key = keyToPrimitive(e.getKey(), e.getValue().getAsJsonPrimitive());
                if (key == null) {
                    return Optional.empty();
                }
                builder.value(key, e.getValue().getAsJsonPrimitive());
            } else {
                builder.key(e.getKey(), e.getValue());
            }
        }
        return Optional.of(builder.build());
    }

    private static JsonPrimitive keyToPrimitive(String key, JsonPrimitive value) {
        if (value.isString()) {
            return new JsonPrimitive(key);
        }
        if (value.isNumber()) {
            return new JsonPrimitive(new JsonPrimitive(key).getAsNumber());
        }
        if (value.isBoolean()) {
            return new JsonPrimitive(new JsonPrimitive(key).getAsBoolean());
        }
        return null;
    }
}
