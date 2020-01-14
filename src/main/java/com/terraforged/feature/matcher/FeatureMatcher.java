package com.terraforged.feature.matcher;

import com.terraforged.feature.matcher.json.JsonMatcher;
import com.terraforged.feature.transformer.FeatureTransformer;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public interface FeatureMatcher {

    FeatureMatcher ANY = c -> true;

    boolean test(ConfiguredFeature<?, ?> feature);

    default FeatureTransformer replace(ConfiguredFeature<?, ?> replacement) {
        return feature -> {
            if (test(feature)) {
                return replacement;
            }
            return feature;
        };
    }

    static FeatureMatcher of(Object... args) {
        JsonMatcher.Builder builder = builder();
        for (Object arg : args) {
            if (arg instanceof Boolean) {
                builder.and((Boolean) arg);
            } else if (arg instanceof Number) {
                builder.and((Number) arg);
            } else if (arg != null) {
                builder.and(arg.toString());
            }
        }
        return builder.build();
    }

    static JsonMatcher.Builder builder() {
        return JsonMatcher.builder();
    }
}
