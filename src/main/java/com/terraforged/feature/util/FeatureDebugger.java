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
        checkConfiguredFeature(feature, errors);
        return errors;
    }

    private static void checkConfiguredFeature(ConfiguredFeature<?, ?> feature, List<String> errors) {
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

        checkFeature(feature.feature, errors);
        checkConfig(feature.config, errors);
    }

    private static void decorated(DecoratedFeatureConfig config, List<String> errors) {
        checkConfiguredFeature(config.feature, errors);
        checkDecorator(config.decorator, errors);
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

    private static void checkFeature(Feature<?> feature, List<String> list) {
        if (feature == null) {
            list.add("null feature");
        } else if (!ForgeRegistries.FEATURES.containsValue(feature)) {
            list.add("unregistered feature: " + feature.getClass().getName());
        }
    }

    private static void checkConfig(IFeatureConfig config, List<String> list) {
        if (config == null) {
            list.add("null config");
            return;
        }

        try {
            config.serialize(JsonOps.INSTANCE);
        } catch (Throwable t) {
            list.add("config: " + config.getClass().getName() + ", error: " + t.getMessage());
        }
    }

    private static void checkDecorator(ConfiguredPlacement<?> decorator, List<String> list) {
        if (decorator == null) {
            list.add("null configured placement");
            return;
        }

        if (decorator.decorator == null) {
            list.add("null placement");
        } else if (!ForgeRegistries.DECORATORS.containsValue(decorator.decorator)) {
            list.add("unregistered placement: " + decorator.decorator.getClass().getName());
        }

        if (decorator.config == null) {
            list.add("null decorator config");
        } else {
            try {
                decorator.config.serialize(JsonOps.INSTANCE);
            } catch (Throwable t) {
                list.add("placement config: " + decorator.config.getClass().getName() + ", error: " + t.getMessage());
            }
        }
    }
}
