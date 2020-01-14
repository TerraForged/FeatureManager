package com.terraforged.feature.biome;

import net.minecraft.world.gen.GenerationStage;

import java.util.*;

public class BiomeFeatures {

    public static final BiomeFeatures NONE = BiomeFeatures.builder().build();
    private static final List<BiomeFeature> empty = Collections.emptyList();

    private final Map<GenerationStage.Decoration, List<BiomeFeature>> features;

    public BiomeFeatures(Builder builder) {
        this.features = builder.features;
        builder.features = Collections.emptyMap();
    }

    public List<BiomeFeature> getStage(GenerationStage.Decoration stage) {
        return features.getOrDefault(stage, empty);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Map<GenerationStage.Decoration, List<BiomeFeature>> features = Collections.emptyMap();

        public Builder add(GenerationStage.Decoration stage, BiomeFeature feature) {
            if (features.isEmpty()) {
                features = new EnumMap<>(GenerationStage.Decoration.class);
            }
            features.computeIfAbsent(stage, s -> new ArrayList<>()).add(feature);
            return this;
        }

        public BiomeFeatures build() {
            return new BiomeFeatures(this);
        }
    }
}
