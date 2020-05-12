package com.terraforged.feature.util;

import com.mojang.datafixers.types.JsonOps;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredRandomFeatureList;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.MultipleRandomFeatureConfig;
import net.minecraft.world.gen.feature.MultipleWithChanceRandomFeatureConfig;
import net.minecraft.world.gen.feature.SingleRandomFeature;
import net.minecraft.world.gen.feature.TwoFeatureChoiceConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class FeatureDebugger {

    public static List<String> getErrors(ConfiguredFeature<?, ?> feature) {
        List<String> errors = new ArrayList<>();
        try {
            checkConfiguredFeature(feature, errors);
        } catch (Throwable t) {
            errors.add(
                    "Exception thrown whilst debugging biome feature: " + feature + " (this is bad)"
                    + "\nError message:"
                    + "\n" + t.getMessage()
            );
        }
        return errors;
    }

    private static void checkConfiguredFeature(ConfiguredFeature<?, ?> feature, List<String> errors) {
        if (!isValid(feature, errors)) {
            return;
        }

        if (feature.config instanceof DecoratedFeatureConfig) {
            decorated((DecoratedFeatureConfig) feature.config, errors);
            return;
        }

        // note SingleRandomFeature & SingleRandomFeatureConfig names a mixed up
        if (feature.config instanceof SingleRandomFeature) {
            single((SingleRandomFeature) feature.config, errors);
            return;
        }

        if (feature.config instanceof TwoFeatureChoiceConfig) {
            twoChoice((TwoFeatureChoiceConfig) feature.config, errors);
            return;
        }

        if (feature.config instanceof MultipleRandomFeatureConfig) {
            multi((MultipleRandomFeatureConfig) feature.config, errors);
            return;
        }

        if (feature.config instanceof MultipleWithChanceRandomFeatureConfig) {
            multiChance((MultipleWithChanceRandomFeatureConfig) feature.config, errors);
            return;
        }
    }

    private static void decorated(DecoratedFeatureConfig config, List<String> errors) {
        checkConfiguredFeature(config.feature, errors);
        isValid(config.decorator, errors);
    }

    private static void single(SingleRandomFeature config, List<String> errors) {
        for (ConfiguredFeature<?, ?> feature : config.features) {
            checkConfiguredFeature(feature, errors);
        }
    }

    private static void twoChoice(TwoFeatureChoiceConfig config, List<String> errors) {
        checkConfiguredFeature(config.field_227285_a_, errors);
        checkConfiguredFeature(config.field_227286_b_, errors);
    }

    private static void multi(MultipleRandomFeatureConfig config, List<String> errors) {
        for (ConfiguredRandomFeatureList<?> feature : config.features) {
            checkConfiguredFeature(feature.feature, errors);
        }
    }

    private static void multiChance(MultipleWithChanceRandomFeatureConfig config, List<String> errors) {
        for (ConfiguredFeature<?, ?> feature : config.features) {
            checkConfiguredFeature(feature, errors);
        }
    }

    private static boolean isValid(ConfiguredFeature<?, ?> feature, List<String> list) {
        if (feature == null) {
            list.add("null configured feature (this is bad! D: )");
            return false;
        }
        return isValid(feature.feature, list) && isValid(feature.config, list);
    }

    private static boolean isValid(Feature<?> feature, List<String> list) {
        if (feature == null) {
            list.add("null feature");
            return false;
        } else if (!ForgeRegistries.FEATURES.containsValue(feature)) {
            list.add("unregistered feature: " + feature.getClass().getName());
            return false;
        }
        return true;
    }

    private static boolean isValid(IFeatureConfig config, List<String> list) {
        if (config == null) {
            list.add("null config");
            return false;
        }

        try {
            config.serialize(JsonOps.INSTANCE);
            return true;
        } catch (Throwable t) {
            list.add("config: " + config.getClass().getName() + ", error: " + t.getMessage());
            return false;
        }
    }

    private static boolean isValid(ConfiguredPlacement<?> decorator, List<String> list) {
        if (decorator == null) {
            list.add("null configured placement");
            return false;
        }

        boolean valid = true;
        if (decorator.decorator == null) {
            list.add("null placement");
            valid = false;
        } else if (!ForgeRegistries.DECORATORS.containsValue(decorator.decorator)) {
            list.add("unregistered placement: " + decorator.decorator.getClass().getName());
            valid = false;
        }

        if (decorator.config == null) {
            list.add("null decorator config");
            valid = false;
        } else {
            try {
                decorator.config.serialize(JsonOps.INSTANCE);
            } catch (Throwable t) {
                valid = false;
                list.add("placement config: " + decorator.config.getClass().getName() + ", error: " + t.getMessage());
            }
        }

        return valid;
    }
}
