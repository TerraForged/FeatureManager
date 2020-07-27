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

package com.terraforged.fm.matcher;

import com.google.gson.JsonElement;
import com.terraforged.fm.matcher.biome.BiomeMatcher;
import com.terraforged.fm.matcher.feature.FeatureMatcher;
import net.minecraft.world.biome.Biome;

import java.util.function.BiPredicate;

public class BiomeFeatureMatcher implements BiPredicate<Biome, JsonElement>, Comparable<BiomeFeatureMatcher> {

    public static final BiomeFeatureMatcher ANY = new BiomeFeatureMatcher(BiomeMatcher.ANY, FeatureMatcher.ANY);

    private final BiomeMatcher biomeMatcher;
    private final FeatureMatcher featureMatcher;

    public BiomeFeatureMatcher(BiomeMatcher biomeMatcher, FeatureMatcher featureMatcher) {
        this.biomeMatcher = biomeMatcher;
        this.featureMatcher = featureMatcher;
    }

    @Override
    public boolean test(Biome biome, JsonElement feature) {
        return getBiomeMatcher().test(biome) && getFeatureMatcher().test(feature);
    }

    public BiomeMatcher getBiomeMatcher() {
        return biomeMatcher;
    }

    public FeatureMatcher getFeatureMatcher() {
        return featureMatcher;
    }

    @Override
    public int compareTo(BiomeFeatureMatcher o) {
        return biomeMatcher.compareTo(o.biomeMatcher);
    }
}
