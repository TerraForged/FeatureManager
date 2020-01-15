package com.terraforged.feature.event;

import com.terraforged.feature.matcher.biome.BiomeMatcher;
import com.terraforged.feature.matcher.feature.FeatureMatcher;
import com.terraforged.feature.modifier.ModifierList;
import com.terraforged.feature.predicate.FeaturePredicate;
import com.terraforged.feature.transformer.FeatureReplacer;
import com.terraforged.feature.transformer.FeatureTransformer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.eventbus.api.Event;

public abstract class FeatureInitEvent<T> extends Event {

    private final WorldType worldType;
    private final ModifierList<T> list;

    public FeatureInitEvent(WorldType worldType, ModifierList<T> list) {
        this.worldType = worldType;
        this.list = list;
    }

    public WorldType getWorldType() {
        return worldType;
    }

    public void register(FeatureMatcher matcher, T modifier) {
        list.add(matcher, modifier);
    }

    public void register(Biome biome, FeatureMatcher matcher, T modifier) {
        list.add(BiomeMatcher.of(biome), matcher, modifier);
    }

    public void register(BiomeMatcher biomeMatcher, FeatureMatcher matcher, T modifier) {
        list.add(biomeMatcher, matcher, modifier);
    }

    public static class Predicate extends FeatureInitEvent<FeaturePredicate> {

        public Predicate(WorldType worldType, ModifierList<FeaturePredicate> list) {
            super(worldType, list);
        }
    }

    public static class Replacer extends FeatureInitEvent<FeatureReplacer> {

        public Replacer(WorldType worldType, ModifierList<FeatureReplacer> list) {
            super(worldType, list);
        }
    }

    public static class Transformer extends FeatureInitEvent<FeatureTransformer> {

        public Transformer(WorldType worldType, ModifierList<FeatureTransformer> list) {
            super(worldType, list);
        }
    }
}
