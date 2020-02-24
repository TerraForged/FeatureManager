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

        List<Decorator<TreeBuffer>> list = new ArrayList<>();
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

    private static final Function<IWorld, TreeBuffer> FACTORY = w -> {
        if (w instanceof TreeBuffer) {
            return (TreeBuffer) w;
        }
        return new TreeBuffer(w);
    };
}
