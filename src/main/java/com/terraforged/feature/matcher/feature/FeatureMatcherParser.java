package com.terraforged.feature.matcher.feature;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Optional;

public class FeatureMatcherParser {

    public static Optional<FeatureMatcher> parse(JsonObject root) {
        if (root.has("match")) {
            return parse(root.get("match"));
        }
        return Optional.of(FeatureMatcher.ANY);
    }

    private static Optional<FeatureMatcher> parse(JsonElement element) {
        if (element.isJsonPrimitive()) {
            Object arg = parseArg(element.getAsJsonPrimitive());
            if (arg == null) {
                return Optional.empty();
            }
            return Optional.of(FeatureMatcher.of(arg));
        }

        if (element.isJsonArray()) {
            JsonMatcher.Builder builder = FeatureMatcher.builder();
            for (JsonElement e : element.getAsJsonArray()) {
                if (e.isJsonPrimitive()) {
                    Object arg = parseArg(e.getAsJsonPrimitive());
                    if (arg == null) {
                        return Optional.empty();
                    }
                    builder.or(arg);
                } else if (e.isJsonArray()) {
                    if (!parseRule(e.getAsJsonArray(), builder.newRule())) {
                        return Optional.empty();
                    }
                } else {
                    // invalid syntax
                    return Optional.empty();
                }
            }
            return Optional.of(builder.build());
        }

        return Optional.empty();
    }

    private static boolean parseRule(JsonArray array, JsonMatcher.Builder builder) {
        for (JsonElement element : array) {
            if (!element.isJsonPrimitive()) {
                return false;
            }
            Object arg = parseArg(element.getAsJsonPrimitive());
            if (arg == null) {
                return false;
            }
            builder.and(arg);
        }
        return true;
    }

    private static Object parseArg(JsonPrimitive primitive) {
        if (primitive.isString()) {
            return primitive.getAsString();
        }
        if (primitive.isBoolean()) {
            return primitive.getAsBoolean();
        }
        if (primitive.isNumber()) {
            return primitive.getAsNumber();
        }
        return null;
    }
}
