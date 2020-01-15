package com.terraforged.feature.transformer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FeatureTransformer implements Function<JsonElement, JsonElement> {

    public static final FeatureTransformer NONE = FeatureTransformer.builder().build();

    private final boolean hasTransformations;
    private final Map<String, JsonElement> keyTransformers;
    private final Map<JsonPrimitive, JsonPrimitive> valueTransformers;

    private FeatureTransformer(Builder builder) {
        this.keyTransformers = builder.keyTransformers;
        this.valueTransformers = builder.valueTransformers;
        this.hasTransformations = !keyTransformers.isEmpty() || !valueTransformers.isEmpty();
        builder.keyTransformers = Collections.emptyMap();
        builder.valueTransformers = Collections.emptyMap();
    }

    @Override
    public JsonElement apply(JsonElement element) {
        if (hasTransformations) {
            if (element.isJsonArray()) {
                return transformArray(element.getAsJsonArray());
            }
            if (element.isJsonObject()) {
                return transformObject(element.getAsJsonObject());
            }
            if (element.isJsonPrimitive()) {
                return transformValue(element.getAsJsonPrimitive());
            }
        }
        return element;
    }

    private JsonPrimitive transformValue(JsonPrimitive primitive) {
        return valueTransformers.getOrDefault(primitive, primitive);
    }

    private JsonArray transformArray(JsonArray source) {
        JsonArray dest = new JsonArray();
        for (JsonElement element : source) {
            dest.add(apply(element));
        }
        return dest;
    }

    private JsonObject transformObject(JsonObject source) {
        JsonObject dest = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : source.entrySet()) {
            JsonElement result = transformEntry(entry.getKey(), entry.getValue());
            dest.add(entry.getKey(), result);
        }
        return dest;
    }

    private JsonElement transformEntry(String key, JsonElement value) {
        JsonElement keyResult = keyTransformers.get(key);
        if (keyResult != null) {
            return keyResult;
        }
        return apply(value);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static <T> FeatureTransformer replace(T find, T replace) {
        if (find instanceof String) {
            return builder().value((String) find, (String) replace).build();
        }
        if (find instanceof Number) {
            return builder().value((Number) find, (Number) replace).build();
        }
        if (find instanceof Boolean) {
            return builder().value((Boolean) find, (Boolean) replace).build();
        }
        return NONE;
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

        public FeatureTransformer build() {
            return new FeatureTransformer(this);
        }
    }
}
