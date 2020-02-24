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

package com.terraforged.feature.template.type;

import com.terraforged.feature.template.decorator.DecoratorFactory;
import com.terraforged.feature.template.decorator.tree.TreeDecoratorFactory;
import com.terraforged.feature.template.placement.Placement;
import com.terraforged.feature.template.placement.TreePlacement;
import net.minecraft.world.gen.feature.Feature;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FeatureTypes {

    private static final Map<String, FeatureType> types = new HashMap<>();
    private static final Map<FeatureType, Set<Feature<?>>> features = new HashMap<>();

    public static final FeatureType ANY = register("any", Placement.ANY, DecoratorFactory.NONE);
    public static final FeatureType TREE = register("tree", TreePlacement.PLACEMENT, TreeDecoratorFactory.INSTANCE);

    public static FeatureType getType(String type) {
        return types.getOrDefault(type, ANY);
    }

    public static void register(FeatureType type, Feature<?> feature) {
        features.computeIfAbsent(type, t -> new HashSet<>()).add(feature);
    }

    protected static Collection<Feature<?>> getFeatures(FeatureType type) {
        return features.getOrDefault(type, Collections.emptySet());
    }

    private static FeatureType register(String name, Placement placement, DecoratorFactory factory) {
        return register(new FeatureType(name, placement, factory));
    }

    private static FeatureType register(FeatureType type) {
        types.put(type.getName(), type);
        return type;
    }
}
