package com.terraforged.feature.util;

import com.google.gson.JsonElement;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredRandomFeatureList;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.MultipleRandomFeatureConfig;
import net.minecraft.world.gen.feature.MultipleWithChanceRandomFeatureConfig;
import net.minecraft.world.gen.feature.SingleRandomFeature;
import net.minecraft.world.gen.feature.TwoFeatureChoiceConfig;

public class FeatureException extends RuntimeException {

    public FeatureException(ConfiguredFeature<?, ?> feature, Throwable t) {
        super(feature(feature), t);
    }

    public FeatureException(JsonElement element, Throwable t) {
        super(element(element), t);
    }

    public static String getName(ConfiguredFeature<?, ?> feature) {
        StringBuilder sb = new StringBuilder(256);
        toString(feature, sb);
        return sb.toString();
    }

    private static String feature(ConfiguredFeature<?, ?> feature) {
        StringBuilder sb = new StringBuilder(256);
        toString(feature, sb);
        return String.format("Failed to serialize Feature: %s", sb.toString());
    }

    private static String element(JsonElement element) {
        return String.format("Failed to deserialize Feature data: %s", element);
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
