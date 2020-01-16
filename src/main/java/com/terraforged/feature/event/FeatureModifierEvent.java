package com.terraforged.feature.event;

import com.terraforged.feature.modifier.FeatureModifiers;
import com.terraforged.feature.modifier.ModifierList;
import com.terraforged.feature.predicate.FeaturePredicate;
import com.terraforged.feature.transformer.FeatureReplacer;
import com.terraforged.feature.transformer.FeatureTransformer;
import net.minecraft.world.IWorld;
import net.minecraft.world.WorldType;
import net.minecraftforge.eventbus.api.Event;

public class FeatureModifierEvent extends Event {

    private final IWorld world;
    private final FeatureModifiers modifiers;

    public FeatureModifierEvent(IWorld world, FeatureModifiers modifiers) {
        this.world = world;
        this.modifiers = modifiers;
    }

    public IWorld getWorld() {
        return world;
    }

    public WorldType getWorldType() {
        return getWorld().getWorld().getWorldType();
    }

    public ModifierList<FeatureReplacer> getReplacers() {
        return modifiers.getReplacers();
    }

    public ModifierList<FeaturePredicate> getPredicates() {
        return modifiers.getPredicates();
    }

    public ModifierList<FeatureTransformer> getTransformers() {
        return modifiers.getTransformers();
    }
}
