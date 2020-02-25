/*
 *
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

package com.terraforged.feature;

import com.google.gson.JsonElement;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredRandomFeatureList;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.MultipleRandomFeatureConfig;
import net.minecraft.world.gen.feature.MultipleWithChanceRandomFeatureConfig;
import net.minecraft.world.gen.feature.SingleRandomFeature;
import net.minecraft.world.gen.feature.TwoFeatureChoiceConfig;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Optional;

public class FeatureSerializer {

    public static final Marker MARKER = MarkerManager.getMarker("Serializer");

    public static JsonElement serialize(ConfiguredFeature<?, ?> feature) {
        return feature.serialize(JsonOps.INSTANCE).getValue();
    }

    public static ConfiguredFeature<?, ?> deserializeUnchecked(JsonElement element) {
        Dynamic<JsonElement> dynamic = new Dynamic<>(JsonOps.INSTANCE, element);
        return ConfiguredFeature.deserialize(dynamic);
    }

    public static Optional<ConfiguredFeature<?, ?>> deserialize(JsonElement element) {
        try {
            return Optional.of(deserializeUnchecked(element));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

    public static String toString(ConfiguredFeature<?, ?> feature) {
        StringBuilder sb = new StringBuilder(256);
        toString(feature, sb);
        return sb.toString();
    }

    private static void toString(ConfiguredFeature<?, ?> feature, StringBuilder sb) {
        if (feature.config instanceof DecoratedFeatureConfig) {
            decorated((DecoratedFeatureConfig) feature.config, sb);
            return;
        }

        // note SingleRandomFeature & SingleRandomFeatureConfig names a mixed up
        if (feature.config instanceof SingleRandomFeature) {
            single((SingleRandomFeature) feature.config, sb);
            return;
        }

        if (feature.config instanceof TwoFeatureChoiceConfig) {
            twoChoice((TwoFeatureChoiceConfig) feature.config, sb);
            return;
        }

        if (feature.config instanceof MultipleRandomFeatureConfig) {
            multi((MultipleRandomFeatureConfig) feature.config, sb);
            return;
        }

        if (feature.config instanceof MultipleWithChanceRandomFeatureConfig) {
            multiChance((MultipleWithChanceRandomFeatureConfig) feature.config, sb);
            return;
        }

        sb.append(feature.feature.getRegistryName());
        sb.append('(').append(config(feature.config)).append(')');
    }

    private static void decorated(DecoratedFeatureConfig config, StringBuilder sb) {
        sb.append("Decorated{");
        toString(config.feature, sb);
        sb.append("}");
    }

    private static void single(SingleRandomFeature config, StringBuilder sb) {
        sb.append("Single[");
        for (ConfiguredFeature<?, ?> feature : config.features) {
            toString(feature, sb);
        }
        sb.append("]");
    }

    private static void twoChoice(TwoFeatureChoiceConfig config, StringBuilder sb) {
        sb.append("Choice{");
        {
            sb.append("a={");
            toString(config.field_227285_a_, sb);
            sb.append("},b={");
            toString(config.field_227286_b_, sb);
            sb.append("}");
        }
        sb.append("}");
    }

    private static void multi(MultipleRandomFeatureConfig config, StringBuilder sb) {
        sb.append("Multi[");
        int start = sb.length();
        for (ConfiguredRandomFeatureList<?> feature : config.features) {
            comma(sb, start);
            toString(feature.feature, sb);
        }
        sb.append("]");
    }

    private static void multiChance(MultipleWithChanceRandomFeatureConfig config, StringBuilder sb) {
        sb.append("Chance[");
        int start = sb.length();
        for (ConfiguredFeature<?, ?> feature : config.features) {
            comma(sb, start);
            toString(feature, sb);
        }
        sb.append("]");
    }

    private static void comma(StringBuilder sb, int len) {
        if (sb.length() > len) {
            sb.append(',');
        }
    }

    private static String config(IFeatureConfig config) {
        try {
            return config.serialize(JsonOps.INSTANCE).getValue().toString();
        } catch (Throwable t) {
            return config + "";
        }
    }
}
