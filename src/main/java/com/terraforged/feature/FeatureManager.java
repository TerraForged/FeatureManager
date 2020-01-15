package com.terraforged.feature;

import com.terraforged.feature.biome.BiomeFeature;
import com.terraforged.feature.biome.BiomeFeatures;
import com.terraforged.feature.event.FeatureInitEvent;
import com.terraforged.feature.modifier.FeatureModifierLoader;
import com.terraforged.feature.modifier.FeatureModifiers;
import com.terraforged.feature.predicate.FeaturePredicate;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.common.MinecraftForge;
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
    public static final Marker LOAD = MarkerManager.getMarker("LOAD");

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

    public static FeatureManager create(WorldType worldType) {
        LOG.debug(INIT, "Creating FeatureManager for WorldType: {}", worldType.getName());

        LOG.debug(LOAD, "Loading feature configuration data");
        FeatureModifiers modifiers = FeatureModifierLoader.load();

        LOG.debug(INIT, "Firing FeatureInitEvents");
        MinecraftForge.EVENT_BUS.post(new FeatureInitEvent.Predicate(worldType, modifiers));
        MinecraftForge.EVENT_BUS.post(new FeatureInitEvent.Transformer(worldType, modifiers));

        int predicates = modifiers.getPredicates().size();
        int transformers = modifiers.getTransformers().size();
        LOG.debug(INIT, "Predicates: {}, Transformers: {}", predicates, transformers);

        LOG.debug(INIT, "Compiling biome feature lists");
        Map<Biome, BiomeFeatures> biomes = new HashMap<>();
        for (Biome biome : ForgeRegistries.BIOMES) {
            biomes.put(biome, compute(biome, modifiers));
        }

        return new FeatureManager(biomes);
    }

    private static BiomeFeatures compute(Biome biome, FeatureModifiers modifiers) {
        BiomeFeatures.Builder builder = BiomeFeatures.builder();
        for (GenerationStage.Decoration stage : GenerationStage.Decoration.values()) {
            for (ConfiguredFeature<?, ?> feature : biome.getFeatures(stage)) {
                FeaturePredicate predicate = modifiers.getPredicate(biome, feature);
                ConfiguredFeature<?, ?> result = modifiers.getFeature(biome, feature);
                builder.add(stage, new BiomeFeature(predicate, result));
            }
        }
        return builder.build();
    }
}
