package com.terraforged.fm.transformer;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class FeatureAppender extends FeatureInjector {

    private final GenerationStage.Decoration stage;

    public FeatureAppender(ConfiguredFeature<?, ?> feature, InjectionPosition type, GenerationStage.Decoration stage) {
        super(feature, type);
        this.stage = stage;
    }

    public GenerationStage.Decoration getStage() {
        return stage;
    }
}
