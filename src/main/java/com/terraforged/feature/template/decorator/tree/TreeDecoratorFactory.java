package com.terraforged.feature.template.decorator.tree;

import com.google.gson.JsonElement;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import com.terraforged.feature.template.decorator.DecoratedFeature;
import com.terraforged.feature.template.decorator.Decorator;
import com.terraforged.feature.template.decorator.DecoratorFactory;
import com.terraforged.feature.template.type.TypedFeature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.treedecorator.TreeDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class TreeDecoratorFactory implements DecoratorFactory {

    public static final TreeDecoratorFactory INSTANCE = new TreeDecoratorFactory();

    @Override
    public <T extends Feature<NoFeatureConfig> & TypedFeature> Optional<DecoratedFeature<?, ?>> apply(T feature, JsonElement decorators) {
        if (!decorators.isJsonObject()) {
            return Optional.empty();
        }

        List<Decorator<TreeRecorder>> list = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : decorators.getAsJsonObject().entrySet()) {
            if (!entry.getValue().isJsonObject()) {
                continue;
            }
            Registry.TREE_DECORATOR_TYPE.getValue(new ResourceLocation(entry.getKey())).ifPresent(type -> {
                Dynamic<JsonElement> dynamic = new Dynamic<>(JsonOps.INSTANCE, entry.getValue());
                TreeDecorator decorator = type.func_227431_a_(dynamic);
                list.add(new TreeFeatureDecorator(decorator));
            });
        }

        if (list.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new DecoratedFeature<>(feature, list, FACTORY));
    }

    private static final Function<IWorld, TreeRecorder> FACTORY = w -> {
        if (w instanceof TreeRecorder) {
            return (TreeRecorder) w;
        }
        return new TreeRecorder(w);
    };
}
