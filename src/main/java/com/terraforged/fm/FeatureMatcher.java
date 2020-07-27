package com.terraforged.fm;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Map;
import java.util.function.Predicate;

public class FeatureMatcher implements Predicate<ConfiguredFeature<?, ?>> {

    private final JsonElement[] rules;

    private FeatureMatcher(JsonElement[] rules) {
        this.rules = rules;
    }

    @Override
    public boolean test(ConfiguredFeature<?, ?> feature) {
        try {
            return test(feature.serialize(JsonOps.INSTANCE).getValue());
        } catch (Throwable t) {
            return false;
        }
    }

    public boolean test(JsonElement element) {
        return test(element, rules, new boolean[rules.length]);
    }

    private static boolean test(JsonElement element, JsonElement[] rules, boolean[] matches) {
        if (element.isJsonObject()) {
            for (Map.Entry<String, JsonElement> e : element.getAsJsonObject().entrySet()) {
                if (test(e.getValue(), rules, matches)) {
                    return true;
                }
            }
        } else if (element.isJsonArray()) {
            for (JsonElement e : element.getAsJsonArray()) {
                if (test(e, rules, matches)) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < rules.length; i++) {
                if (rules[i].isJsonNull() || rules[i].equals(element)) {
                    matches[i] = true;
                }
            }
        }
        return isMatch(matches);
    }

    private static boolean isMatch(boolean[] matches) {
        for (boolean b : matches) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    public static FeatureMatcher of(Object... values) {
        JsonElement[] rules = new JsonElement[values.length];
        for (int i = 0; i < rules.length; i++) {
            rules[i] = toJson(rules[i]);
        }
        return new FeatureMatcher(rules);
    }

    private static JsonElement toJson(Object o) {
        if (o instanceof IForgeRegistryEntry) {
            return new JsonPrimitive(((IForgeRegistryEntry<?>) o).getRegistryName().toString());
        } else if (o instanceof String) {
            return new JsonPrimitive((String) o);
        } else if (o instanceof Long) {
            return new JsonPrimitive((long) o);
        } else if (o instanceof Integer) {
            return new JsonPrimitive((int) o);
        } else if (o instanceof Double) {
            return new JsonPrimitive((double) o);
        } else if (o instanceof Float) {
            return new JsonPrimitive((float) o);
        } else if (o instanceof Boolean) {
            return new JsonPrimitive((boolean) o);
        } else {
            return JsonNull.INSTANCE;
        }
    }
}
