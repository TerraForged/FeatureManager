package com.terraforged.feature.event;

import com.terraforged.feature.matcher.FeatureMatcher;
import com.terraforged.feature.modifier.FeatureModifiers;
import com.terraforged.feature.predicate.FeaturePredicate;
import com.terraforged.feature.transformer.FeatureTransformer;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.eventbus.api.Event;

public class FeatureInitEvent extends Event {

    private final WorldType worldType;
    protected final FeatureModifiers modifiers;

    public FeatureInitEvent(WorldType worldType, FeatureModifiers modifiers) {
        this.worldType = worldType;
        this.modifiers = modifiers;
    }

    public WorldType getWorldType() {
        return worldType;
    }

    public static class Predicate extends FeatureInitEvent {

        public Predicate(WorldType worldType, FeatureModifiers modifiers) {
            super(worldType, modifiers);
        }

        public void register(FeatureMatcher matcher, FeaturePredicate predicate) {
            modifiers.add(matcher, predicate);
        }

        public void register(GenerationStage.Decoration stage, FeatureMatcher matcher, FeaturePredicate predicate) {
            modifiers.add(stage, matcher, predicate);
        }
    }

    public static class Transformer extends FeatureInitEvent {

        public Transformer(WorldType worldType, FeatureModifiers modifiers) {
            super(worldType, modifiers);
        }

        public void register(FeatureMatcher matcher, FeatureTransformer transformer) {
            modifiers.add(matcher, transformer);
        }

        public void register(GenerationStage.Decoration stage, FeatureMatcher matcher, FeatureTransformer transformer) {
            modifiers.add(stage, matcher, transformer);
        }
    }
}
