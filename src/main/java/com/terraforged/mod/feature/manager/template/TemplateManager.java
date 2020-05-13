package com.terraforged.mod.feature.manager.template;

import com.terraforged.mod.feature.manager.FeatureManager;
import com.terraforged.mod.feature.manager.data.DataManager;
import com.terraforged.mod.feature.manager.template.feature.TemplateFeature;
import com.terraforged.mod.feature.manager.template.feature.TemplateFeatureConfig;
import com.terraforged.mod.feature.manager.template.type.FeatureTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModLoadingContext;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.HashMap;
import java.util.Map;

public class TemplateManager {

    private static final Marker marker = MarkerManager.getMarker("TEMPLATES");

    private static final TemplateManager instance = new TemplateManager();

    private final Map<ResourceLocation, TemplateFeatureConfig> templates = new HashMap<>();

    public synchronized TemplateFeatureConfig getTemplateConfig(ResourceLocation name) {
        return templates.getOrDefault(name, TemplateFeatureConfig.NONE);
    }

    public synchronized void load(DataManager dataManager) {
        clear();
        FeatureTypes.clearFeatures();
        TemplateLoader loader = new TemplateLoader(dataManager);
        dataManager.forEachJson("templates", (location, data) -> {
            if (data.isJsonObject()) {
                TemplateFeatureConfig instance = TemplateFeatureConfig.parse(loader, data.getAsJsonObject());
                templates.put(instance.name, instance);
                FeatureManager.LOG.debug(marker, " Loaded template config: {}, size:{}", location, instance.templates.size());
            } else {
                FeatureManager.LOG.error(marker, " Failed to load template config: {}", location);
            }
        });
    }

    public synchronized void clear() {
        templates.clear();
    }

    public static TemplateManager getInstance() {
        return instance;
    }

    public static void register(RegistryEvent.Register<Feature<?>> event) {
        String namespace = ModLoadingContext.get().getActiveNamespace();
        event.getRegistry().register(new TemplateFeature(namespace));
    }
}
