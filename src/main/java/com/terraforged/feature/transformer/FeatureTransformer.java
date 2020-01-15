package com.terraforged.feature.transformer;

import com.terraforged.feature.transformer.json.JsonTransformer;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.function.Function;

public interface FeatureTransformer extends Function<ConfiguredFeature<?, ?>, ConfiguredFeature<?, ?>> {

    FeatureTransformer NONE = FeatureTransformer.builder().build();

    @Override
    ConfiguredFeature<?, ?> apply(ConfiguredFeature<?, ?> feature);

    static FeatureTransformer of(ConfiguredFeature<?, ?> feature) {
        return f -> feature;
    }

    static <T extends IFeatureConfig> FeatureTransformer of(Feature<T> feature, T config) {
        return of(feature.func_225566_b_(config));
    }

    static <T> FeatureTransformer replace(T find, T replace) {
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

    static JsonTransformer.Builder builder() {
        return JsonTransformer.builder();
    }
}
