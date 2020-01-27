package com.terraforged.feature.template.decorator;

import com.google.gson.JsonElement;
import com.terraforged.feature.template.type.TypedFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Optional;

public interface DecoratorFactory {

    DecoratorFactory NONE = new DecoratorFactory() {
        @Override
        public <T extends Feature<NoFeatureConfig> & TypedFeature> Optional<DecoratedFeature<?, ?>> apply(T feature, JsonElement decorators) {
            return Optional.empty();
        }
    };

    <T extends Feature<NoFeatureConfig> & TypedFeature> Optional<DecoratedFeature<?, ?>> apply(T feature, JsonElement decorators);
}
