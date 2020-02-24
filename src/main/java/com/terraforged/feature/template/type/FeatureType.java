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

import com.terraforged.feature.matcher.feature.FeatureMatcher;
import com.terraforged.feature.template.decorator.DecoratorFactory;
import com.terraforged.feature.template.placement.Placement;
import net.minecraft.world.gen.feature.Feature;

public class FeatureType {

    private final String name;
    private final Placement placement;
    private final DecoratorFactory factory;

    public FeatureType(String name, Placement placement, DecoratorFactory factory) {
        this.name = name;
        this.placement = placement;
        this.factory = factory;
    }

    public String getName() {
        return name;
    }

    public Placement getPlacement() {
        return placement;
    }

    public DecoratorFactory getFactory() {
        return factory;
    }

    @Override
    public String toString() {
        return name;
    }

    public FeatureMatcher matcher() {
        FeatureMatcher.Builder builder = FeatureMatcher.builder();
        for (Feature<?> feature : FeatureTypes.getFeatures(this)) {
            builder.or(feature);
        }
        return builder.build();
    }
}
