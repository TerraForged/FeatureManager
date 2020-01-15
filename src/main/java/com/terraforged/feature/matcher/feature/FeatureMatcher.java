package com.terraforged.feature.matcher.feature;

import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.function.Predicate;

public interface FeatureMatcher extends Predicate<ConfiguredFeature<?, ?>> {

    FeatureMatcher ANY = c -> true;

    @Override
    boolean test(ConfiguredFeature<?, ?> feature);

    /**
     * Create a FeatureMatcher that matches ConfiguredFeatures that contain the provided arg
     */
    static FeatureMatcher of(Object arg) {
        return and(arg);
    }

    /**
     * Create a FeatureMatcher that matches ConfiguredFeatures that contain ANY of the provided args
     */
    static FeatureMatcher or(Object... args) {
        if (args.length == 0) {
            return FeatureMatcher.ANY;
        }
        JsonMatcher.Builder builder = builder();
        for (Object o : args) {
            builder.or(o);
        }
        return builder.build();
    }

    /**
     * Create a FeatureMatcher that matches ConfiguredFeatures that contain ALL of the provided args
     */
    static FeatureMatcher and(Object... args) {
        if (args.length == 0) {
            return FeatureMatcher.ANY;
        }
        JsonMatcher.Builder builder = builder();
        for (Object arg : args) {
            builder.and(arg);
        }
        return builder.build();
    }

    static JsonMatcher.Builder builder() {
        return JsonMatcher.builder();
    }
}
