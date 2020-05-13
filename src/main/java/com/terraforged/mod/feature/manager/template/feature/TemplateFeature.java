package com.terraforged.mod.feature.manager.template.feature;

import com.terraforged.mod.feature.manager.FeatureManager;
import com.terraforged.mod.feature.manager.template.Template;
import com.terraforged.mod.feature.manager.template.decorator.Decorator;
import com.terraforged.mod.feature.manager.template.decorator.DecoratorConfig;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;

import java.util.List;
import java.util.Random;

public class TemplateFeature extends Feature<TemplateFeatureConfig> {

    public TemplateFeature(String namespace) {
        super(TemplateFeatureConfig::deserialize);
        setRegistryName(namespace, "template");
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, TemplateFeatureConfig config) {
        if (config.type.getPlacement().canPlaceAt(world, pos)) {
            return paste(world, rand, pos, config, config.decorator);
        }
        return false;
    }

    public static  <T extends IWorld> boolean paste(IWorld world, Random rand, BlockPos pos, TemplateFeatureConfig config, DecoratorConfig<T> decorator) {
        Mirror mirror = nextMirror(rand);
        Rotation rotation = nextRotation(rand);
        return paste(world, rand, pos, mirror, rotation, config, decorator);
    }

    public static  <T extends IWorld> boolean paste(IWorld world, Random rand, BlockPos pos, Mirror mirror, Rotation rotation, TemplateFeatureConfig config, DecoratorConfig<T> decorator) {
        if (config.templates.isEmpty()) {
            FeatureManager.LOG.warn("Empty template list for config: {}", config.name);
            return false;
        }
        Template template = nextTemplate(config.templates, rand);
        T buffer = decorator.createBuffer(world);
        if (template.paste(buffer, pos, mirror, rotation, config.paste)) {
            Biome biome = world.getBiome(pos);
            for (Decorator<T> d : decorator.getDecorators(biome.getRegistryName())) {
                d.apply(buffer, rand);
            }
            return true;
        }
        return false;
    }

    public static  <T extends IWorld> boolean pasteChecked(IWorld world, Random rand, BlockPos pos, Mirror mirror, Rotation rotation, TemplateFeatureConfig config, DecoratorConfig<T> decorator) {
        if (config.templates.isEmpty()) {
            FeatureManager.LOG.warn("Empty template list for config: {}", config.name);
            return false;
        }
        Template template = nextTemplate(config.templates, rand);
        T buffer = decorator.createBuffer(world);
        if (template.pasteWithBoundsCheck(buffer, pos, mirror, rotation, config.paste)) {
            Biome biome = world.getBiome(pos);
            for (Decorator<T> d : decorator.getDecorators(biome.getRegistryName())) {
                d.apply(buffer, rand);
            }
            return true;
        }
        return false;
    }

    public static Template nextTemplate(List<Template> templates, Random random) {
        return templates.get(random.nextInt(templates.size()));
    }

    public static Mirror nextMirror(Random random) {
        return Mirror.values()[random.nextInt(Mirror.values().length)];
    }

    public static Rotation nextRotation(Random random) {
        return Rotation.values()[random.nextInt(Rotation.values().length)];
    }
}
