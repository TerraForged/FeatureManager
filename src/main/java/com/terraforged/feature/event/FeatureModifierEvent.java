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

package com.terraforged.feature.event;

import com.terraforged.feature.modifier.FeatureModifiers;
import com.terraforged.feature.modifier.ModifierList;
import com.terraforged.feature.predicate.FeaturePredicate;
import com.terraforged.feature.transformer.FeatureReplacer;
import com.terraforged.feature.transformer.FeatureTransformer;
import net.minecraft.world.IWorld;
import net.minecraft.world.WorldType;
import net.minecraftforge.eventbus.api.Event;

public class FeatureModifierEvent extends Event {

    private final IWorld world;
    private final FeatureModifiers modifiers;

    public FeatureModifierEvent(IWorld world, FeatureModifiers modifiers) {
        this.world = world;
        this.modifiers = modifiers;
    }

    public IWorld getWorld() {
        return world;
    }

    public WorldType getWorldType() {
        return getWorld().getWorld().getWorldType();
    }

    public ModifierList<FeatureReplacer> getReplacers() {
        return modifiers.getReplacers();
    }

    public ModifierList<FeaturePredicate> getPredicates() {
        return modifiers.getPredicates();
    }

    public ModifierList<FeatureTransformer> getTransformers() {
        return modifiers.getTransformers();
    }
}
