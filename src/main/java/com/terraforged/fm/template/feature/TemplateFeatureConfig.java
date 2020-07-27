package com.terraforged.fm.template.feature;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import com.terraforged.fm.template.type.FeatureType;
import com.terraforged.fm.template.PasteConfig;
import com.terraforged.fm.template.Template;
import com.terraforged.fm.template.TemplateLoader;
import com.terraforged.fm.template.TemplateManager;
import com.terraforged.fm.template.decorator.DecoratorConfig;
import com.terraforged.fm.template.type.FeatureTypes;
import com.terraforged.fm.util.Json;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Collections;
import java.util.List;

public class TemplateFeatureConfig implements IFeatureConfig {

    public static final TemplateFeatureConfig NONE = new TemplateFeatureConfig();

    public final ResourceLocation name;
    public final FeatureType type;
    public final PasteConfig paste;
    public final List<Template> templates;
    public final DecoratorConfig<?> decorator;

    private TemplateFeatureConfig() {
        name = new ResourceLocation("fm", "none");
        type = FeatureTypes.ANY;
        paste = PasteConfig.DEFAULT;
        templates = Collections.emptyList();
        decorator = null;
    }

    public TemplateFeatureConfig(ResourceLocation name, FeatureType type, PasteConfig paste, List<Template> templates, DecoratorConfig<?> decorator) {
        this.name = name;
        this.type = type;
        this.paste = paste;
        this.templates = templates;
        this.decorator = decorator;
        type.register(name);
    }

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(
                ops.createString("template"), ops.createString(name + "")
        )));
    }

    public static TemplateFeatureConfig deserialize(Dynamic<?> dynamic) {
        ResourceLocation name = new ResourceLocation(dynamic.get("template").asString(""));
        return TemplateManager.getInstance().getTemplateConfig(name);
    }

    public static TemplateFeatureConfig parse(TemplateLoader loader, JsonObject root) {
        ResourceLocation name = new ResourceLocation(Json.getString("name", root, ""));
        FeatureType type = FeatureTypes.getType(Json.getString("type", root, ""));
        PasteConfig paste = PasteConfig.parse(root.getAsJsonObject("config"));
        DecoratorConfig<?> decorator = DecoratorConfig.parse(type.getDecorator(), root.getAsJsonObject("decorators"));
        List<Template> templates = loader.load(name.getNamespace(), root.getAsJsonArray("paths"));
        return new TemplateFeatureConfig(name, type, paste, templates, decorator);
    }
}
