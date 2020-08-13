/*
 * MIT License
 *
 * Copyright (c) 2020 TerraForged
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.terraforged.fm;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
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
            return test(FeatureSerializer.serialize(feature));
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
