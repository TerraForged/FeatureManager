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

package com.terraforged.mod.feature.manager.modifier;

import com.google.gson.JsonObject;
import com.terraforged.mod.feature.manager.FeatureManager;
import com.terraforged.mod.feature.manager.data.DataManager;
import com.terraforged.mod.feature.manager.matcher.BiomeFeatureMatcher;
import com.terraforged.mod.feature.manager.matcher.biome.BiomeMatcher;
import com.terraforged.mod.feature.manager.matcher.biome.BiomeMatcherParser;
import com.terraforged.mod.feature.manager.matcher.feature.FeatureMatcher;
import com.terraforged.mod.feature.manager.matcher.feature.FeatureMatcherParser;
import com.terraforged.mod.feature.manager.transformer.FeatureInjector;
import com.terraforged.mod.feature.manager.transformer.FeatureParser;
import com.terraforged.mod.feature.manager.transformer.FeatureReplacer;
import com.terraforged.mod.feature.manager.transformer.FeatureTransformer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Optional;

public class FeatureModifierLoader {

    public static final Marker LOAD = MarkerManager.getMarker("MODIFIERS");

    public static FeatureModifiers load(DataManager data) {
        FeatureManager.LOG.debug(LOAD, "Loading feature modifier configs");
        FeatureModifiers modifiers = new FeatureModifiers();
        data.forEachJson("features", (location, element) -> {
            if (element.isJsonObject()) {
                if (load(location, element.getAsJsonObject(), modifiers)) {
                    FeatureManager.LOG.debug(LOAD, " Loaded modifier config: {}", location);
                    return;
                }
            }
            FeatureManager.LOG.error(LOAD, " Failed to load modifier config: {}", location);
        });
        return modifiers;
    }

    private static boolean load(ResourceLocation location, JsonObject root, FeatureModifiers modifiers) {
        Optional<BiomeMatcher> biome = BiomeMatcherParser.parse(root);
        if (!biome.isPresent()) {
            FeatureManager.LOG.error(LOAD, "  Invalid BiomeMatcher in: {}", location);
            return false;
        }

        Optional<FeatureMatcher> matcher = FeatureMatcherParser.parse(root);
        if (!matcher.isPresent()) {
            FeatureManager.LOG.error(LOAD, "  Invalid FeatureMatcher in: {}", location);
            return false;
        }

        Optional<FeatureReplacer> replacer = FeatureParser.parseReplacer(root);
        if (replacer.isPresent()) {
            BiomeFeatureMatcher biomeFeatureMatcher = new BiomeFeatureMatcher(biome.get(), matcher.get());
            modifiers.getReplacers().add(biomeFeatureMatcher, replacer.get());
            return true;
        }

        Optional<FeatureTransformer> transformer = FeatureParser.parseTransformer(root);
        if (transformer.isPresent()) {
            BiomeFeatureMatcher biomeFeatureMatcher = new BiomeFeatureMatcher(biome.get(), matcher.get());
            modifiers.getTransformers().add(biomeFeatureMatcher, transformer.get());
            return true;
        }

        Optional<FeatureInjector> injector = FeatureParser.parseInjector(root);
        if (injector.isPresent()) {
            BiomeFeatureMatcher biomeFeatureMatcher = new BiomeFeatureMatcher(biome.get(), matcher.get());
            modifiers.getInjectors().add(biomeFeatureMatcher, injector.get());
            return true;
        }

        FeatureManager.LOG.error(LOAD, "  Invalid Replacer/Transformer in: {}", location);

        return false;
    }
}
