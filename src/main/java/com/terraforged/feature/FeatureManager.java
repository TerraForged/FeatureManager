package com.terraforged.feature;

import com.terraforged.feature.biome.BiomeFeature;
import com.terraforged.feature.biome.BiomeFeatures;
import com.terraforged.feature.event.FeatureModifierEvent;
import com.terraforged.feature.modifier.FeatureModifierLoader;
import com.terraforged.feature.modifier.FeatureModifiers;
import com.terraforged.feature.template.TemplateManager;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.HashMap;
import java.util.Map;

public class FeatureManager implements FeatureDecorator {

    public static final Logger LOG = LogManager.getLogger("FeatureManager");
    public static final Marker INIT = MarkerManager.getMarker("INIT");

    private final Map<Biome, BiomeFeatures> biomes;

    public FeatureManager(Map<Biome, BiomeFeatures> biomes) {
        this.biomes = biomes;
    }

    @Override
    public FeatureManager getFeatureManager() {
        return this;
    }

    public BiomeFeatures getFeatures(Biome biome) {
        return biomes.getOrDefault(biome, BiomeFeatures.NONE);
    }

    public static FeatureManager create(IWorld world) {
        FeatureModifiers modifiers = FeatureModifierLoader.load();
        return create(world, modifiers);
    }

    public static FeatureManager create(IWorld world, FeatureModifiers modifiers) {
        LOG.debug(INIT, "Initializing FeatureManager");

        LOG.debug(INIT, " Firing init events");
        MinecraftForge.EVENT_BUS.post(new FeatureModifierEvent(world, modifiers));

        int predicates = modifiers.getPredicates().size();
        int replacers = modifiers.getReplacers().size();
        int transformers = modifiers.getTransformers().size();
        LOG.debug(INIT, " Predicates: {}, Replacers: {}, Transformers: {}", predicates, replacers, transformers);

        modifiers.sort();

        LOG.debug(INIT, " Compiling biome feature lists");
        Map<Biome, BiomeFeatures> biomes = new HashMap<>();
        for (Biome biome : ForgeRegistries.BIOMES) {
            BiomeFeatures features = compute(biome, modifiers);
            biomes.put(biome, features);
        }

        LOG.debug(INIT, " Initialization complete");
        return new FeatureManager(biomes);
    }

    public static void registerTemplates(RegistryEvent.Register<Feature<?>> event) {
        TemplateManager.register(event);
    }

    private static BiomeFeatures compute(Biome biome, FeatureModifiers modifiers) {
        BiomeFeatures.Builder builder = BiomeFeatures.builder();
        for (GenerationStage.Decoration stage : GenerationStage.Decoration.values()) {
            for (ConfiguredFeature<?, ?> feature : biome.getFeatures(stage)) {
                BiomeFeature biomeFeature = modifiers.getFeature(biome, feature);
                builder.add(stage, biomeFeature);
            }
        }
        return builder.build();
    }
}
