package com.terraforged.feature.modifier;

import com.google.gson.JsonObject;
import com.terraforged.feature.FeatureManager;
import com.terraforged.feature.data.DataHelper;
import com.terraforged.feature.matcher.BiomeFeatureMatcher;
import com.terraforged.feature.matcher.biome.BiomeMatcher;
import com.terraforged.feature.matcher.biome.BiomeMatcherParser;
import com.terraforged.feature.matcher.feature.FeatureMatcher;
import com.terraforged.feature.matcher.feature.FeatureMatcherParser;
import com.terraforged.feature.transformer.FeatureReplacer;
import com.terraforged.feature.transformer.FeatureTransformer;
import com.terraforged.feature.transformer.FeatureTransformerParser;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Optional;

public class FeatureModifierLoader {

    public static final Marker LOAD = MarkerManager.getMarker("MODIFIERS");

    public static FeatureModifiers load() {
        FeatureManager.LOG.debug(LOAD, "Loading feature modifier configs");

        FeatureModifiers modifiers = new FeatureModifiers();
        DataHelper.iterateJson("features", (location, element) -> {
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

        Optional<FeatureReplacer> replacer = FeatureTransformerParser.parseReplacer(root);
        if (replacer.isPresent()) {
            BiomeFeatureMatcher biomeFeatureMatcher = new BiomeFeatureMatcher(biome.get(), matcher.get());
            modifiers.getReplacers().add(biomeFeatureMatcher, replacer.get());
            return true;
        }

        Optional<FeatureTransformer> transformer = FeatureTransformerParser.parseTransformer(root);
        if (transformer.isPresent()) {
            BiomeFeatureMatcher biomeFeatureMatcher = new BiomeFeatureMatcher(biome.get(), matcher.get());
            modifiers.getTransformers().add(biomeFeatureMatcher, transformer.get());
            return true;
        }

        FeatureManager.LOG.error(LOAD, "  Invalid Replacer/Transformer in: {}", location);

        return false;
    }
}
