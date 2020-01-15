package com.terraforged.feature.event;

import com.terraforged.feature.matcher.feature.FeatureMatcher;
import com.terraforged.feature.modifier.FeatureModifiers;
import com.terraforged.feature.predicate.FeaturePredicate;
import com.terraforged.feature.transformer.FeatureTransformer;
import net.minecraft.world.WorldType;
import net.minecraftforge.eventbus.api.Event;

public abstract class FeatureInitEvent extends Event {

    private final WorldType worldType;
    private final FeatureModifiers modifiers;

    FeatureInitEvent(WorldType worldType, FeatureModifiers modifiers) {
        this.worldType = worldType;
        this.modifiers = modifiers;
    }

    public WorldType getWorldType() {
        return worldType;
    }

    FeatureModifiers getModifiers() {
        return modifiers;
    }

    /**
     * Used to register FeaturePredicates to the FeatureManager
     */
    public static class Predicate extends FeatureInitEvent {

        public Predicate(WorldType worldType, FeatureModifiers modifiers) {
            super(worldType, modifiers);
        }

        public void register(FeatureMatcher matcher, FeaturePredicate predicate) {
            getModifiers().add(matcher, predicate);
        }
    }

    /**
     * Used to register FeatureTransformers to the FeatureManager
     */
    public static class Transformer extends FeatureInitEvent {

        public Transformer(WorldType worldType, FeatureModifiers modifiers) {
            super(worldType, modifiers);
        }

        public void register(FeatureMatcher matcher, FeatureTransformer transformer) {
            getModifiers().add(matcher, transformer);
        }
    }
}
