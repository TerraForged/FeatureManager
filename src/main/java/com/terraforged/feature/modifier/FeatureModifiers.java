/*
 *
 * MIT License
 *
 * Copyright (c) 2020 TerraForged
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.terraforged.feature.modifier;

import com.google.gson.JsonElement;
import com.terraforged.feature.FeatureManager;
import com.terraforged.feature.FeatureSerializer;
import com.terraforged.feature.biome.BiomeFeature;
import com.terraforged.feature.matcher.dynamic.DynamicList;
import com.terraforged.feature.matcher.dynamic.DynamicPredicate;
import com.terraforged.feature.predicate.FeaturePredicate;
import com.terraforged.feature.transformer.FeatureInserter;
import com.terraforged.feature.transformer.FeatureReplacer;
import com.terraforged.feature.transformer.FeatureTransformer;
import com.terraforged.feature.util.FeatureDebugger;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeatureModifiers extends Event {

    private final DynamicList dynamics = new DynamicList();
    private final ModifierList<FeatureReplacer> replacers = new ModifierList<>();
    private final ModifierList<FeatureInserter> inserters = new ModifierList<>();
    private final ModifierList<FeaturePredicate> predicates = new ModifierList<>();
    private final ModifierList<FeatureTransformer> transformers = new ModifierList<>();

    public DynamicList getDynamic() {
        return dynamics;
    }

    public ModifierList<FeatureReplacer> getReplacers() {
        return replacers;
    }

    public ModifierList<FeatureInserter> getInserters() {
        return inserters;
    }

    public ModifierList<FeaturePredicate> getPredicates() {
        return predicates;
    }

    public ModifierList<FeatureTransformer> getTransformers() {
        return transformers;
    }

    public void sort() {
        replacers.sort();
        predicates.sort();
        transformers.sort();
    }

    public ModifierSet getFeature(Biome biome, ConfiguredFeature<?, ?> feature) {
        try {
            JsonElement element = FeatureSerializer.serialize(feature);
            List<BiomeFeature> before = getInserts(biome, feature, element, FeatureInserter.Type.BEFORE);
            List<BiomeFeature> after = getInserts(biome, feature, element, FeatureInserter.Type.AFTER);

            ConfiguredFeature<?, ?> result = getFeature(biome, feature, element);
            if (result != feature) {
                // re-serialize if feature has been changed
                element = FeatureSerializer.serialize(result);
            }

            FeaturePredicate predicate = getPredicate(result);
            if (predicate == null) {
                predicate = getPredicate(biome, element);
            }

            return new ModifierSet(new BiomeFeature(predicate, result), before, after);
        } catch (Throwable t) {
            String name = biome.getRegistryName() + "";
            List<String> errors = FeatureDebugger.getErrors(feature);
            FeatureManager.LOG.debug(FeatureSerializer.MARKER, "Unable to serialize feature in biome: {}", name);
            if (errors.isEmpty()) {
                FeatureManager.LOG.debug("Unable to determine issues. See stacktrace:", t);
            } else {
                for (String error : errors) {
                    FeatureManager.LOG.debug(FeatureSerializer.MARKER, " - {}", error);
                }
            }
        }
        return new ModifierSet(new BiomeFeature(FeaturePredicate.ALLOW, feature));
    }

    private ConfiguredFeature<?, ?> getFeature(Biome biome, ConfiguredFeature<?, ?> feature, JsonElement element) {
        for (Modifier<FeatureReplacer> modifier : replacers) {
            if (modifier.getMatcher().test(biome, element)) {
                return modifier.getModifier().get();
            }
        }

        boolean modified = false;
        for (Modifier<FeatureTransformer> modifier : transformers) {
            if (modifier.getMatcher().test(biome, element)) {
                modified = true;
                element = modifier.getModifier().apply(element);
            }
        }

        if (!modified) {
            return feature;
        }

        try {
            return FeatureSerializer.deserializeUnchecked(element);
        } catch (Throwable t) {
            FeatureManager.LOG.warn(FeatureSerializer.MARKER, "Unable to deserialize biome feature: {}", biome);
            t.printStackTrace();
            return feature;
        }
    }

    private List<BiomeFeature> getInserts(Biome biome, ConfiguredFeature<?, ?> feature, JsonElement element, FeatureInserter.Type type) {
        List<BiomeFeature> result = Collections.emptyList();
        for (Modifier<FeatureInserter> modifier : getInserters()) {
            if (modifier.getModifier().getType() != type) {
                continue;
            }
            if (modifier.getMatcher().test(biome, element)) {
                FeaturePredicate predicate = getPredicate(feature);
                if (predicate == null) {
                    predicate = getPredicate(biome, element);
                }
                if (result.isEmpty()) {
                    result = new ArrayList<>();
                }
                result.add(new BiomeFeature(predicate, modifier.getModifier().getFeature()));
            }
        }
        return result;
    }

    private FeaturePredicate getPredicate(ConfiguredFeature<?, ?> feature) {
        for (DynamicPredicate predicate : dynamics) {
            if (predicate.getMatcher().test(feature)) {
                return predicate.getPredicate();
            }
        }
        return null;
    }

    private FeaturePredicate getPredicate(Biome biome, JsonElement element) {
        for (Modifier<FeaturePredicate> modifier : predicates) {
            if (modifier.getMatcher().test(biome, element)) {
                return modifier.getModifier();
            }
        }
        return FeaturePredicate.ALLOW;
    }
}
