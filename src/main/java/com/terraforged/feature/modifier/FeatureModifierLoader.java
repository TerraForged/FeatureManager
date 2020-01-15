package com.terraforged.feature.modifier;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.terraforged.feature.FeatureManager;
import com.terraforged.feature.matcher.BiomeFeatureMatcher;
import com.terraforged.feature.matcher.biome.BiomeMatcher;
import com.terraforged.feature.matcher.biome.BiomeMatcherParser;
import com.terraforged.feature.matcher.feature.FeatureMatcher;
import com.terraforged.feature.matcher.feature.FeatureMatcherParser;
import com.terraforged.feature.transformer.FeatureTransformer;
import com.terraforged.feature.transformer.FeatureTransformerParser;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public class FeatureModifierLoader {

    private static final Predicate<String> JSON_MATCHER = s -> s.endsWith(".json");
    private static final JsonParser parser = new JsonParser();

    public static FeatureModifiers load() {
        FeatureModifiers modifiers = new FeatureModifiers();
        IResourceManager manager = getResourceManager();
        Collection<ResourceLocation> locations = manager.getAllResourceLocations("features", JSON_MATCHER);
        for (ResourceLocation location : locations) {
            try (IResource resource = manager.getResource(location)) {
                try (Reader reader = getResourceReader(resource)) {
                    JsonElement element = parser.parse(reader);
                    if (element.isJsonObject() && load(element.getAsJsonObject(), modifiers)) {
                        FeatureManager.LOG.debug(FeatureManager.LOAD, "Loaded feature configuration: {}", location);
                    } else {
                        FeatureManager.LOG.error(FeatureManager.LOAD, "Failed to load feature configuration: {}", location);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return modifiers;
    }

    private static boolean load(JsonObject root, FeatureModifiers modifiers) {
        Optional<BiomeMatcher> biome = BiomeMatcherParser.parse(root);
        if (!biome.isPresent()) {
            FeatureManager.LOG.error(FeatureManager.LOAD, "Invalid BiomeMatcher");
            return false;
        }

        Optional<FeatureMatcher> matcher = FeatureMatcherParser.parse(root);
        if (!matcher.isPresent()) {
            FeatureManager.LOG.error(FeatureManager.LOAD, "Invalid FeatureMatcher");
            return false;
        }

        Optional<FeatureTransformer> transformer = FeatureTransformerParser.parse(root);
        if (!transformer.isPresent()) {
            FeatureManager.LOG.error(FeatureManager.LOAD, "Invalid FeatureTransformer");
            return false;
        }

        BiomeFeatureMatcher biomeFeatureMatcher = new BiomeFeatureMatcher(biome.get(), matcher.get());
        FeatureTransformer featureTransformer = transformer.get();
        modifiers.add(biomeFeatureMatcher, featureTransformer);
        return true;
    }

    private static IResourceManager getResourceManager() {
        MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        return server.getResourceManager();
    }

    private static Reader getResourceReader(IResource resource) {
        return new BufferedReader(new InputStreamReader(resource.getInputStream()));
    }
}
