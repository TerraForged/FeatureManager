package com.terraforged.feature.template;

import com.terraforged.feature.FeatureManager;
import com.terraforged.feature.data.DataHelper;
import com.terraforged.feature.data.DataPack;
import com.terraforged.feature.template.feature.MultiTemplateFeature;
import com.terraforged.feature.template.feature.TemplateFeature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModLoadingContext;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TemplateManager {

    private static final Marker marker = MarkerManager.getMarker("TEMPLATES");

    public static void register(RegistryEvent.Register<Feature<?>> event) {
        String modid = ModLoadingContext.get().getActiveContainer().getModId();
        register(modid, event);
    }

    public static void register(String modid, RegistryEvent.Register<Feature<?>> event) {
        DataPack.getModDataPack(modid).ifPresent(dataPack -> {
            List<TemplateConfig> configs = loadConfigs(dataPack);
            for (TemplateConfig config : configs) {
                FeatureManager.LOG.debug(marker, "Registering feature: {}", config.getRegistryName());
                List<TemplateFeature> templates = loadTemplates(dataPack, config);
                MultiTemplateFeature feature = new MultiTemplateFeature(config.getType(), config.getRegistryName(), templates);
                event.getRegistry().register(feature);
            }
        });
    }

    private static List<TemplateConfig> loadConfigs(DataPack pack) {
        List<TemplateConfig> list = new ArrayList<>();
        pack.iterateJson("templates", (location, element) -> {
            Optional<TemplateConfig> config = TemplateConfig.parse(location, element);
            if (config.isPresent()) {
                list.add(config.get());
                FeatureManager.LOG.debug(marker, " Loaded template config: {}", location);
            } else {
                FeatureManager.LOG.error(marker, " Failed to load template config: {}", location);
            }
        });
        return list;
    }

    private static List<TemplateFeature> loadTemplates(DataPack pack, TemplateConfig config) {
        List<TemplateFeature> list = new ArrayList<>();
        for (ResourceLocation path : config.getPaths()) {
            FeatureManager.LOG.debug(marker, " Loading templates for: {}", config.getRegistryName());
            pack.iterateData(path.getPath(), DataHelper.NBT, (location, data) -> {
                Optional<TemplateFeature> template = TemplateFeature.load(data);
                if (template.isPresent()) {
                    list.add(template.get());
                    FeatureManager.LOG.debug(marker, " Loaded template: {}", location);
                } else {
                    FeatureManager.LOG.error(marker, " Failed to load template: {}", location);
                }
            });
        }
        return list;
    }
}
