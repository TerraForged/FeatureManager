package com.terraforged.feature.matcher.dynamic;

import net.minecraft.world.gen.feature.*;

import java.util.function.Predicate;

public class DynamicMatcher implements Predicate<ConfiguredFeature<?, ?>> {

    public static final DynamicMatcher NONE = DynamicMatcher.of(f -> false);

    private final Predicate<ConfiguredFeature<?, ?>> predicate;

    private DynamicMatcher(Predicate<ConfiguredFeature<?, ?>> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(ConfiguredFeature<?, ?> feature) {
        if (feature.config instanceof DecoratedFeatureConfig) {
            return decorated((DecoratedFeatureConfig) feature.config);
        }

        // note SingleRandomFeature & SingleRandomFeatureConfig names a mixed up
        if (feature.config instanceof SingleRandomFeature) {
            return single((SingleRandomFeature) feature.config);
        }

        if (feature.config instanceof TwoFeatureChoiceConfig) {
            return twoChoice((TwoFeatureChoiceConfig) feature.config);
        }

        if (feature.config instanceof MultipleRandomFeatureConfig) {
            return multi((MultipleRandomFeatureConfig) feature.config);
        }

        if (feature.config instanceof MultipleWithChanceRandomFeatureConfig) {
            return multiChance((MultipleWithChanceRandomFeatureConfig) feature.config);
        }

        return predicate.test(feature);
    }

    private boolean decorated(DecoratedFeatureConfig config) {
        return test(config.feature);
    }

    private boolean single(SingleRandomFeature config) {
        for (ConfiguredFeature<?, ?> feature : config.features) {
            if (test(feature)) {
                return true;
            }
        }
        return false;
    }

    private boolean twoChoice(TwoFeatureChoiceConfig config) {
        if (test(config.field_227285_a_)) {
            return true;
        }
        return test(config.field_227286_b_);
    }

    private boolean multi(MultipleRandomFeatureConfig config) {
        for (ConfiguredRandomFeatureList<?> feature : config.features) {
            if (test(feature.feature)) {
                return true;
            }
        }
        return false;
    }

    private boolean multiChance(MultipleWithChanceRandomFeatureConfig config) {
        for (ConfiguredFeature<?, ?> feature : config.features) {
            if (test(feature)) {
                return true;
            }
        }
        return false;
    }

    public static DynamicMatcher of(Predicate<ConfiguredFeature<?, ?>> predicate) {
        return new DynamicMatcher(predicate);
    }

    public static DynamicMatcher feature(Predicate<Feature<?>> predicate) {
        return DynamicMatcher.of(f -> predicate.test(f.feature));
    }

    public static DynamicMatcher feature(Feature<?> feature) {
        return DynamicMatcher.feature(f -> f == feature);
    }

    public static DynamicMatcher feature(Class<? extends Feature> type) {
        return DynamicMatcher.feature(type::isInstance);
    }

    public static DynamicMatcher config(Predicate<IFeatureConfig> predicate) {
        return DynamicMatcher.of(f -> predicate.test(f.config));
    }

    public static DynamicMatcher config(IFeatureConfig config) {
        return DynamicMatcher.of(f -> f.config == config);
    }

    public static DynamicMatcher config(Class<? extends IFeatureConfig> type) {
        return DynamicMatcher.config(type::isInstance);
    }
}
